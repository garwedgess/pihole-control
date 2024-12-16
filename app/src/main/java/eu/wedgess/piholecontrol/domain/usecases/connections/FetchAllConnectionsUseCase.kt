package eu.wedgess.piholecontrol.domain.usecases.connections

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import kotlinx.coroutines.flow.Flow

class FetchAllConnectionsUseCase(
    private val repository: ConnectionRepository
) {
    operator fun invoke(): Flow<Result<List<ConnectionEntity>>> = repository.fetchAll()
}
