package eu.wedgess.piholecontrol.domain.usecases.app

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.StatusEntity
import eu.wedgess.piholecontrol.domain.repository.StatusRepository

class EnableAdBlockingUseCase(private val repository: StatusRepository) {
    suspend operator fun invoke(connection: ConnectionEntity): Result<StatusEntity> {
        return repository.enableAdBlocking(connection)
    }
}
