package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.extensions.fetchBaseRequestInfo
import eu.wedgess.piholecontrol.data.model.responses.PiHoleStatusResponse
import eu.wedgess.piholecontrol.data.utils.requestResult
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import javax.inject.Inject
import kotlin.time.Duration

class StatusApiServiceImpl @Inject constructor(
    @DefaultHttpClient private val defaultHttpClient: HttpClient,
    @TrustAllCertificatesHttpClient private val trustAllCertsHttpClient: HttpClient
) : StatusApiService {

    override suspend fun fetchStatus(activeMiHole: ConnectionEntity): Result<PiHoleStatusResponse> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleStatusResponse, String> {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["status"] = true.toString()
            }
        }
    }

    override suspend fun enableAdBlocking(
        activeMiHole: ConnectionEntity
    ): Result<PiHoleStatusResponse> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleStatusResponse, String> {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["enable"] = true.toString()
            }
        }
    }

    override suspend fun disableAdBlocking(
        activeMiHole: ConnectionEntity,
        duration: Duration
    ): Result<PiHoleStatusResponse> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleStatusResponse, String> {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["disable"] =
                    (if (duration.isInfinite()) 0 else duration.inWholeSeconds).run { toString() }
            }
        }
    }
}
