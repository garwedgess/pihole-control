package eu.wedgess.piholecontrol.domain.usecases

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.SettingsRepository
import eu.wedgess.piholecontrol.presentation.base.RefreshFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

class PeriodicRefreshUseCase(
    private val observeActiveUser: ObserveActiveUserUseCase,
    private val settingsRepository: SettingsRepository
) {
    private val refreshFlow = RefreshFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun <T> invoke(fetchData: suspend (ConnectionEntity) -> T): Flow<T> {
        val delayFlow = settingsRepository.getRefreshInterval()

        return refreshFlow.flatMapLatest {
            combine(
                observeActiveUser(),
                delayFlow
            ) { connection, delay ->
                connection to delay
            }
        }.flatMapLatest { (connection, delay) ->
            flow {
                val currentConnection = connection.getOrNull()
                while (currentConnection != null) {
                    emit(fetchData(currentConnection))
                    delay(delay)
                }
            }
        }
    }

    fun triggerRefresh() = refreshFlow.refresh()
}
