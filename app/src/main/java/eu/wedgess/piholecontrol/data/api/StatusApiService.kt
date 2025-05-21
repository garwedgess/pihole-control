package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.model.responses.PiHoleStatusResponseData
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import kotlin.time.Duration

interface StatusApiService {

    suspend fun fetchStatus(activeMiHole: ConnectionEntity): Result<PiHoleStatusResponseData>
    suspend fun enableAdBlocking(activeMiHole: ConnectionEntity): Result<PiHoleStatusResponseData>
    suspend fun disableAdBlocking(
        activeMiHole: ConnectionEntity,
        duration: Duration
    ): Result<PiHoleStatusResponseData>
}
