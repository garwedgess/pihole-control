package eu.wedgess.piholecontrol.data.api.v6

import eu.wedgess.piholecontrol.data.extensions.fetchBaseRequestInfoV6
import eu.wedgess.piholecontrol.data.model.requests.PiHoleAuthPasswordRequestDataV6
import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleAuthSessionStatusResponseDataV6
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

class AuthApiServiceV6Impl @Inject constructor(
    @AuthHttpClient private val defaultHttpClient: HttpClient,
    @AuthTrustAllCertificatesHttpClient private val trustAllCertsHttpClient: HttpClient
) : AuthApiServiceV6 {

    override suspend fun validateSessionId(
        connection: ConnectionEntity.Version6
    ): PiHoleApiResult<PiHoleAuthSessionStatusResponseDataV6> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfoV6(connection, path = AUTH_ENDPOINT)
        }
    }

    override suspend fun generateSessionId(
        connection: ConnectionEntity.Version6
    ): PiHoleApiResult<PiHoleAuthSessionStatusResponseDataV6> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfoV6(connection, path = AUTH_ENDPOINT)
            url {
                method = HttpMethod.Post
                contentType(ContentType.Application.Json)
                setBody(
                    PiHoleAuthPasswordRequestDataV6(password = connection.password)
                )
            }
        }
    }

    companion object {
        private const val AUTH_ENDPOINT = "/api/auth"
    }
}
