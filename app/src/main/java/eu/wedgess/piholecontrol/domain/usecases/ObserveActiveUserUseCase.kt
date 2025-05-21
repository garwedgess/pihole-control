package eu.wedgess.piholecontrol.domain.usecases

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveActiveUserUseCase @Inject constructor(private val repository: ConnectionRepository) {
    operator fun invoke(): Flow<Result<ConnectionEntity>> = repository.fetchActiveFlow()
}
