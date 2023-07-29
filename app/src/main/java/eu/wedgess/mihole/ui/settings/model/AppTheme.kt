package eu.wedgess.mihole.ui.settings.model

import eu.wedgess.mihole.data.model.UserPreferences.Theme
import eu.wedgess.mihole.utils.UiText

enum class AppTheme(val theme: Theme, val label: UiText) {
    System(theme = Theme.SYSTEM, UiText.DynamicString("System")),
    Light(theme = Theme.LIGHT, UiText.DynamicString("Light")),
    Dark(theme = Theme.DARK, UiText.DynamicString("Dark"));
}

fun AppTheme.mapToTheme(): Theme = when(this) {
    AppTheme.System -> Theme.SYSTEM
    AppTheme.Dark -> Theme.DARK
    AppTheme.Light -> Theme.LIGHT
}

fun Theme.mapToAppTheme(): AppTheme = when(this) {
    Theme.SYSTEM, Theme.UNRECOGNIZED -> AppTheme.System
    Theme.DARK -> AppTheme.Dark
    Theme.LIGHT -> AppTheme.Light
}