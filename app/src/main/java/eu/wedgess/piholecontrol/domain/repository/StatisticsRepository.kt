package eu.wedgess.piholecontrol.domain.repository

import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.ForwardDestinationEntity
import eu.wedgess.piholecontrol.domain.model.QueryTypeEntity
import eu.wedgess.piholecontrol.domain.model.TopClientQueriesEntity
import eu.wedgess.piholecontrol.domain.model.TopQueriesEntity

interface StatisticsRepository {
    suspend fun fetchQueryTypes(
        activeConnection: ConnectionEntity
    ): PiHoleApiResult<List<QueryTypeEntity>>

    suspend fun fetchForwardDestinations(
        activeConnection: ConnectionEntity
    ): PiHoleApiResult<List<ForwardDestinationEntity>>

    suspend fun fetchTopQueries(
        activeConnection: ConnectionEntity
    ): PiHoleApiResult<TopQueriesEntity>

    suspend fun fetchTopClients(
        activeConnection: ConnectionEntity
    ): PiHoleApiResult<TopClientQueriesEntity>
}
