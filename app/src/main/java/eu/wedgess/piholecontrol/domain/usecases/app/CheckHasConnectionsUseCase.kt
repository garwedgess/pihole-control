package eu.wedgess.piholecontrol.domain.usecases.app

import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import javax.inject.Inject

class CheckHasConnectionsUseCase @Inject constructor(
    private val connectionRepository: ConnectionRepository
) {
    suspend operator fun invoke(): Result<Boolean> = connectionRepository.checkHasConnections()
}
