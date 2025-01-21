package eu.wedgess.piholecontrol.domain.usecases

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.NetworkConnectionState
import eu.wedgess.piholecontrol.domain.model.RefreshMode
import eu.wedgess.piholecontrol.domain.repository.SettingsRepository
import eu.wedgess.piholecontrol.domain.usecases.app.ObserveNetworkConnectivityUseCase
import eu.wedgess.piholecontrol.presentation.base.RefreshFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

class PeriodicRefreshUseCase(
    private val observeActiveUser: ObserveActiveUserUseCase,
    private val observeNetworkConnectivityUseCase: ObserveNetworkConnectivityUseCase,
    private val settingsRepository: SettingsRepository
) {
    private val refreshFlow = RefreshFlow()
    private var currentRefreshMode: RefreshMode = RefreshMode.Automatic()
    private var lastResult: Result<*>? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun <T> invoke(fetchData: suspend (ConnectionEntity) -> Result<T>): Flow<Result<T>> {
        return refreshFlow.flatMapLatest {
            combine(
                observeActiveUser(),
                observeNetworkConnectivityUseCase(),
                settingsRepository.getRefreshInterval()
            ) { pihole, networkStatus, delay ->
                Triple(pihole, networkStatus, delay)
            }
        }.flatMapLatest { (pihole, networkStatus, delay) ->
            flow {
                val activePihole = pihole.getOrNull()

                setRefreshModeBasedOnNetworkStatus(networkStatus)

                when (val refreshMode = currentRefreshMode) {
                    is RefreshMode.Automatic -> {
                        if (activePihole != null) {
                            val result = fetchData(activePihole)
                            emit(result)

                            if (result.isFailure) {
                                currentRefreshMode = RefreshMode.Manual
                            } else {
                                lastResult = result
                                delay(refreshMode.refreshDelay ?: delay)
                                refreshFlow.refresh()
                            }
                        }
                    }

                    RefreshMode.Manual -> {
                        if (activePihole != null) {
                            val result = fetchData(activePihole)
                            emit(result)

                            if (result.isSuccess) {
                                lastResult = result
                                currentRefreshMode = RefreshMode.Automatic()
                            }
                        }
                    }

                    RefreshMode.None -> {
                        lastResult?.run {
                            @Suppress("UNCHECKED_CAST")
                            emit(this@run as Result<T>)
                        } ?: emit(
                            Result.failure(Exception("Failed to connect to ${activePihole?.host}"))
                        )
                    }
                }
            }
        }
    }

    private fun setRefreshModeBasedOnNetworkStatus(networkStatus: NetworkConnectionState) {
        when (networkStatus) {
            NetworkConnectionState.Available -> {
                if (currentRefreshMode == RefreshMode.None) {
                    currentRefreshMode = RefreshMode.Automatic()
                }
            }

            NetworkConnectionState.Unavailable -> {
                if (currentRefreshMode != RefreshMode.None) {
                    currentRefreshMode = RefreshMode.None
                }
            }
        }
    }

    fun setRefreshMode(refreshMode: RefreshMode) {
        this.currentRefreshMode = refreshMode
    }

    fun triggerRefresh() = refreshFlow.refresh()
}
