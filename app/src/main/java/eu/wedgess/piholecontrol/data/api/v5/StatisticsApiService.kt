package eu.wedgess.piholecontrol.data.api.v5

import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleForwardDestinations
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleQueryTypes
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleTopClients
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleTopQueries
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

interface StatisticsApiService {
    suspend fun fetchQueryTypes(activeMiHole: ConnectionEntity): Result<PiHoleQueryTypes>
    suspend fun fetchForwardDestinations(activeMiHole: ConnectionEntity): Result<PiHoleForwardDestinations>
    suspend fun fetchTopQueries(activeMiHole: ConnectionEntity): Result<PiHoleTopQueries>
    suspend fun fetchTopClients(activeMiHole: ConnectionEntity): Result<PiHoleTopClients>
}
