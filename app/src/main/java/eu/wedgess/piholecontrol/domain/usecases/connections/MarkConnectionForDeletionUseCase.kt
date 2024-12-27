package eu.wedgess.piholecontrol.domain.usecases.connections

import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository

class MarkConnectionForDeletionUseCase(
    private val repository: ConnectionRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> {
        return repository.markForDeletion(id)
    }
}
