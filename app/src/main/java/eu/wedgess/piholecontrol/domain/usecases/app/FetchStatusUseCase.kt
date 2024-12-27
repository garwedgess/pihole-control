package eu.wedgess.piholecontrol.domain.usecases.app

import eu.wedgess.piholecontrol.domain.model.StatusEntity
import eu.wedgess.piholecontrol.domain.repository.StatusRepository
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class FetchStatusUseCase(
    private val repository: StatusRepository,
    private val periodicRefreshUseCase: PeriodicRefreshUseCase
) {
    operator fun invoke(): Flow<Result<StatusEntity>> {
        return periodicRefreshUseCase { connection ->
            repository.fetchStatus(connection).onFailure {
                Timber.e(it, "Failed to fetch status: ${it.message}")
            }
        }
    }
}
