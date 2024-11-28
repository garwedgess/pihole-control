package eu.wedgess.piholecontrol.domain.usecases.connections

import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository

class FetchConnectionByIdUseCase(
    private val repository: ConnectionRepository
) {
    suspend operator fun invoke(connectionId: Long): Result<ConnectionInfo> {
        return repository.fetchById(connectionId)
    }
}