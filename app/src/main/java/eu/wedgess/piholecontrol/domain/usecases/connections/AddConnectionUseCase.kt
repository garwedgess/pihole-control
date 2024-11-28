package eu.wedgess.piholecontrol.domain.usecases.connections

import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository

class AddConnectionUseCase(
    private val repository: ConnectionRepository
) {
    suspend operator fun invoke(connectionInfo: ConnectionInfo): Result<Unit> {
        return repository.insert(connectionInfo)
    }
}