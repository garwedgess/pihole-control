package eu.wedgess.piholecontrol.data.api.v6

import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleClientsOverTimeV6Data
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleOverTimeV6Data
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleSummaryV6Data
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

interface DashboardApiServiceV6 {
    suspend fun fetchStatusSummary(connection: ConnectionEntity): Result<PiHoleSummaryV6Data>

    suspend fun fetchOverTimeData10Minutes(connection: ConnectionEntity): Result<PiHoleOverTimeV6Data>

    suspend fun fetchOverTimeDataClients(
        connection: ConnectionEntity
    ): Result<PiHoleClientsOverTimeV6Data>
}