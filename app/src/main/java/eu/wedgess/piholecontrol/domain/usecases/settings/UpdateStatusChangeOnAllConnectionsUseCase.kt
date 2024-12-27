package eu.wedgess.piholecontrol.domain.usecases.settings

import eu.wedgess.piholecontrol.domain.repository.SettingsRepository

class UpdateStatusChangeOnAllConnectionsUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke(applyOnAll: Boolean): Result<Unit> {
        return repository.updateStatusChangeOnAllConnections(applyOnAll)
    }
}
