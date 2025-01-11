package eu.wedgess.piholecontrol.data.api.v6

import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleClientsOverTimeResponseDataV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleOverTimeResponseDataV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleSummaryResponseDataV6
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

interface DashboardApiServiceV6 {
    suspend fun fetchStatusSummary(connection: ConnectionEntity.Version6): Result<PiHoleSummaryResponseDataV6>

    suspend fun fetchOverTimeData10Minutes(connection: ConnectionEntity.Version6): Result<PiHoleOverTimeResponseDataV6>

    suspend fun fetchOverTimeDataClients(
        connection: ConnectionEntity.Version6
    ): Result<PiHoleClientsOverTimeResponseDataV6>
}
