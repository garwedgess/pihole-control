package eu.wedgess.piholecontrol.domain.usecases.connections

import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository

class SetConnectionAsActiveUseCase(
    private val repository: ConnectionRepository
) {
    suspend operator fun invoke(connectionId: Long): Result<Unit> {
        return repository.setActiveById(connectionId)
    }
}