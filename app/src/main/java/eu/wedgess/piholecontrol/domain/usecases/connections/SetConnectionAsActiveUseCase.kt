package eu.wedgess.piholecontrol.domain.usecases.connections

import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import java.util.UUID

class SetConnectionAsActiveUseCase(
    private val repository: ConnectionRepository
) {
    suspend operator fun invoke(connectionId: UUID): Result<Unit> {
        return repository.setActiveById(connectionId)
    }
}
