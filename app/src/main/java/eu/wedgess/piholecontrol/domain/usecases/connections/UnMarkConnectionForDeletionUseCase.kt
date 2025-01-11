package eu.wedgess.piholecontrol.domain.usecases.connections

import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import java.util.UUID

class UnMarkConnectionForDeletionUseCase(
    private val repository: ConnectionRepository
) {
    suspend operator fun invoke(id: UUID): Result<Unit> {
        return repository.unmarkForDeletion(id)
    }
}
