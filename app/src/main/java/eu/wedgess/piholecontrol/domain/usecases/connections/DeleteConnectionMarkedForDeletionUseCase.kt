package eu.wedgess.piholecontrol.domain.usecases.connections

import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository

class DeleteConnectionMarkedForDeletionUseCase(
    private val repository: ConnectionRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.deleteAllMarkedForDeletion()
    }
}
