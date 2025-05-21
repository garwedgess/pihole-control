package eu.wedgess.piholecontrol.data.utils

import eu.wedgess.piholecontrol.data.model.responses.ApiErrorResponse
import eu.wedgess.piholecontrol.data.model.responses.PiHoleErrorResponseData
import eu.wedgess.piholecontrol.data.model.responses.exceptions.ApiErrorThrowable
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
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

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

private suspend fun handleRecovery(e: Throwable): Throwable {
    return when (e) {
        is ClientRequestException -> handleClientError(e)
        is ServerResponseException -> handleServerError(e)
        is IOException -> Exception("Network error occurred: ${e.message}", e)
        is SerializationException -> Exception("Serialization error: ${e.message}", e)
        else -> Exception("Unknown error occurred: ${e.message}", e)
    }
}

private suspend fun handleClientError(e: ClientRequestException): Throwable {
    val errorBody = e.errorBody<PiHoleErrorResponseData>()
    return errorBody?.let { error -> ApiErrorThrowable(ApiErrorResponse(error)) } ?: e
}

private suspend fun handleServerError(e: ServerResponseException): Throwable {
    val errorBody = e.errorBody<PiHoleErrorResponseData>()
    return errorBody?.let { error -> ApiErrorThrowable(ApiErrorResponse(error)) } ?: e
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

private suspend inline fun <reified E> ResponseException.errorBody(): E? {
    return try {
        response.body<E>()
    } catch (e: SerializationException) {
        null // If deserialization fails, return null or handle appropriately
    } catch (e: Exception) {
        null // Any other errors while deserializing error body should return null
    }
}
