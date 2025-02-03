package eu.wedgess.piholecontrol.data.api.v5

import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleQueryTypesResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleTopClientsResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleTopQueriesResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleUpstreamsResponseDataV5
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

interface StatisticsApiServiceV5 {
    suspend fun fetchQueryTypes(
        connection: ConnectionEntity.Version5
    ): PiHoleApiResult<PiHoleQueryTypesResponseDataV5>

    suspend fun fetchUpstreams(
        connection: ConnectionEntity.Version5
    ): PiHoleApiResult<PiHoleUpstreamsResponseDataV5>

    suspend fun fetchTopQueries(
        connection: ConnectionEntity.Version5
    ): PiHoleApiResult<PiHoleTopQueriesResponseDataV5>

    suspend fun fetchTopClients(
        connection: ConnectionEntity.Version5
    ): PiHoleApiResult<PiHoleTopClientsResponseDataV5>
}
