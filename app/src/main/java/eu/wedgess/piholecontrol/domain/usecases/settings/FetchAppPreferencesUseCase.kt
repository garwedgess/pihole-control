package eu.wedgess.piholecontrol.domain.usecases.settings

import eu.wedgess.piholecontrol.domain.repository.SettingsRepository
import javax.inject.Inject

class FetchAppPreferencesUseCase @Inject constructor(private val repository: SettingsRepository) {
    operator fun invoke() = repository.fetchAllPreferences()
}
