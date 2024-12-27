package eu.wedgess.piholecontrol.domain.usecases.settings

import eu.wedgess.piholecontrol.domain.model.AppThemeEntity
import eu.wedgess.piholecontrol.domain.repository.SettingsRepository

class UpdateSelectedThemeUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke(theme: AppThemeEntity): Result<Unit> {
        return repository.updateSelectedTheme(theme)
    }
}
