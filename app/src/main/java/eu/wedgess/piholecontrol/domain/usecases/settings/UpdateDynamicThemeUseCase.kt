package eu.wedgess.piholecontrol.domain.usecases.settings

import eu.wedgess.piholecontrol.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateDynamicThemeUseCase @Inject constructor(private val repository: SettingsRepository) {
    suspend operator fun invoke(useDynamicTheme: Boolean): Result<Unit> {
        return repository.updateDynamicTheme(useDynamicTheme)
    }
}
