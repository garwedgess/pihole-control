package eu.wedgess.piholecontrol.domain.repository

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.StatusEntity
import kotlin.time.Duration

interface StatusRepository {
    suspend fun fetchStatus(activeConnection: ConnectionEntity): Result<StatusEntity>
    suspend fun enableAdBlocking(activeConnection: ConnectionEntity): Result<StatusEntity>
    suspend fun disableAdBlocking(
        activeConnection: ConnectionEntity,
        duration: Duration
    ): Result<StatusEntity>
}
