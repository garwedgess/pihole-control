package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.extensions.fetchBaseRequestInfo
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.data.model.responses.PiHoleClientsOverTimeData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleOverTimeData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleSummary
import eu.wedgess.piholecontrol.data.utils.requestResult
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import io.ktor.client.HttpClient
import javax.inject.Inject

class DashboardApiServiceImpl @Inject constructor(
    @DefaultHttpClient private val defaultHttpClient: HttpClient,
    @TrustAllCertificatesHttpClient private val trustAllCertsHttpClient: HttpClient
) : DashboardApiService {

    override suspend fun fetchStatusSummary(connection: ConnectionEntity): Result<PiHoleSummary> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleSummary, String> {
            fetchBaseRequestInfo(connection)
            url {
                parameters["summaryRaw"] = true.toString()
            }
        }
    }

    override suspend fun fetchOverTimeData10Minutes(
        connection: ConnectionEntity
    ): Result<PiHoleOverTimeData> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleOverTimeData, String> {
            fetchBaseRequestInfo(connection)
            url {
                parameters["overTimeData10mins"] = true.toString()
            }
        }
    }

    override suspend fun fetchOverTimeDataClients(
        connection: ConnectionEntity
    ): Result<PiHoleClientsOverTimeData> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleClientsOverTimeData, String> {
            fetchBaseRequestInfo(connection)
            url {
                parameters["overTimeDataClients"] = true.toString()
                parameters["getClientNames"] = true.toString()
            }
        }
    }
}
