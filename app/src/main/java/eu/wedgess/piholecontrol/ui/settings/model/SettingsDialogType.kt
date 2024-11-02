package eu.wedgess.piholecontrol.ui.settings.model

sealed interface SettingsDialogType {
    data object None : SettingsDialogType
    data class RefreshInterval(val currentRefreshTime: Long) : SettingsDialogType
}