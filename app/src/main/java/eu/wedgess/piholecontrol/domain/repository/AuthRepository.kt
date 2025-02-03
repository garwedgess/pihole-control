package eu.wedgess.piholecontrol.domain.repository

import eu.wedgess.piholecontrol.domain.model.AuthSessionStatusEntity
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

interface AuthRepository {
    suspend fun validateSessionId(connection: ConnectionEntity.Version6): Result<AuthSessionStatusEntity>
    suspend fun generateSessionId(connection: ConnectionEntity.Version6): Result<AuthSessionStatusEntity>
}
