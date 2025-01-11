package eu.wedgess.piholecontrol.data.utils

import eu.wedgess.piholecontrol.data.model.responses.ApiErrorResponse
import eu.wedgess.piholecontrol.data.model.responses.exceptions.ApiErrorThrowable
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleErrorResponseDataV6
import eu.wedgess.piholecontrol.utils.extensions.resultOf
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

// Safe request function that uses Result<T> and catches exceptions, supports error deserialization with ApiError
internal suspend inline fun <reified T> HttpClient.requestResult(
    block: HttpRequestBuilder.() -> Unit
): Result<T> {
    return resultOf {
        val response = request { block() }
        response.handleResponse<T>()
    }.recoverCatching { e ->
        throw handleRecovery(e)
    }
}

// Extracted recoverCatching logic to handle different types of exceptions
private suspend fun handleRecovery(e: Throwable): Throwable {
    return when (e) {
        is ClientRequestException -> handleClientError(e)
        is ServerResponseException -> handleServerError(e)
        is IOException -> Exception("Network error occurred: ${e.message}", e)
        is SerializationException -> Exception("Serialization error: ${e.message}", e)
        else -> Exception("Unknown error occurred: ${e.message}", e)
    }
}

// Logic to handle client errors (4xx)
private suspend fun handleClientError(e: ClientRequestException): Throwable {
    val errorBody = e.errorBody<PiHoleErrorResponseDataV6>()
    return if (errorBody != null) {
        ApiErrorThrowable(ApiErrorResponse.V6(errorBody))
    } else {
        ApiErrorThrowable(ApiErrorResponse.V5(e.message))
    }
}

// Logic to handle server errors (5xx)
private suspend fun handleServerError(e: ServerResponseException): Throwable {
    val errorBody = e.errorBody<PiHoleErrorResponseDataV6>()
    return if (errorBody != null) {
        ApiErrorThrowable(ApiErrorResponse.V6(errorBody))
    } else {
        ApiErrorThrowable(ApiErrorResponse.V5(e.message))
    }
}

private suspend inline fun <reified T> HttpResponse.handleResponse(): T {
    return when (this.status.value) {
        in HttpStatusCode.BadRequest.value..HttpStatusCode.TooManyRequests.value -> {
            throw ClientRequestException(this, this.bodyAsText())
        }

        in HttpStatusCode.InternalServerError.value..HttpStatusCode.InsufficientStorage.value -> {
            throw ServerResponseException(this, this.bodyAsText())
        }

        HttpStatusCode.NoContent.value -> Unit as T
        HttpStatusCode.OK.value, HttpStatusCode.Created.value -> this.body()
        else -> throw ResponseException(this, this.bodyAsText())
    }
}

// Extension function to safely get the error body from a ResponseException, return null if deserialization fails
private suspend inline fun <reified E> ResponseException.errorBody(): E? {
    return try {
        response.body<E>()
    } catch (e: SerializationException) {
        null // If deserialization fails, return null or handle appropriately
    } catch (e: Exception) {
        null // Any other errors while deserializing error body should return null
    }
}
