package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.PiHoleAuthSessionStatusResponseData
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

interface AuthApiService {
    suspend fun validateSessionId(
        connection: ConnectionEntity
    ): PiHoleApiResult<PiHoleAuthSessionStatusResponseData>
    suspend fun generateSessionId(
        connection: ConnectionEntity
    ): PiHoleApiResult<PiHoleAuthSessionStatusResponseData>
}
