package eu.wedgess.piholecontrol.data.repository

import eu.wedgess.piholecontrol.data.api.DashboardApiService
import eu.wedgess.piholecontrol.data.mappers.toEntity
import eu.wedgess.piholecontrol.data.model.responses.PiHoleClientsOverTimeResponseData
import eu.wedgess.piholecontrol.domain.model.ClientOverTimeEntity
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.OverTimeEntity
import eu.wedgess.piholecontrol.domain.model.QueriesOverTimeEntity
import eu.wedgess.piholecontrol.domain.model.SummaryEntity
import eu.wedgess.piholecontrol.domain.repository.DashboardRepository
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import kotlinx.coroutines.withContext

class DashboardRepositoryImpl(
    private val dashboardApiService: DashboardApiService,
    private val dispatcherProvider: DispatcherProvider
) : DashboardRepository {

    override suspend fun fetchStatusSummary(
        activeConnection: ConnectionEntity
    ): Result<SummaryEntity> = withContext(dispatcherProvider.io) {
        dashboardApiService.fetchStatusSummary(activeConnection).mapCatching { it.toEntity() }
    }

    override suspend fun fetchOverTimeData10Minutes(
        activeConnection: ConnectionEntity
    ): Result<QueriesOverTimeEntity> = withContext(dispatcherProvider.io) {
        dashboardApiService.fetchOverTimeData10Minutes(activeConnection).mapCatching { it.toEntity() }
    }

    override suspend fun fetchOverTimeDataClients(
        activeConnection: ConnectionEntity
    ): Result<List<ClientOverTimeEntity>> = withContext(dispatcherProvider.io) {
        dashboardApiService.fetchOverTimeDataClients(activeConnection)
            .mapCatching { it.mapToClientsOverTime() }
    }

    private fun PiHoleClientsOverTimeResponseData.mapToClientsOverTime(): List<ClientOverTimeEntity> {
        return this.clients.map { (ip, clientInfo) ->
            ClientOverTimeEntity(
                clientName = clientInfo.name ?: "",
                clientIp = ip,
                clientActivity = this.history.map { history ->
                    OverTimeEntity(
                        timestamp = history.timestamp?.toLong()?.times(1000) ?: 0,
                        hits = history.data[ip]?.toLong() ?: 0
                    )
                }
            )
        }.sortedByDescending { client ->
            client.clientActivity.sumOf { it.hits }
        }
    }
}
