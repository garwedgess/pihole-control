package eu.wedgess.piholecontrol.data.utils

import eu.wedgess.piholecontrol.data.model.ResponseResult
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException

internal suspend inline fun <reified T, reified E> HttpClient.safeRequest(
    block: HttpRequestBuilder.() -> Unit,
): ResponseResult<T, E> =
    try {
        request { block() }.handleResponse()
    } catch (e: ClientRequestException) {
        ResponseResult.Error.Api(e.response.status.value, e.errorBody())
    } catch (e: ServerResponseException) {
        ResponseResult.Error.Api(e.response.status.value, e.errorBody())
    } catch (e: IOException) {
        ResponseResult.Error.Network(e)
    } catch (e: NoTransformationFoundException) {
        ResponseResult.Error.Serialization(SerializationException(e.message, e.cause))
    } catch (e: SerializationException) {
        ResponseResult.Error.Serialization(e)
    } catch (e: JsonConvertException) {
        ResponseResult.Error.Serialization(SerializationException(e.message, e.cause))
    } catch (e: Exception) {
        ResponseResult.Error.Unknown(e)
    }

private suspend inline fun <reified T, reified E> HttpResponse.handleResponse(): ResponseResult<T, E> {
    when (val statusCode = this.status.value) {
        in HttpStatusCode.MultipleChoices.value..HttpStatusCode.PermanentRedirect.value -> throw RedirectResponseException(
            this,
            "Code: $statusCode"
        )

        in HttpStatusCode.BadRequest.value..HttpStatusCode.TooManyRequests.value -> throw ClientRequestException(
            this,
            "Code: $statusCode"
        )

        in HttpStatusCode.InternalServerError.value..HttpStatusCode.InsufficientStorage.value -> throw ServerResponseException(
            this,
            "Code: $statusCode"
        )

        HttpStatusCode.NoContent.value -> {
            return ResponseResult.Error.Unknown(Exception("No data"))
        }

        HttpStatusCode.OK.value, HttpStatusCode.Created.value -> {
            return ResponseResult.Success(this.body())
        }

        else -> {
            throw ResponseException(this, "Code: $statusCode")
        }

    }
}


/**
 * Extension function for getting the error response body from the [ResponseException]
 *
 * @param E - Type of the error class
 * @return - nullable error class
 */
private suspend inline fun <reified E> ResponseException.errorBody(): E? =
    try {
        response.body()
    } catch (e: SerializationException) {
        null
    }