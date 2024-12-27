package eu.wedgess.piholecontrol.domain.usecases.connections

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository

class FetchConnectionByIdUseCase(
    private val repository: ConnectionRepository
) {
    suspend operator fun invoke(connectionId: Long): Result<ConnectionEntity> {
        return repository.fetchById(connectionId)
    }
}
