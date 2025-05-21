package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.extensions.fetchBaseRequestInfo
import eu.wedgess.piholecontrol.data.model.requests.PiHoleStatusRequestData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.PiHoleStatusResponseData
import eu.wedgess.piholecontrol.data.utils.requestResult
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.DurationUnit

class StatusApiServiceImpl @Inject constructor(
    @DefaultHttpClient private val defaultHttpClient: HttpClient,
    @TrustAllCertificatesHttpClient private val trustAllCertsHttpClient: HttpClient
) : StatusApiService {

    override suspend fun fetchStatus(
        activeMiHole: ConnectionEntity
    ): PiHoleApiResult<PiHoleStatusResponseData> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfo(activeMiHole, path = STATUS_ENDPOINT)
        }
    }

    override suspend fun enableAdBlocking(
        activeMiHole: ConnectionEntity
    ): PiHoleApiResult<PiHoleStatusResponseData> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfo(activeMiHole, path = STATUS_ENDPOINT)
            method = HttpMethod.Post
            contentType(ContentType.Application.Json)
            setBody(
                PiHoleStatusRequestData(
                    blocking = true,
                    timer = null
                )
            )
        }
    }

    override suspend fun disableAdBlocking(
        activeMiHole: ConnectionEntity,
        duration: Duration
    ): PiHoleApiResult<PiHoleStatusResponseData> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfo(activeMiHole, path = STATUS_ENDPOINT)
            method = HttpMethod.Post
            contentType(ContentType.Application.Json)
            setBody(
                PiHoleStatusRequestData(
                    blocking = false,
                    timer = duration.takeUnless { it == Duration.INFINITE }
                        ?.toLong(DurationUnit.SECONDS)
                )
            )
        }
    }

    companion object {
        private const val STATUS_ENDPOINT = "/dns/blocking"
    }
}
