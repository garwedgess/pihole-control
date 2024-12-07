package eu.wedgess.piholecontrol.domain.model

data class AppPreferencesEntity(
    val theme: AppThemeEntity,
    val useDynamicColors: Boolean,
    val refreshInterval: Long,
    val multiStatusChange: Boolean
)
