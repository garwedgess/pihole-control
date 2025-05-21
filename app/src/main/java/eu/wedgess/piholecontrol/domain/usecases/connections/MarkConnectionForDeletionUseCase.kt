package eu.wedgess.piholecontrol.domain.usecases.connections

import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import java.util.UUID
import javax.inject.Inject

class MarkConnectionForDeletionUseCase @Inject constructor(
    private val repository: ConnectionRepository
) {
    suspend operator fun invoke(id: UUID): Result<Unit> {
        return repository.markForDeletion(id)
    }
}
