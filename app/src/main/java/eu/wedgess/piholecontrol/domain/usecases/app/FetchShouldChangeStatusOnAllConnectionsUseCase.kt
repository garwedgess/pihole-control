package eu.wedgess.piholecontrol.domain.usecases.app

import eu.wedgess.piholecontrol.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class FetchShouldChangeStatusOnAllConnectionsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): Boolean =
        settingsRepository.changeStatusOnAllConnection().first()
}
