package eu.wedgess.piholecontrol.domain.usecases.app

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.StatusEntity
import eu.wedgess.piholecontrol.domain.repository.StatusRepository
import kotlin.time.Duration

class DisableAdBlockingUseCase(private val repository: StatusRepository) {
    suspend operator fun invoke(
        connection: ConnectionEntity,
        duration: Duration
    ): Result<StatusEntity> {
        return repository.disableAdBlocking(connection, duration)
    }
}