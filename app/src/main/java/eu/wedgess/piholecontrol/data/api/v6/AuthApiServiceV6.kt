package eu.wedgess.piholecontrol.data.api.v6

import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleAuthSessionStatusResponseDataV6
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

interface AuthApiServiceV6 {
    suspend fun validateSessionId(
        connection: ConnectionEntity.Version6
    ): PiHoleApiResult<PiHoleAuthSessionStatusResponseDataV6>
    suspend fun generateSessionId(
        connection: ConnectionEntity.Version6
    ): PiHoleApiResult<PiHoleAuthSessionStatusResponseDataV6>
}
