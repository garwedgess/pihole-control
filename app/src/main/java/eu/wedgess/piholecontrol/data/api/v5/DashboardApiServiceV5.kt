package eu.wedgess.piholecontrol.data.api.v5

import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleClientsOverTimeV5Data
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleOverTimeV5Data
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleSummaryV5Data
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

interface DashboardApiServiceV5 {
    suspend fun fetchStatusSummary(connection: ConnectionEntity): Result<PiHoleSummaryV5Data>

    suspend fun fetchOverTimeData10Minutes(connection: ConnectionEntity): Result<PiHoleOverTimeV5Data>

    suspend fun fetchOverTimeDataClients(
        connection: ConnectionEntity
    ): Result<PiHoleClientsOverTimeV5Data>
}
