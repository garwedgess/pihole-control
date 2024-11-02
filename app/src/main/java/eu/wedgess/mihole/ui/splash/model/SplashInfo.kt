package eu.wedgess.mihole.ui.splash.model

import eu.wedgess.mihole.data.model.UserPreferences

data class SplashInfo(
    val theme: UserPreferences.Theme,
    val useDynamicColors: Boolean
)