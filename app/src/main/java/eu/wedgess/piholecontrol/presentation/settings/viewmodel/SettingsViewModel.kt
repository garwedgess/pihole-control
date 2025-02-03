package eu.wedgess.piholecontrol.presentation.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.usecases.ObserveActiveUserUseCase
import eu.wedgess.piholecontrol.domain.usecases.settings.FetchAppPreferencesUseCase
import eu.wedgess.piholecontrol.domain.usecases.settings.UpdateDynamicThemeUseCase
import eu.wedgess.piholecontrol.domain.usecases.settings.UpdateRefreshIntervalUseCase
import eu.wedgess.piholecontrol.domain.usecases.settings.UpdateSelectedThemeUseCase
import eu.wedgess.piholecontrol.domain.usecases.settings.UpdateStatusChangeOnAllConnectionsUseCase
import eu.wedgess.piholecontrol.presentation.base.EventDrivenViewModel
import eu.wedgess.piholecontrol.presentation.base.SideEffectViewModel
import eu.wedgess.piholecontrol.presentation.base.SideEffectViewModelImpl
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.settings.SettingsContract
import eu.wedgess.piholecontrol.presentation.settings.model.AppThemePres
import eu.wedgess.piholecontrol.presentation.settings.model.SettingsDialogType
import eu.wedgess.piholecontrol.presentation.settings.model.mapToAppThemePres
import eu.wedgess.piholecontrol.presentation.settings.model.mapToEntity
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
    fetchAppPreferencesUseCase: FetchAppPreferencesUseCase,
    observeActiveUserUseCase: ObserveActiveUserUseCase,
    private val updateRefreshIntervalUseCase: UpdateRefreshIntervalUseCase,
    private val updateDynamicThemeUseCase: UpdateDynamicThemeUseCase,
    private val updateSelectedThemeUseCase: UpdateSelectedThemeUseCase,
    private val updateStatusChangeOnAllConnectionsUseCase: UpdateStatusChangeOnAllConnectionsUseCase
) : ViewModel(),
    EventDrivenViewModel<SettingsContract.Event>,
    SideEffectViewModel<SettingsContract.Effect> by SideEffectViewModelImpl() {

    private val uiState = MutableStateFlow(SettingsContract.UiState.initial())

    val uiResult = combine(
        observeActiveUserUseCase(),
        fetchAppPreferencesUseCase(),
        uiState
    ) { connection, preferences, uiState ->
        UIResult.Loaded(
            uiState.copy(
                currentConnection = connection.getOrNull() ?: ConnectionEntity.Version5.default,
                currentTheme = preferences.theme.mapToAppThemePres(),
                refreshInterval = preferences.refreshInterval,
                useDynamicThemeColors = preferences.useDynamicColors,
                changeStatusOnAllConnections = preferences.multiStatusChange
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
            updateRefreshIntervalUseCase(refreshInterval).onFailure {
                Timber.e("Failed to refresh interval", it)
                return@launch
            }
        }
    }

    private fun updateStatusChangeOnAllConnections(applyOnAll: Boolean) {
        viewModelScope.launch {
            updateStatusChangeOnAllConnectionsUseCase(applyOnAll).onFailure {
                Timber.e(it, "Failed to refresh interval")
                return@launch
            }
        }
    }

    private fun navigateTo(destination: SettingsContract.Effect.Navigation) {
        viewModelScope.emitSideEffect(destination)
    }

    private fun updateTheme(theme: AppThemePres) {
        viewModelScope.launch {
            updateSelectedThemeUseCase(theme.mapToEntity()).onFailure {
                Timber.e(it, "Failed to update theme")
                return@launch
            }
        }
    }

    private fun updateDynamicTheme(useDynamicTheme: Boolean) {
        viewModelScope.launch {
            updateDynamicThemeUseCase(useDynamicTheme).onFailure {
                Timber.e(it, "Failed to set dynamic theme to $useDynamicTheme")
                it.printStackTrace()
                return@launch
            }
        }
    }
}
