package eu.wedgess.piholecontrol.data.extensions

import eu.wedgess.piholecontrol.data.model.UserPreferences
import eu.wedgess.piholecontrol.data.model.UserPreferences.Theme

fun UserPreferences.Builder.defaultValues(): UserPreferences.Builder =
    this.setThemeValue(Theme.DARK_VALUE)
        .setDefaultLogEntries(100)
        .setUseDynamicColors(false)
        .setRefreshTime(5_000)
        .setChangeStatusOnAllConnection(false)
