package eu.wedgess.piholecontrol.data.api.v5

import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleStatusResponse
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import kotlin.time.Duration

interface StatusApiService {

    suspend fun fetchStatus(activeMiHole: ConnectionEntity): Result<PiHoleStatusResponse>
    suspend fun enableAdBlocking(activeMiHole: ConnectionEntity): Result<PiHoleStatusResponse>
    suspend fun disableAdBlocking(
        activeMiHole: ConnectionEntity,
        duration: Duration
    ): Result<PiHoleStatusResponse>
}
