package eu.wedgess.piholecontrol.data.repository

import eu.wedgess.piholecontrol.data.api.v5.StatisticsApiServiceV5
import eu.wedgess.piholecontrol.data.api.v6.StatisticsApiServiceV6
import eu.wedgess.piholecontrol.data.mappers.toEntity
import eu.wedgess.piholecontrol.data.mappers.toEntityList
import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleQueryTypesResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleTopClientsResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleTopQueriesResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleUpstreamsResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleQueryTypesResponseDataV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleTopClientsCombinedResponseDataV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleTopQueriesCombinedResponseV6Data
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleUpstreamsResponseDataV6
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.QueryTypeEntity
import eu.wedgess.piholecontrol.domain.model.TopClientQueriesEntity
import eu.wedgess.piholecontrol.domain.model.TopQueriesEntity
import eu.wedgess.piholecontrol.domain.model.UpstreamDestinationEntity
import eu.wedgess.piholecontrol.domain.repository.StatisticsRepository
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import kotlinx.coroutines.withContext

class StatisticsRepositoryImpl(
    private val apiV5: StatisticsApiServiceV5,
    private val apiV6: StatisticsApiServiceV6,
    private val dispatcherProvider: DispatcherProvider
) : StatisticsRepository {

    override suspend fun fetchQueryTypes(
        activeConnection: ConnectionEntity
    ): PiHoleApiResult<List<QueryTypeEntity>> =
        withContext(dispatcherProvider.io) {
            callVersionedEndpoint(
                activeConnection = activeConnection,
                v5Call = { apiV5.fetchQueryTypes(it) },
                v6Call = { apiV6.fetchQueryTypes(it) },
                mapper = {
                    when (it) {
                        is PiHoleQueryTypesResponseDataV5 -> it.toEntityList()
                        is PiHoleQueryTypesResponseDataV6 -> it.toEntityList()
                        else -> throw IllegalArgumentException("Unknown type: ${it.javaClass.name}")
                    }
                }
            )
        }

    override suspend fun fetchUpstreamDestinations(
        activeConnection: ConnectionEntity
    ): PiHoleApiResult<List<UpstreamDestinationEntity>> =
        withContext(dispatcherProvider.io) {
            callVersionedEndpoint(
                activeConnection = activeConnection,
                v5Call = { apiV5.fetchUpstreams(it) },
                v6Call = { apiV6.fetchUpstreams(it) },
                mapper = {
                    when (it) {
                        is PiHoleUpstreamsResponseDataV5 -> {
                            it.upstreamDestinations.toEntityList()
                        }

                        is PiHoleUpstreamsResponseDataV6 -> {
                            it.combinedUpstreamPercentages.toEntityList()
                        }

                        else -> throw IllegalArgumentException("Unknown type: ${it.javaClass.name}")
                    }
                }
            )
        }

    override suspend fun fetchTopQueries(
        activeConnection: ConnectionEntity
    ): PiHoleApiResult<TopQueriesEntity> =
        withContext(dispatcherProvider.io) {
            callVersionedEndpoint(
                activeConnection = activeConnection,
                v5Call = { apiV5.fetchTopQueries(it) },
                v6Call = { apiV6.fetchTopCombinedQueries(it) },
                mapper = {
                    when (it) {
                        is PiHoleTopQueriesResponseDataV5 -> it.toEntity()
                        is PiHoleTopQueriesCombinedResponseV6Data -> it.toEntity()
                        else -> throw IllegalArgumentException("Unknown type: ${it.javaClass.name}")
                    }
                }
            )
        }

    override suspend fun fetchTopClients(
        activeConnection: ConnectionEntity
    ): PiHoleApiResult<TopClientQueriesEntity> =
        withContext(dispatcherProvider.io) {
            callVersionedEndpoint(
                activeConnection = activeConnection,
                v5Call = { apiV5.fetchTopClients(it) },
                v6Call = { apiV6.fetchTopCombinedClients(it) },
                mapper = {
                    when (it) {
                        is PiHoleTopClientsResponseDataV5 -> it.toEntity()
                        is PiHoleTopClientsCombinedResponseDataV6 -> it.toEntity()
                        else -> throw IllegalArgumentException("Unknown type: ${it.javaClass.name}")
                    }
                }
            )
        }
}
