package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.extensions.fetchBaseRequestInfo
import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.PiHoleClientsOverTimeResponseData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleOverTimeResponseData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleSummaryResponseData
import eu.wedgess.piholecontrol.data.utils.requestResult
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import javax.inject.Inject

class DashboardApiServiceImpl @Inject constructor(
    @DefaultHttpClient private val defaultHttpClient: HttpClient,
    @TrustAllCertificatesHttpClient private val trustAllCertsHttpClient: HttpClient
) : DashboardApiService {

    override suspend fun fetchStatusSummary(
        connection: ConnectionEntity
    ): PiHoleApiResult<PiHoleSummaryResponseData> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfo(connection, path = SUMMARY_ENDPOINT)
        }
    }

    override suspend fun fetchOverTimeData10Minutes(
        connection: ConnectionEntity
    ): PiHoleApiResult<PiHoleOverTimeResponseData> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfo(connection, path = OVERTIME_DATA_ENDPOINT)
        }
    }

    override suspend fun fetchOverTimeDataClients(
        connection: ConnectionEntity
    ): PiHoleApiResult<PiHoleClientsOverTimeResponseData> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfo(connection, path = CLIENTS_OVERTIME_DATA_ENDPOINT)
        }
    }

    companion object {
        private const val SUMMARY_ENDPOINT = "/stats/summary"
        private const val OVERTIME_DATA_ENDPOINT = "/history"
        private const val CLIENTS_OVERTIME_DATA_ENDPOINT = "/history/clients"
    }
}
