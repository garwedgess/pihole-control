package eu.wedgess.piholecontrol.data.api.v6

import eu.wedgess.piholecontrol.data.extensions.fetchBaseRequestInfoV6
import eu.wedgess.piholecontrol.data.model.requests.PiHoleStatusRequestDataV6
import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleStatusResponseDataV6
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

class StatusApiServiceV6Impl @Inject constructor(
    @DefaultHttpClient private val defaultHttpClient: HttpClient,
    @TrustAllCertificatesHttpClient private val trustAllCertsHttpClient: HttpClient
) : StatusApiServiceV6 {

    override suspend fun fetchStatus(
        activeMiHole: ConnectionEntity.Version6
    ): PiHoleApiResult<PiHoleStatusResponseDataV6> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfoV6(activeMiHole, path = STATUS_ENDPOINT)
        }
    }

    override suspend fun enableAdBlocking(
        activeMiHole: ConnectionEntity.Version6
    ): PiHoleApiResult<PiHoleStatusResponseDataV6> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfoV6(activeMiHole, path = STATUS_ENDPOINT)
            method = HttpMethod.Post
            contentType(ContentType.Application.Json)
            setBody(
                PiHoleStatusRequestDataV6(
                    blocking = true,
                    timer = null
                )
            )
        }
    }

    override suspend fun disableAdBlocking(
        activeMiHole: ConnectionEntity.Version6,
        duration: Duration
    ): PiHoleApiResult<PiHoleStatusResponseDataV6> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfoV6(activeMiHole, path = STATUS_ENDPOINT)
            method = HttpMethod.Post
            contentType(ContentType.Application.Json)
            setBody(
                PiHoleStatusRequestDataV6(
                    blocking = false,
                    timer = duration.takeUnless { it == Duration.INFINITE }
                        ?.toLong(DurationUnit.SECONDS)
                )
            )
        }
    }

    companion object {
        private const val STATUS_ENDPOINT = "/api/dns/blocking"
    }
}
