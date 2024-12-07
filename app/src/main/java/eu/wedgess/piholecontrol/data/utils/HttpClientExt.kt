package eu.wedgess.piholecontrol.data.utils

import eu.wedgess.piholecontrol.utils.extensions.resultOf
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException

// Safe request function that uses Result<T> and catches exceptions, supports error deserialization with E
internal suspend inline fun <reified T, reified E> HttpClient.requestResult(
    block: HttpRequestBuilder.() -> Unit
): Result<T> {
    return resultOf {
        val response = request { block() }
        response.handleResponse<T, E>()
    }.recoverCatching { e ->
        throw handleRecovery<E>(e)
    }
}

// Extracted recoverCatching logic to handle different types of exceptions
private suspend inline fun <reified E> handleRecovery(e: Throwable): Throwable {
    return when (e) {
        is ClientRequestException -> handleClientError(e, e.errorBody<E>())
        is ServerResponseException -> handleServerError(e, e.errorBody<E>())
        is IOException -> Exception("Network error occurred: ${e.message}", e)
        is SerializationException -> Exception("Serialization error: ${e.message}", e)
        else -> Exception("Unknown error occurred: ${e.message}", e)
    }
}

// Logic to handle client errors (4xx), customize based on requirements
private fun <E> handleClientError(e: ClientRequestException, errorBody: E?): ClientRequestException {
    println("Client error: ${e.response.status}, Body: $errorBody")
    return ClientRequestException(e.response, "Client error: ${e.response.status}, Body: $errorBody")
}

// Logic to handle server errors (5xx), customize based on requirements
private fun <E> handleServerError(e: ServerResponseException, errorBody: E?): ServerResponseException {
    println("Server error: ${e.response.status}, Body: $errorBody")
    return ServerResponseException(e.response, "Server error: ${e.response.status}, Body: $errorBody")
}

private suspend inline fun <reified T, reified E> HttpResponse.handleResponse(): T {
    when (val statusCode = this.status.value) {
        in HttpStatusCode.BadRequest.value..HttpStatusCode.TooManyRequests.value -> {
            throw ClientRequestException(this, "Client error: HTTP $statusCode").also {
                it.errorBody<E>()
            }
        }

        in HttpStatusCode.InternalServerError.value..HttpStatusCode.InsufficientStorage.value -> {
            throw ServerResponseException(this, "Server error: HTTP $statusCode").also {
                it.errorBody<E>()
            }
        }
        HttpStatusCode.NoContent.value -> throw Exception("No content: HTTP $statusCode")
        HttpStatusCode.OK.value, HttpStatusCode.Created.value -> return this.body()
        else ->  throw ResponseException(this, "Unexpected HTTP status: $statusCode")
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