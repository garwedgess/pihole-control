package eu.wedgess.piholecontrol.domain.usecases

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.RefreshMode
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
    private var currentRefreshMode: RefreshMode = RefreshMode.Automatic

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun <T> invoke(fetchData: suspend (ConnectionEntity) -> Result<T>): Flow<Result<T>> {
        val delayFlow = settingsRepository.getRefreshInterval()

        return refreshFlow.flatMapLatest {
            combine(
                observeActiveUser(),
                delayFlow
            ) { activeConnection, delay ->
                activeConnection to delay
            }
        }.flatMapLatest { (activeConnection, delay) ->
            flow {
                val currentActiveConnection = activeConnection.getOrNull()

                when (currentRefreshMode) {
                    RefreshMode.Automatic -> {
                        while (
                            currentActiveConnection != null &&
                            currentRefreshMode == RefreshMode.Automatic
                        ) {
                            val result = fetchData(currentActiveConnection)
                            emit(result)
                            if (result.isFailure) {
                                currentRefreshMode = RefreshMode.Manual
                            } else {
                                delay(delay)
                            }
                        }
                    }

                    RefreshMode.Manual -> {
                        if (currentActiveConnection != null) {
                            val result = fetchData(currentActiveConnection)
                            emit(result)
                            if (result.isSuccess) {
                                currentRefreshMode = RefreshMode.Automatic
                            }
                        }
                    }
                }
            }
        }
    }

    fun triggerRefresh() = refreshFlow.refresh()
}
