package eu.wedgess.piholecontrol.domain.usecases.settings

import eu.wedgess.piholecontrol.domain.repository.SettingsRepository

class UpdateDynamicThemeUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke(useDynamicTheme: Boolean): Result<Unit> {
        return repository.updateDynamicTheme(useDynamicTheme)
    }
}