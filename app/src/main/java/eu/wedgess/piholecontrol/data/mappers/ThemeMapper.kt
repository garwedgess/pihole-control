package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.UserPreferences.Theme
import eu.wedgess.piholecontrol.domain.model.AppThemeEntity

fun Theme?.toEntity(): AppThemeEntity = when (this) {
    null,
    Theme.SYSTEM,
    Theme.UNRECOGNIZED -> AppThemeEntity.SYSTEM

    Theme.DARK -> AppThemeEntity.DARK
    Theme.LIGHT -> AppThemeEntity.LIGHT
}
