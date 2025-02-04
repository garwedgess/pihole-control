package eu.wedgess.piholecontrol.domain.usecases.connections

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import java.util.UUID

class FetchConnectionByIdUseCase(
    private val repository: ConnectionRepository
) {
    suspend operator fun invoke(connectionId: UUID): Result<ConnectionEntity> {
        return repository.fetchById(connectionId)
    }
}
