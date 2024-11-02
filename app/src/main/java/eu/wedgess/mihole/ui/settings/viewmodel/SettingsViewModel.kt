package eu.wedgess.mihole.ui.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.mihole.data.model.PiHoleInfo
import eu.wedgess.mihole.data.model.UserPreferences
import eu.wedgess.mihole.data.toPiHoleInfo
import eu.wedgess.mihole.ui.base.EventDrivenViewModel
import eu.wedgess.mihole.ui.base.SideEffectViewModel
import eu.wedgess.mihole.ui.base.SideEffectViewModelImpl
import eu.wedgess.mihole.ui.compose.ResultType
import eu.wedgess.mihole.ui.compose.UIResult
import eu.wedgess.mihole.ui.settings.SettingsContract
import eu.wedgess.mihole.ui.settings.controller.SettingsController
import eu.wedgess.mihole.ui.settings.model.SettingsDialogType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val controller: SettingsController
) : ViewModel(),
    EventDrivenViewModel<SettingsContract.Event>,
    SideEffectViewModel<SettingsContract.Effect> by SideEffectViewModelImpl() {

    private val uiState = MutableStateFlow(SettingsContract.UiState.initial())

    val uiResult = combine(
        controller.fetchActiveConnection(),
        controller.fetchUserPreferences(),
        uiState
    ) { connection, preferences, uiState ->
        UIResult.Loaded(
            uiState.copy(
                currentConnection = connection?.toPiHoleInfo() ?: PiHoleInfo.default,
                currentTheme = preferences.theme,
                refreshInterval = preferences.refreshTime,
                useDynamicThemeColors = preferences.useDynamicColors
            )
        )
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            UIResult.Loading(ResultType.Loading.WithTitle())
        )

    override fun onEvent(event: SettingsContract.Event) {
        when (event) {
            SettingsContract.Event.OnRefreshIntervalClicked -> uiState.update {
                it.copy(dialogType = SettingsDialogType.RefreshInterval(it.refreshInterval))
            }

            SettingsContract.Event.OnServerClicked ->
                navigateTo(SettingsContract.Effect.Navigation.Connections)

            SettingsContract.Event.OnDismissDialog -> uiState.update {
                it.copy(dialogType = SettingsDialogType.None)
            }

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
            controller.updateRefreshInterval(refreshInterval).onFailure {
                Timber.e("Failed to refresh interval", it)
                return@launch
            }
            uiState.update { it.copy(refreshInterval = refreshInterval) }
        }
    }

    private fun updateStatusChangeOnAllConnections(applyOnAll: Boolean) {
        viewModelScope.launch {
            controller.updateStatusChangeOnAllConnections(applyOnAll).onFailure {
                Timber.e("Failed to refresh interval", it)
                return@launch
            }
            uiState.update { it.copy(changeStatusOnAllConnections = applyOnAll) }
        }
    }

    private fun navigateTo(destination: SettingsContract.Effect.Navigation) {
        viewModelScope.emitSideEffect(destination)
    }

    private fun updateTheme(theme: UserPreferences.Theme) {
        viewModelScope.launch {
            controller.updateSelectedTheme(theme).onFailure {
                Timber.e("Failed to update theme", it)
                return@launch
            }
            uiState.update { it.copy(currentTheme = theme) }
        }
    }

    private fun updateDynamicTheme(useDynamicTheme: Boolean) {
        viewModelScope.launch {
            controller.updateDynamicTheme(useDynamicTheme).onFailure {
                Timber.e("Failed to set dynamic theme to $useDynamicTheme", it)
                it.printStackTrace()
                return@launch
            }
            uiState.update { it.copy(useDynamicThemeColors = useDynamicTheme) }
        }
    }
}