package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.data.model.responses.PiHoleClientsOverTimeData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleOverTimeData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleSummary

interface DashboardApiService {
    suspend fun fetchStatusSummary(connection: ConnectionEntity): Result<PiHoleSummary>

    suspend fun fetchOverTimeData10Minutes(connection: ConnectionEntity): Result<PiHoleOverTimeData>

    suspend fun fetchOverTimeDataClients(
        connection: ConnectionEntity
    ): Result<PiHoleClientsOverTimeData>
}