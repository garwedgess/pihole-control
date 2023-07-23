package eu.wedgess.mihole.ui.settings

import eu.wedgess.mihole.data.model.MiHolesInfo
import eu.wedgess.mihole.data.model.UserPreferences.Theme
import eu.wedgess.mihole.ui.base.UiResult
import eu.wedgess.mihole.ui.base.UnidirectionalViewModel
import eu.wedgess.mihole.utils.UiText

interface SettingsContract :
    UnidirectionalViewModel<SettingsContract.UiState, SettingsContract.Event, SettingsContract.Effect> {

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

        fun dynamicColors(useDynamicTheme: Boolean): UiState =
            this.copy(useDynamicThemeColors = useDynamicTheme)

        fun theme(theme: Theme): UiState =
            this.copy(currentTheme = theme)

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
        sealed interface Navigation : Effect {
            object Connections : Navigation
        }

    }

    sealed interface Event {
        object FetchSettings : Event
        data class OnThemeChanged(val theme: Theme) : Event
        data class OnDynamicThemeColorsChanged(val useDynamicTheme: Boolean) : Event
        object OnRefreshIntervalClicked : Event
        object OnServerClicked : Event
    }
}