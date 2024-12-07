package eu.wedgess.piholecontrol.domain.repository

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.ForwardDestinationEntity
import eu.wedgess.piholecontrol.domain.model.QueryTypeEntity
import eu.wedgess.piholecontrol.domain.model.TopClientEntity
import eu.wedgess.piholecontrol.domain.model.TopQueriesEntity

interface StatisticsRepository {
    suspend fun fetchQueryTypes(activeConnection: ConnectionEntity): Result<List<QueryTypeEntity>>
    suspend fun fetchForwardDestinations(activeConnection: ConnectionEntity): Result<List<ForwardDestinationEntity>>
    suspend fun fetchTopQueries(activeConnection: ConnectionEntity): Result<TopQueriesEntity>
    suspend fun fetchTopClients(activeConnection: ConnectionEntity): Result<List<TopClientEntity>>
}