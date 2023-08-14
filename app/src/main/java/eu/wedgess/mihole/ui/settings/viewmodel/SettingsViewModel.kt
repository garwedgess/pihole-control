package eu.wedgess.mihole.ui.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.mihole.R
import eu.wedgess.mihole.data.PiHoleRepository
import eu.wedgess.mihole.data.model.UserPreferences
import eu.wedgess.mihole.ui.settings.SettingsContract
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
class SettingsViewModel @Inject constructor(
    private val repository: PiHoleRepository
) : ViewModel(), SettingsContract {

    private val _uiState: MutableStateFlow<SettingsContract.UiState> =
        MutableStateFlow(SettingsContract.UiState.initial())
    override val uiState: StateFlow<SettingsContract.UiState> = _uiState.asStateFlow()

    private val _effect: Channel<SettingsContract.Effect> = Channel(Channel.UNLIMITED)
    override val effect: Flow<SettingsContract.Effect> = _effect.receiveAsFlow()

    override fun onEvent(event: SettingsContract.Event) {
        when (event) {
            SettingsContract.Event.FetchSettings -> fetchSettings()
            SettingsContract.Event.OnRefreshIntervalClicked ->
                _uiState.update { it.copy(showRefreshIntervalDialog = true) }
            SettingsContract.Event.OnServerClicked ->
                navigateTo(SettingsContract.Effect.Navigation.Connections)
            SettingsContract.Event.OnDismissRefreshIntervalDialog ->
                _uiState.update { it.copy(showRefreshIntervalDialog = false) }
            is SettingsContract.Event.OnThemeChanged -> updateTheme(event.theme)
            is SettingsContract.Event.OnDynamicThemeColorsChanged ->
                updateDynamicTheme(event.useDynamicTheme)
            is SettingsContract.Event.OnRefreshIntervalChanged ->
                updateRefreshInterval(event.refreshInterval)
            is SettingsContract.Event.OnChangeStatusOnAllConnectionsChanged ->
                updateStatusChangeOnAllConnections(event.changeOnAll)
        }
    }

    private fun updateRefreshInterval(refreshInterval: Long) {
        viewModelScope.launch {
            repository.updateRefreshInterval(refreshInterval).onFailure {
                Timber.e("Failed to refresh interval", it)
                return@launch
            }
            _uiState.update { it.setRefreshInterval(refreshInterval = refreshInterval) }
        }
    }

    private fun updateStatusChangeOnAllConnections(applyOnAll: Boolean) {
        viewModelScope.launch {
            repository.updateStatusChangeOnAllConnections(applyOnAll).onFailure {
                Timber.e("Failed to refresh interval", it)
                return@launch
            }
            _uiState.update { it.setChangeStatusOnAllConnections(changeOnAll = applyOnAll) }
        }
    }

    private fun navigateTo(destination: SettingsContract.Effect.Navigation) {
        viewModelScope.launch { _effect.send(destination) }
    }

    private fun updateTheme(theme: UserPreferences.Theme) {
        viewModelScope.launch {
            repository.updateSelectedTheme(theme).onFailure {
                Timber.e("Failed to update theme", it)
                return@launch
            }
            _uiState.update { it.copy(currentTheme = theme) }
        }
    }

    private fun updateDynamicTheme(useDynamicTheme: Boolean) {
        viewModelScope.launch {
            repository.updateDynamicTheme(useDynamicTheme).onFailure {
                Timber.e("Failed to set dynamic theme to $useDynamicTheme", it)
                it.printStackTrace()
                return@launch
            }
            _uiState.update { it.copy(useDynamicThemeColors = useDynamicTheme) }
        }
    }

    private val connectionErrorHandler = CoroutineExceptionHandler { _, throwable ->
        val errorMessage = throwable.message?.run {
            UiText.DynamicString(this)
        } ?: UiText.StringResource(R.string.all_error_msg_unknown)
        _uiState.update { it.connectionError(errorMessage) }
    }

    private fun fetchSettings() {
        fetchConnections()
        fetchPreferences()
    }

    private fun fetchConnections() {
        viewModelScope.launch(connectionErrorHandler) {
            val connection = repository.fetchActive().getOrThrow()
            _uiState.update { it.connection(connection) }
        }
    }

    private fun fetchPreferences() {
        viewModelScope.launch {
            repository.fetchUserPreferences()
                .distinctUntilChanged()
                .collectLatest { preferences ->
                    Timber.d("Preferences: $preferences")
                    _uiState.update { state ->
                        state.theme(preferences.theme)
                            .refreshInterval(preferences.refreshTime)
                            .dynamicColors(preferences.useDynamicColors)
                            .setChangeStatusOnAllConnections(preferences.changeStatusOnAllConnection)
                    }
                }
        }
    }
}