package eu.wedgess.piholecontrol.domain.repository

import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.domain.model.ClientOverTimeEntity
import eu.wedgess.piholecontrol.domain.model.QueriesOverTimeEntity
import eu.wedgess.piholecontrol.domain.model.SummaryEntity

interface DashboardRepository {
    suspend fun fetchStatusSummary(activeMiHole: ConnectionInfo): Result<SummaryEntity>
    suspend fun fetchOverTimeData10Minutes(activeMiHole: ConnectionInfo): Result<QueriesOverTimeEntity>
    suspend fun fetchOverTimeDataClients(activeMiHole: ConnectionInfo): Result<List<ClientOverTimeEntity>>
}