package eu.wedgess.piholecontrol.data.api.v5

import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleClientsOverTimeResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleOverTimeResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleSummaryResponseDataV5
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

interface DashboardApiServiceV5 {
    suspend fun fetchStatusSummary(
        connection: ConnectionEntity.Version5
    ): PiHoleApiResult<PiHoleSummaryResponseDataV5>

    suspend fun fetchOverTimeData10Minutes(
        connection: ConnectionEntity.Version5
    ): PiHoleApiResult<PiHoleOverTimeResponseDataV5>

    suspend fun fetchOverTimeDataClients(
        connection: ConnectionEntity.Version5
    ): PiHoleApiResult<PiHoleClientsOverTimeResponseDataV5>
}
