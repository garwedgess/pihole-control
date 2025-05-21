package eu.wedgess.piholecontrol.domain.usecases.connections

import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import java.util.UUID
import javax.inject.Inject

class SetConnectionAsActiveUseCase @Inject constructor(
    private val repository: ConnectionRepository
) {
    suspend operator fun invoke(connectionId: UUID): Result<Unit> {
        return repository.setActiveById(connectionId)
    }
}
