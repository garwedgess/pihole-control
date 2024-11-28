package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.data.model.responses.PiHoleStatusResponse
import kotlin.time.Duration

interface StatusApiService {

    suspend fun fetchStatus(activeMiHole: ConnectionInfo): Result<PiHoleStatusResponse>
    suspend fun enableAdBlocking(activeMiHole: ConnectionInfo): Result<PiHoleStatusResponse>
    suspend fun disableAdBlocking(
        activeMiHole: ConnectionInfo,
        duration: Duration
    ): Result<PiHoleStatusResponse>

}