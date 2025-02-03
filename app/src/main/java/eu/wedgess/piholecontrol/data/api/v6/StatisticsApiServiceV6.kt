package eu.wedgess.piholecontrol.data.api.v6

import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleQueryTypesResponseDataV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleTopClientsCombinedResponseDataV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleTopQueriesCombinedResponseV6Data
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleUpstreamsResponseDataV6
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

interface StatisticsApiServiceV6 {
    suspend fun fetchQueryTypes(
        connection: ConnectionEntity.Version6
    ): PiHoleApiResult<PiHoleQueryTypesResponseDataV6>

    suspend fun fetchUpstreams(
        connection: ConnectionEntity.Version6
    ): PiHoleApiResult<PiHoleUpstreamsResponseDataV6>

    suspend fun fetchTopCombinedQueries(
        connection: ConnectionEntity.Version6
    ): PiHoleApiResult<PiHoleTopQueriesCombinedResponseV6Data>

    suspend fun fetchTopCombinedClients(
        connection: ConnectionEntity.Version6
    ): PiHoleApiResult<PiHoleTopClientsCombinedResponseDataV6>
}
