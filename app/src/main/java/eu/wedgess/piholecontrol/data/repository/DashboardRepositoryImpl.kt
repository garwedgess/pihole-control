package eu.wedgess.piholecontrol.data.repository

import eu.wedgess.piholecontrol.data.api.v5.DashboardApiServiceV5
import eu.wedgess.piholecontrol.data.api.v6.DashboardApiServiceV6
import eu.wedgess.piholecontrol.data.mappers.toEntity
import eu.wedgess.piholecontrol.data.mappers.toSummaryEntity
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleClientsOverTimeV5Data
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleOverTimeV5Data
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleSummaryV5Data
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleClientsOverTimeV6Data
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleOverTimeV6Data
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleSummaryV6Data
import eu.wedgess.piholecontrol.domain.model.ClientOverTimeEntity
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.OverTimeEntity
import eu.wedgess.piholecontrol.domain.model.PiHoleApiVersionEntity
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
            apiVersion = activeMiHole.apiVersion,
            v5Call = { apiV5.fetchStatusSummary(activeMiHole) },
            v6Call = { apiV6.fetchStatusSummary(activeMiHole) },
            mapper = {
                when (it) {
                    is PiHoleSummaryV5Data -> it.toSummaryEntity()
                    is PiHoleSummaryV6Data -> it.toSummaryEntity()
                    else -> throw IllegalArgumentException("Unknown type: ${it.javaClass.name}")
                }
            }
        )
    }

    override suspend fun fetchOverTimeData10Minutes(
        activeMiHole: ConnectionEntity
    ): Result<QueriesOverTimeEntity> = withContext(dispatcherProvider.io) {
        callVersionedEndpoint(
            apiVersion = activeMiHole.apiVersion,
            v5Call = { apiV5.fetchOverTimeData10Minutes(activeMiHole) },
            v6Call = { apiV6.fetchOverTimeData10Minutes(activeMiHole) },
            mapper = {
                when (it) {
                    is PiHoleOverTimeV5Data -> it.toEntity()
                    is PiHoleOverTimeV6Data -> it.toEntity()
                    else -> throw IllegalArgumentException("Unknown type: ${it.javaClass.name}")
                }
            }
        )
    }

    override suspend fun fetchOverTimeDataClients(
        activeMiHole: ConnectionEntity
    ): Result<List<ClientOverTimeEntity>> = withContext(dispatcherProvider.io) {
        callVersionedEndpoint(
            apiVersion = activeMiHole.apiVersion,
            v5Call = {
                apiV5.fetchOverTimeDataClients(activeMiHole)
            },
            v6Call = {
                apiV6.fetchOverTimeDataClients(activeMiHole)
                     },
            mapper = {
                when (it) {
                    is PiHoleClientsOverTimeV5Data -> mapV5ClientsOverTime(it)
                    is PiHoleClientsOverTimeV6Data -> mapV6ClientsOverTime(it)
                    else -> throw IllegalArgumentException("Unknown type: ${it.javaClass.name}")
                }
            }
        )
    }

    private fun mapV5ClientsOverTime(response: PiHoleClientsOverTimeV5Data): List<ClientOverTimeEntity> {
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

    private fun mapV6ClientsOverTime(response: PiHoleClientsOverTimeV6Data): List<ClientOverTimeEntity> {
        return response.clients.map { (ip, clientInfo) ->
            ClientOverTimeEntity(
                clientName = clientInfo.name,
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

inline suspend fun <T, R> callVersionedEndpoint(
    apiVersion: PiHoleApiVersionEntity,
    crossinline v5Call: suspend () -> Result<T>,
    crossinline v6Call: suspend () -> Result<T>,
    crossinline mapper: (T) -> R
): Result<R> {
    return when (apiVersion) {
        PiHoleApiVersionEntity.Version5 -> v5Call()
        PiHoleApiVersionEntity.Version6 -> v6Call()
    }.mapCatching {
        mapper(it)
    }
}
