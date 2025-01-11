package eu.wedgess.piholecontrol.data.repository

import eu.wedgess.piholecontrol.data.api.v5.DashboardApiServiceV5
import eu.wedgess.piholecontrol.data.api.v6.DashboardApiServiceV6
import eu.wedgess.piholecontrol.data.mappers.toEntity
import eu.wedgess.piholecontrol.data.mappers.toSummaryEntity
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleClientsOverTimeResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleOverTimeResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleSummaryResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleClientsOverTimeResponseDataV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleOverTimeResponseDataV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleSummaryResponseDataV6
import eu.wedgess.piholecontrol.domain.model.ClientOverTimeEntity
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.OverTimeEntity
import eu.wedgess.piholecontrol.domain.model.QueriesOverTimeEntity
import eu.wedgess.piholecontrol.domain.model.SummaryEntity
import eu.wedgess.piholecontrol.domain.repository.DashboardRepository
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import kotlinx.coroutines.withContext

class DashboardRepositoryImpl(
    private val apiV5: DashboardApiServiceV5,
    private val apiV6: DashboardApiServiceV6,
    private val dispatcherProvider: DispatcherProvider
) : DashboardRepository {

    override suspend fun fetchStatusSummary(
        activeMiHole: ConnectionEntity
    ): Result<SummaryEntity> = withContext(dispatcherProvider.io) {
        callVersionedEndpoint(
            activeConnection = activeMiHole,
            v5Call = { apiV5.fetchStatusSummary(it) },
            v6Call = { apiV6.fetchStatusSummary(it) },
            mapper = {
                when (it) {
                    is PiHoleSummaryResponseDataV5 -> it.toSummaryEntity()
                    is PiHoleSummaryResponseDataV6 -> it.toSummaryEntity()
                    else -> throw IllegalArgumentException("Unknown type: ${it.javaClass.name}")
                }
            }
        )
    }

    override suspend fun fetchOverTimeData10Minutes(
        activeMiHole: ConnectionEntity
    ): Result<QueriesOverTimeEntity> = withContext(dispatcherProvider.io) {
        callVersionedEndpoint(
            activeConnection = activeMiHole,
            v5Call = { apiV5.fetchOverTimeData10Minutes(it) },
            v6Call = { apiV6.fetchOverTimeData10Minutes(it) },
            mapper = {
                when (it) {
                    is PiHoleOverTimeResponseDataV5 -> it.toEntity()
                    is PiHoleOverTimeResponseDataV6 -> it.toEntity()
                    else -> throw IllegalArgumentException("Unknown type: ${it.javaClass.name}")
                }
            }
        )
    }

    override suspend fun fetchOverTimeDataClients(
        activeMiHole: ConnectionEntity
    ): Result<List<ClientOverTimeEntity>> = withContext(dispatcherProvider.io) {
        callVersionedEndpoint(
            activeConnection = activeMiHole,
            v5Call = {
                apiV5.fetchOverTimeDataClients(it)
            },
            v6Call = {
                apiV6.fetchOverTimeDataClients(it)
            },
            mapper = {
                when (it) {
                    is PiHoleClientsOverTimeResponseDataV5 -> mapV5ClientsOverTime(it)
                    is PiHoleClientsOverTimeResponseDataV6 -> mapV6ClientsOverTime(it)
                    else -> throw IllegalArgumentException("Unknown type: ${it.javaClass.name}")
                }
            }
        )
    }

    private fun mapV5ClientsOverTime(response: PiHoleClientsOverTimeResponseDataV5): List<ClientOverTimeEntity> {
        return response.clients
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

    private fun mapV6ClientsOverTime(response: PiHoleClientsOverTimeResponseDataV6): List<ClientOverTimeEntity> {
        return response.clients.map { (ip, clientInfo) ->
            ClientOverTimeEntity(
                clientName = clientInfo.name ?: "",
                clientIp = ip,
                clientActivity = response.history.map { history ->
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
