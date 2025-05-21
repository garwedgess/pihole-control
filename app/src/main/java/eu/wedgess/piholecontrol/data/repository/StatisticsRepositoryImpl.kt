package eu.wedgess.piholecontrol.data.repository

import eu.wedgess.piholecontrol.data.api.StatisticsApiService
import eu.wedgess.piholecontrol.data.mappers.toEntity
import eu.wedgess.piholecontrol.data.mappers.toEntityList
import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.QueryTypeEntity
import eu.wedgess.piholecontrol.domain.model.TopClientQueriesEntity
import eu.wedgess.piholecontrol.domain.model.TopQueriesEntity
import eu.wedgess.piholecontrol.domain.model.UpstreamDestinationEntity
import eu.wedgess.piholecontrol.domain.repository.StatisticsRepository
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import kotlinx.coroutines.withContext

class StatisticsRepositoryImpl(
    private val statisticsApiService: StatisticsApiService,
    private val dispatcherProvider: DispatcherProvider
) : StatisticsRepository {

    override suspend fun fetchQueryTypes(
        activeConnection: ConnectionEntity
    ): PiHoleApiResult<List<QueryTypeEntity>> =
        withContext(dispatcherProvider.io) {
            statisticsApiService.fetchQueryTypes(activeConnection)
                .mapCatching { it.toEntityList() }
        }

    override suspend fun fetchUpstreamDestinations(
        activeConnection: ConnectionEntity
    ): PiHoleApiResult<List<UpstreamDestinationEntity>> =
        withContext(dispatcherProvider.io) {
            statisticsApiService.fetchUpstreams(activeConnection).mapCatching {
                it.combinedUpstreamPercentages.toEntityList()
            }
        }

    override suspend fun fetchTopQueries(
        activeConnection: ConnectionEntity
    ): PiHoleApiResult<TopQueriesEntity> =
        withContext(dispatcherProvider.io) {
            statisticsApiService.fetchTopCombinedQueries(activeConnection).mapCatching {
                it.toEntity()
            }
        }

    override suspend fun fetchTopClients(
        activeConnection: ConnectionEntity
    ): PiHoleApiResult<TopClientQueriesEntity> =
        withContext(dispatcherProvider.io) {
            statisticsApiService.fetchTopCombinedClients(activeConnection).mapCatching {
                it.toEntity()
            }
        }
}
