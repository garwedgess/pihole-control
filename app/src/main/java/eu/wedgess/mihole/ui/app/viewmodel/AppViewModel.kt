package eu.wedgess.mihole.ui.app.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.mihole.R
import eu.wedgess.mihole.data.PiHoleRepository
import eu.wedgess.mihole.data.model.MiHolesInfo
import eu.wedgess.mihole.data.model.ResponseResult
import eu.wedgess.mihole.ui.app.AppContract
import eu.wedgess.mihole.ui.base.RefreshableViewModel
import eu.wedgess.mihole.utils.UiText
import eu.wedgess.mihole.utils.extensions.handleError
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val repository: PiHoleRepository
) : RefreshableViewModel(repository), AppContract {

    private val _uiState: MutableStateFlow<AppContract.UiState> =
        MutableStateFlow(AppContract.UiState.initial())
    override val uiState: StateFlow<AppContract.UiState> = _uiState.asStateFlow()

    private val _effect: Channel<AppContract.Effect> = Channel(Channel.UNLIMITED)
    override val effect: Flow<AppContract.Effect> = _effect.receiveAsFlow()

    override fun onEvent(event: AppContract.Event) {
        when (event) {
            AppContract.Event.FetchSettings -> fetchPreferences()
            AppContract.Event.FetchCurrentConnection -> fetchActiveConnection()
            AppContract.Event.FetchConnections -> fetchConnections()
            AppContract.Event.FetchStatus -> fetchStatus()
            AppContract.Event.DismissEnabledStatusDialog -> _uiState.update { it.copy(showEnableStatusDialog = false) }
            AppContract.Event.DismissDisabledStatusDialog -> _uiState.update { it.copy(showDisableStatusDialog = false) }
            is AppContract.Event.SetDisabledStatus -> setDisableAdBlocking(event.duration)
            is AppContract.Event.OnConnectionSelected -> setConnectionActive(event.mihHole)
            AppContract.Event.SetEnabledStatus -> setEnableAdBlocking()
            AppContract.Event.ShowEnabledStatusDialog -> _uiState.update { it.copy(showEnableStatusDialog = true) }
            AppContract.Event.ShowDisabledStatusDialog -> _uiState.update { it.copy(showDisableStatusDialog = true) }
        }
    }

    private fun setConnectionActive(mihHole: MiHolesInfo) {
        viewModelScope.launch {
            repository.setConnectionAsActive(mihHole)
                .onSuccess { _uiState.update { it.connection(mihHole) } }
                .onFailure { Timber.e("Failed to change active connection", it) }
        }
    }


    private fun setDisableAdBlocking(long: Long) {
        _uiState.update { it.copy(showDisableStatusDialog = false) }
        viewModelScope.launch {
            when (val response = repository.disableAdBlocking(long)) {
                is ResponseResult.Success -> {
                    _uiState.update { it.status(response.data.status) }
                }
                is ResponseResult.Error -> {

                }
            }
        }
    }

    private fun setEnableAdBlocking() {
        _uiState.update { it.copy(showEnableStatusDialog = false) }
        viewModelScope.launch {
            when (val response = repository.enableAdBlocking()) {
                is ResponseResult.Success -> {
                    _uiState.update { it.status(response.data.status) }
                }
                is ResponseResult.Error -> {

                }
            }
        }
    }

    private val connectionErrorHandler = CoroutineExceptionHandler { _, throwable ->
        val errorMessage = throwable.message?.run {
            UiText.DynamicString(this)
        } ?: UiText.StringResource(R.string.all_error_msg_unknown)
        _uiState.update { it.connectionError(errorMessage) }
    }

    private val connectionsErrorHandler = CoroutineExceptionHandler { _, throwable ->
        val errorMessage = throwable.message?.run {
            UiText.DynamicString(this)
        } ?: UiText.StringResource(R.string.all_error_msg_unknown)
        _uiState.update { it.connectionsError(errorMessage) }
    }

    private val statusErrorHandler = CoroutineExceptionHandler { _, throwable ->
        val errorMessage = throwable.message?.run {
            UiText.DynamicString(this)
        } ?: UiText.StringResource(R.string.all_error_msg_unknown)
        _uiState.update { it.statusError(errorMessage) }
    }

    private fun fetchActiveConnection() {
        viewModelScope.launch(connectionErrorHandler) {
            repository.fetchActiveFlow()
                .distinctUntilChanged()
                .collectLatest { miHoleInfo ->
                    Timber.d("New Active connection ${miHoleInfo}")
                    _uiState.update { it.connection(miHoleInfo) }
                }
        }
    }

    private fun fetchConnections() {
        viewModelScope.launch(connectionsErrorHandler) {
            repository.fetchAllFlow()
                .distinctUntilChanged()
                .collectLatest { connections ->
                    _uiState.update { it.connections(connections) }
                }
        }
    }

    private fun fetchStatus() {
        viewModelScope.launch(statusErrorHandler) {
            when (val response = repository.fetchStatus()) {
                is ResponseResult.Success -> {
                    _uiState.update { it.status(response.data.status) }
                }

                is ResponseResult.Error -> {
                    statusErrorHandler.handleException(
                        this@launch.coroutineContext,
                        response.handleError()
                    )
                }
            }
        }
    }

    private fun fetchPreferences() {
        viewModelScope.launch {
            repository.fetchUserPreferences()
                .distinctUntilChanged()
                .collectLatest { preferences ->
                    _uiState.update { state ->
                        state.theme(preferences.theme)
                            .refreshInterval(preferences.refreshTime)
                            .dynamicColors(preferences.useDynamicColors)
                    }
                }
        }
    }

    override fun onRefresh() {
        fetchStatus()
    }
}