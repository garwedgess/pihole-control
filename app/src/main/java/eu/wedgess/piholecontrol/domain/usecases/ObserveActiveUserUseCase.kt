package eu.wedgess.piholecontrol.domain.usecases;

import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository;
import kotlinx.coroutines.flow.Flow

class ObserveActiveUserUseCase(private val repository: ConnectionRepository) {
    operator fun invoke(): Flow<Result<ConnectionInfo>> = repository.fetchActiveFlow()
}