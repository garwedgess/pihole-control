package eu.wedgess.piholecontrol.presentation.settings

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.presentation.settings.model.AppThemePres
import eu.wedgess.piholecontrol.presentation.settings.model.SettingsDialogType

interface SettingsContract {

    data class UiState(
        val currentConnection: ConnectionEntity,
        val currentTheme: AppThemePres,
        val refreshInterval: Long,
        val useDynamicThemeColors: Boolean,
        val dialogType: SettingsDialogType,
        val changeStatusOnAllConnections: Boolean
    ) {

        companion object {
            fun initial() = UiState(
                currentConnection = ConnectionEntity.Version5.default,
                currentTheme = AppThemePres.System,
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
        data class OnThemeChanged(val theme: AppThemePres) : Event
        data class OnDynamicThemeColorsChanged(val useDynamicTheme: Boolean) : Event
        data class OnRefreshIntervalChanged(val refreshInterval: Long) : Event
        data class OnChangeStatusOnAllConnectionsChanged(val changeOnAll: Boolean) : Event
        data object OnRefreshIntervalClicked : Event
        data object OnServerClicked : Event
        data object OnDismissDialog : Event
    }
}
