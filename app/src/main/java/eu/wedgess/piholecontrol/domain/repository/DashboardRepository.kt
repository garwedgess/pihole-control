package eu.wedgess.piholecontrol.domain.repository

import eu.wedgess.piholecontrol.domain.model.ClientOverTimeEntity
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.QueriesOverTimeEntity
import eu.wedgess.piholecontrol.domain.model.SummaryEntity

interface DashboardRepository {
    suspend fun fetchStatusSummary(activeConnection: ConnectionEntity): Result<SummaryEntity>
    suspend fun fetchOverTimeData10Minutes(
        activeConnection: ConnectionEntity
    ): Result<QueriesOverTimeEntity>

    suspend fun fetchOverTimeDataClients(
        activeConnection: ConnectionEntity
    ): Result<List<ClientOverTimeEntity>>
}
