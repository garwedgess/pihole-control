package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.extensions.fetchBaseRequestInfo
import eu.wedgess.piholecontrol.data.model.ConnectionInfo
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

    override suspend fun fetchStatusSummary(activeConnection: ConnectionInfo): Result<PiHoleSummary> {
        val client = if (activeConnection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleSummary, String> {
            fetchBaseRequestInfo(activeConnection)
            url {
                parameters["summaryRaw"] = true.toString()
            }
        }
    }

    override suspend fun fetchOverTimeData10Minutes(activeConnection: ConnectionInfo): Result<PiHoleOverTimeData> {
        val client = if (activeConnection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleOverTimeData, String> {
            fetchBaseRequestInfo(activeConnection)
            url {
                parameters["overTimeData10mins"] = true.toString()
            }
        }
    }

    override suspend fun fetchOverTimeDataClients(activeConnection: ConnectionInfo): Result<PiHoleClientsOverTimeData> {
        val client = if (activeConnection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleClientsOverTimeData, String> {
            fetchBaseRequestInfo(activeConnection)
            url {
                parameters["overTimeDataClients"] = true.toString()
                parameters["getClientNames"] = true.toString()
            }
        }
    }
}
