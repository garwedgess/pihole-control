package eu.wedgess.piholecontrol.ui.splash.model

import eu.wedgess.piholecontrol.data.model.UserPreferences

data class SplashInfo(
    val theme: UserPreferences.Theme,
    val useDynamicColors: Boolean
)