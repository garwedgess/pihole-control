package eu.wedgess.piholecontrol.data.utils

import eu.wedgess.piholecontrol.data.model.UserPreferences
import eu.wedgess.piholecontrol.data.model.UserPreferences.Theme

fun UserPreferences.Builder.defaultValues(): UserPreferences.Builder =
    this.setThemeValue(Theme.DARK_VALUE)
        .setDefaultLogEntries(500)
        .setUseDynamicColors(false)
        .setRefreshTime(10_000)
        .setChangeStatusOnAllConnection(false)
