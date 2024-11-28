package eu.wedgess.piholecontrol.data

import eu.wedgess.piholecontrol.data.api.StatisticsApiService
import eu.wedgess.piholecontrol.data.model.ConnectionInfo
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
        activeConnection: ConnectionInfo
    ): Result<List<QueryTypeEntity>> =
        withContext(dispatcherProvider.io) {
            api.fetchQueryTypes(activeConnection).mapCatching { queryTypes ->
                queryTypes.queryTypes.asList().map { (key, value) ->
                    QueryTypeEntity(key, value)
                }
            }
        }

    override suspend fun fetchForwardDestinations(
        activeConnection: ConnectionInfo
    ): Result<List<ForwardDestinationEntity>> =
        withContext(dispatcherProvider.io) {
            api.fetchForwardDestinations(activeConnection).mapCatching { destinations ->
                destinations.forwardDestinations.map { (key, value) ->
                    ForwardDestinationEntity(key, value)
                }
            }
        }

    override suspend fun fetchTopQueries(activeConnection: ConnectionInfo): Result<TopQueriesEntity> =
        withContext(dispatcherProvider.io) {
            api.fetchTopQueries(activeConnection).mapCatching { top ->
                val topAllowed = top.topQueries.map { TopDomainEntity(it.key, it.value) }
                val topBlocked = top.topAds.map { TopDomainEntity(it.key, it.value) }
                TopQueriesEntity(allowed = topAllowed, blocked = topBlocked)
            }
        }

    override suspend fun fetchTopClients(
        activeConnection: ConnectionInfo
    ): Result<List<TopClientEntity>> =
        withContext(dispatcherProvider.io) {
            api.fetchTopClients(activeConnection).mapCatching { clients ->
                clients.topSources.map { (client, hits) ->
                    TopClientEntity(client, hits)
                }
            }
        }

}