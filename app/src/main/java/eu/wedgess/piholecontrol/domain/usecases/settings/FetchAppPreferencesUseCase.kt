package eu.wedgess.piholecontrol.domain.usecases.settings

import eu.wedgess.piholecontrol.domain.repository.SettingsRepository

class FetchAppPreferencesUseCase(private val repository: SettingsRepository) {
    operator fun invoke() = repository.fetchAllPreferences()
}