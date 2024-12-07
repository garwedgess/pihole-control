package eu.wedgess.piholecontrol.domain.usecases.connections

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository

class AddConnectionUseCase(
    private val repository: ConnectionRepository
) {
    suspend operator fun invoke(connectionInfo: ConnectionEntity): Result<Unit> {
        return repository.insert(connectionInfo)
    }
}