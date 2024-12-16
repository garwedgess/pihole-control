package eu.wedgess.piholecontrol.domain.usecases.connections

import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository

class DeleteConnectionUseCase(
    private val repository: ConnectionRepository
) {
    suspend operator fun invoke(connectionId: Long): Result<Unit> {
        return repository.deleteById(connectionId)
    }
}
