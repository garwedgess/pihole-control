package eu.wedgess.piholecontrol.data.api.v6

import eu.wedgess.piholecontrol.data.extensions.fetchBaseRequestInfoV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleClientsOverTimeV6Data
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleOverTimeV6Data
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleSummaryV6Data
import eu.wedgess.piholecontrol.data.utils.requestResult
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import javax.inject.Inject

class DashboardApiServiceV6Impl @Inject constructor(
    @DefaultHttpClient private val defaultHttpClient: HttpClient,
    @TrustAllCertificatesHttpClient private val trustAllCertsHttpClient: HttpClient
) : DashboardApiServiceV6 {

    override suspend fun fetchStatusSummary(connection: ConnectionEntity): Result<PiHoleSummaryV6Data> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleSummaryV6Data, String> {
            fetchBaseRequestInfoV6(connection, path = SUMMARY_ENDPOINT)

        }
    }

    override suspend fun fetchOverTimeData10Minutes(
        connection: ConnectionEntity
    ): Result<PiHoleOverTimeV6Data> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleOverTimeV6Data, String> {
            fetchBaseRequestInfoV6(connection, path = OVERTIME_DATA_ENDPOINT)
        }
    }

    override suspend fun fetchOverTimeDataClients(
        connection: ConnectionEntity
    ): Result<PiHoleClientsOverTimeV6Data> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleClientsOverTimeV6Data, String> {
            fetchBaseRequestInfoV6(connection, path = CLIENTS_OVERTIME_DATA_ENDPOINT)
        }
    }

    companion object {
        private const val SUMMARY_ENDPOINT = "/api/stats/summary"
        private const val OVERTIME_DATA_ENDPOINT = "/api/history"
        private const val CLIENTS_OVERTIME_DATA_ENDPOINT = "/api/history/clients"
    }
}
