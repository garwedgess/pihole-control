package eu.wedgess.piholecontrol.data.api.v6

import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleStatusResponseDataV6
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import kotlin.time.Duration

interface StatusApiServiceV6 {

    suspend fun fetchStatus(activeMiHole: ConnectionEntity.Version6): Result<PiHoleStatusResponseDataV6>
    suspend fun enableAdBlocking(activeMiHole: ConnectionEntity.Version6): Result<PiHoleStatusResponseDataV6>
    suspend fun disableAdBlocking(
        activeMiHole: ConnectionEntity.Version6,
        duration: Duration
    ): Result<PiHoleStatusResponseDataV6>
}
