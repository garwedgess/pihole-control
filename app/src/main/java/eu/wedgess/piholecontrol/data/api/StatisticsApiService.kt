package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.PiHoleQueryTypesResponseData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopClientsCombinedResponseData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopQueriesCombinedResponseData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleUpstreamsResponseData
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

interface StatisticsApiService {
    suspend fun fetchQueryTypes(
        connection: ConnectionEntity
    ): PiHoleApiResult<PiHoleQueryTypesResponseData>

    suspend fun fetchUpstreams(
        connection: ConnectionEntity
    ): PiHoleApiResult<PiHoleUpstreamsResponseData>

    suspend fun fetchTopCombinedQueries(
        connection: ConnectionEntity
    ): PiHoleApiResult<PiHoleTopQueriesCombinedResponseData>

    suspend fun fetchTopCombinedClients(
        connection: ConnectionEntity
    ): PiHoleApiResult<PiHoleTopClientsCombinedResponseData>
}
