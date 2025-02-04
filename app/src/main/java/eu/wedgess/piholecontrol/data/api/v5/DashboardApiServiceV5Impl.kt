package eu.wedgess.piholecontrol.data.api.v5

import eu.wedgess.piholecontrol.data.extensions.fetchBaseRequestInfoV5
import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleClientsOverTimeResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleOverTimeResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleSummaryResponseDataV5
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

    override suspend fun fetchStatusSummary(
        connection: ConnectionEntity.Version5
    ): PiHoleApiResult<PiHoleSummaryResponseDataV5> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfoV5(connection)
            url {
                parameters["summaryRaw"] = true.toString()
            }
        }
    }

    override suspend fun fetchOverTimeData10Minutes(
        connection: ConnectionEntity.Version5
    ): PiHoleApiResult<PiHoleOverTimeResponseDataV5> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfoV5(connection)
            url {
                parameters["overTimeData10mins"] = true.toString()
            }
        }
    }

    override suspend fun fetchOverTimeDataClients(
        connection: ConnectionEntity.Version5
    ): PiHoleApiResult<PiHoleClientsOverTimeResponseDataV5> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfoV5(connection)
            url {
                parameters["overTimeDataClients"] = true.toString()
                parameters["getClientNames"] = true.toString()
            }
        }
    }
}
