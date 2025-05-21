package eu.wedgess.piholecontrol.domain.usecases.connections

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import java.util.UUID
import javax.inject.Inject

class FetchConnectionByIdUseCase @Inject constructor(
    private val repository: ConnectionRepository
) {
    suspend operator fun invoke(connectionId: UUID): Result<ConnectionEntity> {
        return repository.fetchById(connectionId)
    }
}
