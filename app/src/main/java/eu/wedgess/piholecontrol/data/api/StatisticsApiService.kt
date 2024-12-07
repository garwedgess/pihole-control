package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.data.model.responses.PiHoleForwardDestinations
import eu.wedgess.piholecontrol.data.model.responses.PiHoleQueryTypes
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopClients
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopQueries

interface StatisticsApiService {
    suspend fun fetchQueryTypes(activeMiHole: ConnectionEntity): Result<PiHoleQueryTypes>
    suspend fun fetchForwardDestinations(activeMiHole: ConnectionEntity): Result<PiHoleForwardDestinations>
    suspend fun fetchTopQueries(activeMiHole: ConnectionEntity): Result<PiHoleTopQueries>
    suspend fun fetchTopClients(activeMiHole: ConnectionEntity): Result<PiHoleTopClients>
}