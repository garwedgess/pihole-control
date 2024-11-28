package eu.wedgess.piholecontrol.presentation.settings.model

import eu.wedgess.piholecontrol.data.model.UserPreferences.Theme
import eu.wedgess.piholecontrol.domain.model.AppThemeEntity
import eu.wedgess.piholecontrol.utils.UiText

enum class AppThemePres(val theme: Theme, val label: UiText) {
    System(theme = Theme.SYSTEM, UiText.DynamicString("System")),
    Light(theme = Theme.LIGHT, UiText.DynamicString("Light")),
    Dark(theme = Theme.DARK, UiText.DynamicString("Dark"));
}

fun AppThemePres.mapToEntity(): AppThemeEntity = when (this) {
    AppThemePres.System -> AppThemeEntity.SYSTEM
    AppThemePres.Dark -> AppThemeEntity.DARK
    AppThemePres.Light -> AppThemeEntity.LIGHT
}

fun AppThemeEntity.mapToAppThemePres(): AppThemePres = when (this) {
    AppThemeEntity.SYSTEM -> AppThemePres.System
    AppThemeEntity.DARK -> AppThemePres.Dark
    AppThemeEntity.LIGHT -> AppThemePres.Light
}