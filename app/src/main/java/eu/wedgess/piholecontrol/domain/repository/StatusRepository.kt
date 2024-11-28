package eu.wedgess.piholecontrol.domain.repository

import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.domain.model.StatusEntity
import kotlin.time.Duration

interface StatusRepository {
    suspend fun fetchStatus(activeConnection: ConnectionInfo): Result<StatusEntity>
    suspend fun enableAdBlocking(activeConnection: ConnectionInfo): Result<StatusEntity>
    suspend fun disableAdBlocking(
        activeConnection: ConnectionInfo,
        duration: Duration
    ): Result<StatusEntity>
}