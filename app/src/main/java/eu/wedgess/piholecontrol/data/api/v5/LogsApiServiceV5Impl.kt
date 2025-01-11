package eu.wedgess.piholecontrol.data.api.v5

import eu.wedgess.piholecontrol.data.extensions.fetchBaseRequestInfoV5
import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleLogsResponseDataV5
import eu.wedgess.piholecontrol.data.utils.requestResult
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import javax.inject.Inject

class LogsApiServiceV5Impl @Inject constructor(
    @DefaultHttpClient private val defaultHttpClient: HttpClient,
    @TrustAllCertificatesHttpClient private val trustAllCertsHttpClient: HttpClient
) : LogsApiServiceV5 {

    override suspend fun fetchLogs(
        connection: ConnectionEntity.Version5,
        limit: Int
    ): PiHoleApiResult<PiHoleLogsResponseDataV5> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfoV5(connection)
            url {
                parameters["getAllQueries"] = limit.toString()
            }
        }
    }
}
