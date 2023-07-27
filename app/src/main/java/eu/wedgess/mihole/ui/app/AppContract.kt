package eu.wedgess.mihole.ui.app

import eu.wedgess.mihole.data.model.MiHolesInfo
import eu.wedgess.mihole.data.model.UserPreferences.Theme
import eu.wedgess.mihole.data.model.enums.PiHoleStatus
import eu.wedgess.mihole.ui.base.UiResult
import eu.wedgess.mihole.ui.base.UnidirectionalViewModel
import eu.wedgess.mihole.utils.UiText

interface AppContract :
    UnidirectionalViewModel<AppContract.UiState, AppContract.Event, AppContract.Effect> {

    data class UiState(
        val currentConnection: UiResult<MiHolesInfo>,
        val connections: UiResult<List<MiHolesInfo>>,
        val status: UiResult<PiHoleStatus>,
        val currentTheme: Theme,
        val refreshInterval: Long,
        val useDynamicThemeColors: Boolean,
        val showConnectionDropdown: Boolean,
        val showEnableStatusDialog: Boolean,
        val showDisableStatusDialog: Boolean
    ) {

        fun connection(connection: MiHolesInfo): UiState =
            this.copy(currentConnection = UiResult.Success(connection))

        fun connectionError(errorMessage: UiText): UiState =
            this.copy(currentConnection = UiResult.Error(errorMessage))

        fun connections(connection: List<MiHolesInfo>): UiState =
            this.copy(connections = UiResult.Success(connection))

        fun connectionsError(errorMessage: UiText): UiState =
            this.copy(connections = UiResult.Error(errorMessage))

        fun status(status: PiHoleStatus): UiState =
            this.copy(status = UiResult.Success(status))

        fun statusError(errorMessage: UiText): UiState =
            this.copy(status = UiResult.Error(errorMessage))

        fun refreshInterval(interval: Long): UiState =
            this.copy(refreshInterval = interval)

        fun theme(theme: Theme): UiState =
            this.copy(currentTheme = theme)

        fun dynamicColors(useDynamicTheme: Boolean): AppContract.UiState =
            this.copy(useDynamicThemeColors = useDynamicTheme)

        companion object {
            fun initial() = UiState(
                currentConnection = UiResult.Loading,
                connections = UiResult.Loading,
                currentTheme = Theme.SYSTEM,
                refreshInterval = 10_000,
                useDynamicThemeColors = false,
                status = UiResult.Loading,
                showConnectionDropdown = false,
                showEnableStatusDialog = false,
                showDisableStatusDialog = false
            )
        }
    }

    sealed interface Effect {
    }

    sealed interface Event {
        object FetchCurrentConnection : Event
        object FetchConnections : Event
        object FetchStatus : Event
        object FetchSettings : Event
        object ShowEnabledStatusDialog: Event
        object DismissEnabledStatusDialog: Event
        object ShowDisabledStatusDialog: Event
        object DismissDisabledStatusDialog: Event
        object SetEnabledStatus: Event
        data class OnConnectionSelected(val mihHole: MiHolesInfo): Event
        data class SetDisabledStatus(val duration: Long): Event
    }
}