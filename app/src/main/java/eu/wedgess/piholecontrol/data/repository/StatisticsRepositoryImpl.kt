package eu.wedgess.piholecontrol.data.repository

import eu.wedgess.piholecontrol.data.api.v5.StatisticsApiService
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.ForwardDestinationEntity
import eu.wedgess.piholecontrol.domain.model.QueryTypeEntity
import eu.wedgess.piholecontrol.domain.model.TopClientEntity
import eu.wedgess.piholecontrol.domain.model.TopDomainEntity
import eu.wedgess.piholecontrol.domain.model.TopQueriesEntity
import eu.wedgess.piholecontrol.domain.repository.StatisticsRepository
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import kotlinx.coroutines.withContext

class StatisticsRepositoryImpl(
    private val api: StatisticsApiService,
    private val dispatcherProvider: DispatcherProvider
) : StatisticsRepository {

    override suspend fun fetchQueryTypes(
        activeConnection: ConnectionEntity
    ): Result<List<QueryTypeEntity>> =
        withContext(dispatcherProvider.io) {
            api.fetchQueryTypes(activeConnection).mapCatching { queryTypes ->
                queryTypes.queryTypes.asList().map { (key, value) ->
                    QueryTypeEntity(key, value)
                }
            }
        }

    override suspend fun fetchForwardDestinations(
        activeConnection: ConnectionEntity
    ): Result<List<ForwardDestinationEntity>> =
        withContext(dispatcherProvider.io) {
            api.fetchForwardDestinations(activeConnection).mapCatching { destinations ->
                destinations.forwardDestinations.map { (key, value) ->
                    ForwardDestinationEntity(key, value)
                }
            }
        }

    override suspend fun fetchTopQueries(activeConnection: ConnectionEntity): Result<TopQueriesEntity> =
        withContext(dispatcherProvider.io) {
            api.fetchTopQueries(activeConnection).mapCatching { top ->
                val topAllowed = top.topQueries.map { TopDomainEntity(it.key, it.value) }
                val topBlocked = top.topAds.map { TopDomainEntity(it.key, it.value) }
                TopQueriesEntity(allowed = topAllowed, blocked = topBlocked)
            }
        }

    override suspend fun fetchTopClients(
        activeConnection: ConnectionEntity
    ): Result<List<TopClientEntity>> =
        withContext(dispatcherProvider.io) {
            api.fetchTopClients(activeConnection).mapCatching { clients ->
                clients.topSources.map { (client, hits) ->
                    TopClientEntity(client, hits)
                }
            }
        }
}
