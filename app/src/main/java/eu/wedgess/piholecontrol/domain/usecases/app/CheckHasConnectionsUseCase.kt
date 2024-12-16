package eu.wedgess.piholecontrol.domain.usecases.app

import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository

class CheckHasConnectionsUseCase(private val connectionRepository: ConnectionRepository) {
    suspend operator fun invoke(): Result<Boolean> = connectionRepository.checkHasConnections()
}
