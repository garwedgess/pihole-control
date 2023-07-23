package eu.wedgess.mihole.ui.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.mihole.R
import eu.wedgess.mihole.data.PiHoleRepository
import eu.wedgess.mihole.ui.app.AppContract
import eu.wedgess.mihole.utils.UiText
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
) : ViewModel(), AppContract {

    private val _uiState: MutableStateFlow<AppContract.UiState> =
        MutableStateFlow(AppContract.UiState.initial())
    override val uiState: StateFlow<AppContract.UiState> = _uiState.asStateFlow()

    private val _effect: Channel<AppContract.Effect> = Channel(Channel.UNLIMITED)
    override val effect: Flow<AppContract.Effect> = _effect.receiveAsFlow()

    override fun onEvent(event: AppContract.Event) {
        when (event) {
            AppContract.Event.FetchSettings -> fetchSettings()
            AppContract.Event.FetchCurrentConnection -> fetchActiveConnection()
        }
    }

    private val connectionErrorHandler = CoroutineExceptionHandler { _, throwable ->
        val errorMessage = throwable.message?.run {
            UiText.DynamicString(this)
        } ?: UiText.StringResource(R.string.all_error_msg_unknown)
        _uiState.update { it.connectionError(errorMessage) }
    }

    private fun fetchSettings() {
        fetchPreferences()
        fetchActiveConnection()
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
}