package eu.wedgess.piholecontrol.domain.repository

import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.QueryTypeEntity
import eu.wedgess.piholecontrol.domain.model.TopClientQueriesEntity
import eu.wedgess.piholecontrol.domain.model.TopQueriesEntity
import eu.wedgess.piholecontrol.domain.model.UpstreamDestinationEntity

interface StatisticsRepository {
    suspend fun fetchQueryTypes(
        activeConnection: ConnectionEntity
    ): PiHoleApiResult<List<QueryTypeEntity>>

    suspend fun fetchForwardDestinations(
        activeConnection: ConnectionEntity
    ): PiHoleApiResult<List<UpstreamDestinationEntity>>

    suspend fun fetchTopQueries(
        activeConnection: ConnectionEntity
    ): PiHoleApiResult<TopQueriesEntity>

    suspend fun fetchTopClients(
        activeConnection: ConnectionEntity
    ): PiHoleApiResult<TopClientQueriesEntity>
}
