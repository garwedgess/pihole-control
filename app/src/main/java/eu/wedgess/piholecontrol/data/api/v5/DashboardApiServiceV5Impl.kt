package eu.wedgess.piholecontrol.data.api.v5

import eu.wedgess.piholecontrol.data.extensions.fetchBaseRequestInfoV5
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleClientsOverTimeV5Data
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleOverTimeV5Data
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleSummaryV5Data
import eu.wedgess.piholecontrol.data.utils.requestResult
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import javax.inject.Inject

class DashboardApiServiceV5Impl @Inject constructor(
    @DefaultHttpClient private val defaultHttpClient: HttpClient,
    @TrustAllCertificatesHttpClient private val trustAllCertsHttpClient: HttpClient
) : DashboardApiServiceV5 {

    override suspend fun fetchStatusSummary(connection: ConnectionEntity): Result<PiHoleSummaryV5Data> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleSummaryV5Data, String> {
            fetchBaseRequestInfoV5(connection)
            url {
                parameters["summaryRaw"] = true.toString()
            }
        }
    }

    override suspend fun fetchOverTimeData10Minutes(
        connection: ConnectionEntity
    ): Result<PiHoleOverTimeV5Data> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleOverTimeV5Data, String> {
            fetchBaseRequestInfoV5(connection)
            url {
                parameters["overTimeData10mins"] = true.toString()
            }
        }
    }

    override suspend fun fetchOverTimeDataClients(
        connection: ConnectionEntity
    ): Result<PiHoleClientsOverTimeV5Data> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleClientsOverTimeV5Data, String> {
            fetchBaseRequestInfoV5(connection)
            url {
                parameters["overTimeDataClients"] = true.toString()
                parameters["getClientNames"] = true.toString()
            }
        }
    }
}
