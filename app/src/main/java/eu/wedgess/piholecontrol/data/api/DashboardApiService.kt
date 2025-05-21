package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.model.responses.PiHoleClientsOverTimeResponseData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleOverTimeResponseData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleSummaryResponseData
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

interface DashboardApiService {
    suspend fun fetchStatusSummary(connection: ConnectionEntity): Result<PiHoleSummaryResponseData>

    suspend fun fetchOverTimeData10Minutes(connection: ConnectionEntity): Result<PiHoleOverTimeResponseData>

    suspend fun fetchOverTimeDataClients(
        connection: ConnectionEntity
    ): Result<PiHoleClientsOverTimeResponseData>
}
