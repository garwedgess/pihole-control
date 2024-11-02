package eu.wedgess.mihole.ui.app.model

import eu.wedgess.mihole.data.model.PiHoleInfo
import eu.wedgess.mihole.data.model.UserPreferences.Theme
import eu.wedgess.mihole.data.model.enums.PiHoleStatus

data class PiHoleAppInfo(
    val currentConnection: PiHoleInfo,
    val connections: List<PiHoleInfo>,
    val status: PiHoleStatus,
    val currentTheme: Theme,
    val refreshInterval: Long,
    val useDynamicThemeColors: Boolean
) {
    companion object {
        fun initial() = PiHoleAppInfo(
            currentConnection =PiHoleInfo.default,
            connections = emptyList(),
            currentTheme = Theme.SYSTEM,
            refreshInterval = 10_000,
            useDynamicThemeColors = false,
            status = PiHoleStatus.UNKNOWN
        )
    }
}
