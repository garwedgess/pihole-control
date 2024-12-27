package eu.wedgess.piholecontrol.domain.model

data class AppPreferencesEntity(
    val theme: AppThemeEntity,
    val useDynamicColors: Boolean,
    val refreshInterval: Long,
    val multiStatusChange: Boolean
) {
    companion object {
        val default = AppPreferencesEntity(
            theme = AppThemeEntity.DARK,
            useDynamicColors = false,
            refreshInterval = 1000L,
            multiStatusChange = true
        )
    }
}
