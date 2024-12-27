package eu.wedgess.piholecontrol.data.repository

import eu.wedgess.piholecontrol.data.api.DashboardApiService
import eu.wedgess.piholecontrol.data.mappers.toQueriesOverTimeData
import eu.wedgess.piholecontrol.data.mappers.toSummaryEntity
import eu.wedgess.piholecontrol.domain.model.ClientOverTimeEntity
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.OverTimeEntity
import eu.wedgess.piholecontrol.domain.model.QueriesOverTimeEntity
import eu.wedgess.piholecontrol.domain.model.SummaryEntity
import eu.wedgess.piholecontrol.domain.repository.DashboardRepository
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import kotlinx.coroutines.withContext

class DashboardRepositoryImpl(
    private val api: DashboardApiService,
    private val dispatcherProvider: DispatcherProvider
) : DashboardRepository {

    override suspend fun fetchStatusSummary(
        activeMiHole: ConnectionEntity
    ): Result<SummaryEntity> = withContext(dispatcherProvider.io) {
        api.fetchStatusSummary(activeMiHole).mapCatching {
            it.toSummaryEntity()
        }
    }

    override suspend fun fetchOverTimeData10Minutes(
        activeMiHole: ConnectionEntity
    ): Result<QueriesOverTimeEntity> = withContext(dispatcherProvider.io) {
        api.fetchOverTimeData10Minutes(activeMiHole).mapCatching {
            it.toQueriesOverTimeData()
        }
    }

    override suspend fun fetchOverTimeDataClients(
        activeMiHole: ConnectionEntity
    ): Result<List<ClientOverTimeEntity>> = withContext(dispatcherProvider.io) {
        api.fetchOverTimeDataClients(activeMiHole).mapCatching { response ->
            response.clients
                .mapIndexed { index, client ->
                    client to response.clientsOverTime.map { (k, v) ->
                        k to v[index]
                    }
                }
                .groupBy({ it.first }, { it.second })
                .mapValues { it.value.flatten() }
                .map { (client, activity) ->
                    ClientOverTimeEntity(
                        clientName = client.name,
                        clientIp = client.ip,
                        clientActivity = activity.map {
                            OverTimeEntity(
                                it.first * 1000L,
                                it.second.toLong()
                            )
                        }

                    )
                }
                .sortedByDescending { client ->
                    client.clientActivity.sumOf { it.hits }
                }
        }
    }
}
