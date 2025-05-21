package eu.wedgess.piholecontrol.domain.usecases.connections

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import javax.inject.Inject

class UpdateConnectionUseCase @Inject constructor(
    private val repository: ConnectionRepository
) {
    suspend operator fun invoke(connectionInfo: ConnectionEntity): Result<Unit> {
        return repository.update(connectionInfo)
    }
}
