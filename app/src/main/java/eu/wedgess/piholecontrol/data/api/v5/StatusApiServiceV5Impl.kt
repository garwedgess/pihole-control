package eu.wedgess.piholecontrol.data.api.v5

import eu.wedgess.piholecontrol.data.extensions.fetchBaseRequestInfoV5
import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleStatusResponseDataV5
import eu.wedgess.piholecontrol.data.utils.requestResult
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import javax.inject.Inject
import kotlin.time.Duration

class StatusApiServiceV5Impl @Inject constructor(
    @DefaultHttpClient private val defaultHttpClient: HttpClient,
    @TrustAllCertificatesHttpClient private val trustAllCertsHttpClient: HttpClient
) : StatusApiServiceV5 {

    override suspend fun fetchStatus(
        connection: ConnectionEntity.Version5
    ): PiHoleApiResult<PiHoleStatusResponseDataV5> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfoV5(connection)
            url {
                parameters["status"] = true.toString()
            }
        }
    }

    override suspend fun enableAdBlocking(
        connection: ConnectionEntity.Version5
    ): PiHoleApiResult<PiHoleStatusResponseDataV5> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfoV5(connection)
            url {
                parameters["enable"] = true.toString()
            }
        }
    }

    override suspend fun disableAdBlocking(
        connection: ConnectionEntity.Version5,
        duration: Duration
    ): PiHoleApiResult<PiHoleStatusResponseDataV5> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfoV5(connection)
            url {
                parameters["disable"] =
                    (if (duration.isInfinite()) 0 else duration.inWholeSeconds).run { toString() }
            }
        }
    }
}
