package eu.wedgess.piholecontrol.domain.usecases.auth

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.AuthRepository
import javax.inject.Inject

class ValidateSessionIdUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(connectionEntity: ConnectionEntity) =
        repository.validateSessionId(connectionEntity)
}
