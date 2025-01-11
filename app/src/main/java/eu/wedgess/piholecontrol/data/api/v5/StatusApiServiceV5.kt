package eu.wedgess.piholecontrol.data.api.v5

import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleStatusResponseDataV5
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import kotlin.time.Duration

interface StatusApiServiceV5 {

    suspend fun fetchStatus(
        connection: ConnectionEntity.Version5
    ): PiHoleApiResult<PiHoleStatusResponseDataV5>

    suspend fun enableAdBlocking(
        connection: ConnectionEntity.Version5
    ): PiHoleApiResult<PiHoleStatusResponseDataV5>

    suspend fun disableAdBlocking(
        connection: ConnectionEntity.Version5,
        duration: Duration
    ): PiHoleApiResult<PiHoleStatusResponseDataV5>
}
