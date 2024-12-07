package eu.wedgess.piholecontrol.presentation.settings.model

sealed interface SettingsDialogType {
    data object None : SettingsDialogType
    data class RefreshInterval(val currentRefreshTime: Long) : SettingsDialogType
}