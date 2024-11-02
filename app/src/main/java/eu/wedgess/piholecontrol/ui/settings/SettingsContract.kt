package eu.wedgess.piholecontrol.ui.settings

import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.data.model.UserPreferences.Theme
import eu.wedgess.piholecontrol.ui.settings.model.SettingsDialogType

interface SettingsContract {

    data class UiState(
        val currentConnection: ConnectionInfo,
        val currentTheme: Theme,
        val refreshInterval: Long,
        val useDynamicThemeColors: Boolean,
        val dialogType: SettingsDialogType,
        val changeStatusOnAllConnections: Boolean
    ) {

        companion object {
            fun initial() = UiState(
                currentConnection = ConnectionInfo.default,
                currentTheme = Theme.SYSTEM,
                refreshInterval = 10_000,
                useDynamicThemeColors = false,
                dialogType = SettingsDialogType.None,
                changeStatusOnAllConnections = false
            )
        }
    }

    sealed interface Effect {
        sealed interface Navigation : Effect {
            data object Connections : Navigation
        }
    }

    sealed interface Event {
        data class OnThemeChanged(val theme: Theme) : Event
        data class OnDynamicThemeColorsChanged(val useDynamicTheme: Boolean) : Event
        data class OnRefreshIntervalChanged(val refreshInterval: Long) : Event
        data class OnChangeStatusOnAllConnectionsChanged(val changeOnAll: Boolean) : Event
        data object OnRefreshIntervalClicked : Event
        data object OnServerClicked : Event
        data object OnDismissDialog : Event
    }
}