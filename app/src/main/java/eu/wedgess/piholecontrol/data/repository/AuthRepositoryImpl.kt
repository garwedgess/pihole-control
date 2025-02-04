package eu.wedgess.piholecontrol.data.repository

import eu.wedgess.piholecontrol.data.api.v6.AuthApiServiceV6
import eu.wedgess.piholecontrol.data.mappers.toEntity
import eu.wedgess.piholecontrol.domain.model.AuthSessionStatusEntity
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.AuthRepository
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface TokenRefresher {
    suspend fun generateSessionId(connection: ConnectionEntity.Version6): Result<AuthSessionStatusEntity>
}

class AuthRepositoryImpl @Inject constructor(
    val api: AuthApiServiceV6,
    val dispatcherProvider: DispatcherProvider
) : AuthRepository, TokenRefresher {

    override suspend fun validateSessionId(
        connection: ConnectionEntity.Version6
    ): Result<AuthSessionStatusEntity> =
        withContext(dispatcherProvider.io) {
            api.validateSessionId(connection).map { it.toEntity() }
        }

    override suspend fun generateSessionId(
        connection: ConnectionEntity.Version6
    ): Result<AuthSessionStatusEntity> = withContext(dispatcherProvider.io) {
        api.generateSessionId(connection).map { it.toEntity() }
    }
}
