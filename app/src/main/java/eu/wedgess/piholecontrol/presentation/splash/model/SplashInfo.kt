package eu.wedgess.piholecontrol.presentation.splash.model

import eu.wedgess.piholecontrol.presentation.settings.model.AppThemePres

data class SplashInfo(
    val theme: AppThemePres,
    val useDynamicColors: Boolean,
    val hasConnections: Boolean
)
