package eu.wedgess.piholecontrol.domain.usecases.connections

import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository

class UpdateConnectionUseCase(
    private val repository: ConnectionRepository
) {
    suspend operator fun invoke(connectionInfo: ConnectionInfo): Result<Unit> {
        return repository.update(connectionInfo)
    }
}