package eu.wedgess.piholecontrol.domain.usecases.app

import eu.wedgess.piholecontrol.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.first

class FetchShouldChangeStatusOnAllConnectionsUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): Boolean =
        settingsRepository.changeStatusOnAllConnection().first()
}