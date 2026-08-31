package eu.wedgess.piholecontrol.presentation.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.NetworkConnectionState
import eu.wedgess.piholecontrol.domain.model.StatusEntity
import eu.wedgess.piholecontrol.domain.usecases.app.DisableAdBlockingConditionalUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.EnableAdBlockingConditionalUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.FetchAppInfoUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.ObserveNetworkConnectivityUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.SetConnectionAsActiveUseCase
import eu.wedgess.piholecontrol.presentation.app.AppContract
import eu.wedgess.piholecontrol.presentation.app.model.AppDialogType
import eu.wedgess.piholecontrol.presentation.app.model.withAppInfo
import eu.wedgess.piholecontrol.presentation.base.EventDrivenViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val fetchAppInfoUseCase: FetchAppInfoUseCase,
    private val enableAdBlockingConditionalUseCase: EnableAdBlockingConditionalUseCase,
    private val disableAdBlockingConditionalUseCase: DisableAdBlockingConditionalUseCase,
    private val setConnectionAsActiveUseCase: SetConnectionAsActiveUseCase,
    private val observeNetworkConnectivityUseCase: ObserveNetworkConnectivityUseCase
) : ViewModel(),
    EventDrivenViewModel<AppContract.Event> {

    private val _uiState = MutableStateFlow(AppContract.UiState.initial())
    private var timerJob: Job? = null
    private var shouldShowNetworkStatus: Boolean = false

    val uiState = combine(
        fetchAppInfoUseCase(),
        _uiState
    ) { appInfo, uiState ->
        uiState.copy(
            appInfo = appInfo,
            appBarState = uiState.appBarState.withAppInfo(
                adBlockingEnabled = appInfo.status == StatusEntity.ENABLED,
                currentConnection = appInfo.currentConnection,
                connections = appInfo.connections
            )
        )
    }
        .onCompletion {
            shouldShowNetworkStatus = false
        }
        .onStart {
            listenToNetworkChanges()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            AppContract.UiState.initial()
        )

    override fun onEvent(event: AppContract.Event) {
        when (event) {
            AppContract.Event.DismissDialog -> _uiState.update {
                it.copy(dialogType = AppDialogType.None)
            }

            is AppContract.Event.SetDisabledStatus -> setDisableAdBlocking(event.duration)
            is AppContract.Event.OnConnectionSelected -> setConnectionActive(event.mihHole)
            AppContract.Event.SetEnabledStatus -> setEnableAdBlocking()
            AppContract.Event.ShowEnabledStatusDialog -> _uiState.update {
                it.copy(dialogType = AppDialogType.EnableAdBlocking)
            }

            AppContract.Event.ShowDisabledStatusDialog -> _uiState.update {
                it.copy(dialogType = AppDialogType.DisableAdBlocking)
            }

            is AppContract.Event.UpdateAppBarState -> {
                _uiState.update {
                    it.copy(appBarState = event.updateState)
                }
            }
        }
    }

    private fun listenToNetworkChanges() {
        observeNetworkConnectivityUseCase()
            .onEach { handleNetworkStatusChange(it) }
            .launchIn(viewModelScope)
    }

    private fun handleNetworkStatusChange(connectionState: NetworkConnectionState) {
        when (connectionState) {
            NetworkConnectionState.Available -> {
                startTimer()
            }

            NetworkConnectionState.Unavailable -> {
                shouldShowNetworkStatus = true
                timerJob?.cancel()
            }
        }
        _uiState.update {
            it.copy(
                networkConnectionState = it.networkConnectionState.copy(
                    networkConnectionState = connectionState,
                    isVisible = shouldShowNetworkStatus
                )
            )
        }
        if (!shouldShowNetworkStatus) shouldShowNetworkStatus = true
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            delay(CONNECTION_DISMISS_TIME)
            _uiState.update {
                it.copy(networkConnectionState = it.networkConnectionState.copy(isVisible = false))
            }
        }
    }

    private fun setConnectionActive(mihHole: ConnectionEntity) {
        viewModelScope.launch {
            setConnectionAsActiveUseCase(mihHole.id)
                .onFailure { Timber.e("Failed to change active connection", it) }
        }
    }

    private fun setDisableAdBlocking(duration: Long) {
        _uiState.update { it.copy(dialogType = AppDialogType.None) }
        viewModelScope.launch {
            disableAdBlockingConditionalUseCase(duration)
                .onFailure { }
                .onSuccess {
                    delay(REFRESH_DELAY)
                    fetchAppInfoUseCase.refresh()
                }
        }
    }

    private fun setEnableAdBlocking() {
        _uiState.update { it.copy(dialogType = AppDialogType.None) }
        viewModelScope.launch {
            enableAdBlockingConditionalUseCase()
                .onFailure { }
                .onSuccess {
                    delay(REFRESH_DELAY)
                    fetchAppInfoUseCase.refresh()
                }
        }
    }
}

private const val CONNECTION_DISMISS_TIME: Long = 3_000
private const val REFRESH_DELAY: Long = 300
