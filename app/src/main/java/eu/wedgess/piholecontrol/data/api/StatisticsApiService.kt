package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.data.model.responses.PiHoleForwardDestinations
import eu.wedgess.piholecontrol.data.model.responses.PiHoleQueryTypes
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopClients
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopQueries

interface StatisticsApiService {
    suspend fun fetchQueryTypes(activeMiHole: ConnectionInfo): Result<PiHoleQueryTypes>
    suspend fun fetchForwardDestinations(activeMiHole: ConnectionInfo): Result<PiHoleForwardDestinations>
    suspend fun fetchTopQueries(activeMiHole: ConnectionInfo): Result<PiHoleTopQueries>
    suspend fun fetchTopClients(activeMiHole: ConnectionInfo): Result<PiHoleTopClients>
}