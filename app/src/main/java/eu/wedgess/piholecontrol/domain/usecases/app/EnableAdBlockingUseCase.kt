package eu.wedgess.piholecontrol.domain.usecases.app

import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.domain.model.StatusEntity
import eu.wedgess.piholecontrol.domain.repository.StatusRepository

class EnableAdBlockingUseCase(private val repository: StatusRepository) {
    suspend operator fun invoke(connection: ConnectionInfo): Result<StatusEntity> {
        return repository.enableAdBlocking(connection)
    }
}