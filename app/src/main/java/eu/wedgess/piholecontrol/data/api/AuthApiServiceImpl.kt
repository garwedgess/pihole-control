package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.extensions.fetchBaseRequestInfo
import eu.wedgess.piholecontrol.data.model.requests.PiHoleAuthPasswordRequestData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.PiHoleAuthSessionStatusResponseData
import eu.wedgess.piholecontrol.data.utils.requestResult
import eu.wedgess.piholecontrol.di.annotations.AuthHttpClient
import eu.wedgess.piholecontrol.di.annotations.AuthTrustAllCertificatesHttpClient
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import javax.inject.Inject

class AuthApiServiceImpl @Inject constructor(
    @AuthHttpClient private val defaultHttpClient: HttpClient,
    @AuthTrustAllCertificatesHttpClient private val trustAllCertsHttpClient: HttpClient
) : AuthApiService {

    override suspend fun validateSessionId(
        connection: ConnectionEntity
    ): PiHoleApiResult<PiHoleAuthSessionStatusResponseData> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfo(connection, path = AUTH_ENDPOINT)
        }
    }

    override suspend fun generateSessionId(
        connection: ConnectionEntity
    ): PiHoleApiResult<PiHoleAuthSessionStatusResponseData> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfo(connection, path = AUTH_ENDPOINT)
            url {
                method = HttpMethod.Post
                contentType(ContentType.Application.Json)
                setBody(
                    PiHoleAuthPasswordRequestData(password = connection.password)
                )
            }
        }
    }

    companion object {
        private const val AUTH_ENDPOINT = "/auth"
    }
}
