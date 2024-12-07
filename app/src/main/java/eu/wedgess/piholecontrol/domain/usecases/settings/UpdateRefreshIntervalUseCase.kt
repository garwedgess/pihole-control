package eu.wedgess.piholecontrol.domain.usecases.settings

import eu.wedgess.piholecontrol.domain.repository.SettingsRepository

class UpdateRefreshIntervalUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke(interval: Long): Result<Unit> {
        return repository.updateRefreshInterval(interval)
    }
}