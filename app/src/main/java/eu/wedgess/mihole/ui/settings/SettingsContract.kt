package eu.wedgess.mihole.ui.settings

import eu.wedgess.mihole.data.model.PiHoleInfo
import eu.wedgess.mihole.data.model.UserPreferences.Theme
import eu.wedgess.mihole.ui.base.UiResult
import eu.wedgess.mihole.ui.base.UnidirectionalViewModel
import eu.wedgess.mihole.utils.UiText

interface SettingsContract :
    UnidirectionalViewModel<SettingsContract.UiState, SettingsContract.Event, SettingsContract.Effect> {

    data class UiState(
        val currentConnection: UiResult<PiHoleInfo>,
        val currentTheme: Theme,
        val refreshInterval: Long,
        val useDynamicThemeColors: Boolean,
        val showRefreshIntervalDialog: Boolean,
        val changeStatusOnAllConnections: Boolean
    ) {

        fun connection(connection: PiHoleInfo): UiState =
            this.copy(currentConnection = UiResult.Success(connection))

        fun connectionError(errorMessage: UiText): UiState =
            this.copy(currentConnection = UiResult.Error(errorMessage))

        fun refreshInterval(interval: Long): UiState =
            this.copy(refreshInterval = interval)

        fun dynamicColors(useDynamicTheme: Boolean): UiState =
            this.copy(useDynamicThemeColors = useDynamicTheme)

        fun theme(theme: Theme): UiState =
            this.copy(currentTheme = theme)

        fun setRefreshInterval(refreshInterval: Long): UiState =
            this.copy(refreshInterval = refreshInterval, showRefreshIntervalDialog = false)

        fun setChangeStatusOnAllConnections(changeOnAll: Boolean): UiState =
            this.copy(changeStatusOnAllConnections = changeOnAll)

        companion object {
            fun initial() = UiState(
                currentConnection = UiResult.Loading,
                currentTheme = Theme.SYSTEM,
                refreshInterval = 10_000,
                useDynamicThemeColors = false,
                showRefreshIntervalDialog = false,
                changeStatusOnAllConnections = false
            )
        }
    }

    sealed interface Effect {
        sealed interface Navigation : Effect {
            object Connections : Navigation
        }

    }

    sealed interface Event {
        object FetchSettings : Event
        data class OnThemeChanged(val theme: Theme) : Event
        data class OnDynamicThemeColorsChanged(val useDynamicTheme: Boolean) : Event
        data class OnRefreshIntervalChanged(val refreshInterval: Long) : Event
        data class OnChangeStatusOnAllConnectionsChanged(val changeOnAll: Boolean) : Event
        object OnRefreshIntervalClicked : Event
        object OnServerClicked : Event
        object OnDismissRefreshIntervalDialog : Event
    }
}