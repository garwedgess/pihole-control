package eu.wedgess.mihole.data.utils

import eu.wedgess.mihole.data.model.UserPreferences
import eu.wedgess.mihole.data.model.UserPreferences.Theme

fun UserPreferences.Builder.defaultValues(): UserPreferences.Builder =
    this.setThemeValue(Theme.DARK_VALUE)
        .setDefaultLogEntries(500)
        .setUseDynamicColors(false)
        .setRefreshTime(10_000)
        .setChangeStatusOnAllConnection(false)