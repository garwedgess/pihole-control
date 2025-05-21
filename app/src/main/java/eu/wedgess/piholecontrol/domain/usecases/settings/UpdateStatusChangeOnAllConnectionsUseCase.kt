package eu.wedgess.piholecontrol.domain.usecases.settings

import eu.wedgess.piholecontrol.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateStatusChangeOnAllConnectionsUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(applyOnAll: Boolean): Result<Unit> {
        return repository.updateStatusChangeOnAllConnections(applyOnAll)
    }
}
