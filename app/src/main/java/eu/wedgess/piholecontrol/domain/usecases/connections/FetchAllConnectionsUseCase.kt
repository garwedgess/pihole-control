package eu.wedgess.piholecontrol.domain.usecases.connections

import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import kotlinx.coroutines.flow.Flow

class FetchAllConnectionsUseCase(
    private val repository: ConnectionRepository
) {
    operator fun invoke(): Flow<Result<List<ConnectionInfo>>> =repository.fetchAll()
}