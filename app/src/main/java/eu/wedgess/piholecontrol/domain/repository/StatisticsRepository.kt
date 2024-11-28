package eu.wedgess.piholecontrol.domain.repository

import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.domain.model.ForwardDestinationEntity
import eu.wedgess.piholecontrol.domain.model.QueryTypeEntity
import eu.wedgess.piholecontrol.domain.model.TopClientEntity
import eu.wedgess.piholecontrol.domain.model.TopQueriesEntity

interface StatisticsRepository {
    suspend fun fetchQueryTypes(activeConnection: ConnectionInfo): Result<List<QueryTypeEntity>>
    suspend fun fetchForwardDestinations(activeConnection: ConnectionInfo): Result<List<ForwardDestinationEntity>>
    suspend fun fetchTopQueries(activeConnection: ConnectionInfo): Result<TopQueriesEntity>
    suspend fun fetchTopClients(activeConnection: ConnectionInfo): Result<List<TopClientEntity>>
}