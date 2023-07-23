package eu.wedgess.mihole.ui.app

import eu.wedgess.mihole.data.model.MiHolesInfo
import eu.wedgess.mihole.data.model.UserPreferences.Theme
import eu.wedgess.mihole.ui.base.UiResult
import eu.wedgess.mihole.ui.base.UnidirectionalViewModel
import eu.wedgess.mihole.ui.settings.SettingsContract
import eu.wedgess.mihole.utils.UiText

interface AppContract :
    UnidirectionalViewModel<AppContract.UiState, AppContract.Event, AppContract.Effect> {

    data class UiState(
        val currentConnection: UiResult<MiHolesInfo>,
        val currentTheme: Theme,
        val refreshInterval: Int,
        val useDynamicThemeColors: Boolean
    ) {

        fun connection(connection: MiHolesInfo): UiState =
            this.copy(currentConnection = UiResult.Success(connection))

        fun connectionError(errorMessage: UiText): UiState =
            this.copy(currentConnection = UiResult.Error(errorMessage))

        fun refreshInterval(interval: Int): UiState =
            this.copy(refreshInterval = interval)

        fun theme(theme: Theme): UiState =
            this.copy(currentTheme = theme)

        fun dynamicColors(useDynamicTheme: Boolean): AppContract.UiState =
            this.copy(useDynamicThemeColors = useDynamicTheme)

        companion object {
            fun initial() = UiState(
                currentConnection = UiResult.Loading,
                currentTheme = Theme.SYSTEM,
                refreshInterval = 10_000,
                useDynamicThemeColors = false
            )
        }
    }

    sealed interface Effect {
    }

    sealed interface Event {
        object FetchCurrentConnection : Event
        object FetchSettings : Event
    }
}