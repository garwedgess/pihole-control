package eu.wedgess.piholecontrol.domain.repository

import eu.wedgess.piholecontrol.domain.model.AppPreferencesEntity
import eu.wedgess.piholecontrol.domain.model.AppThemeEntity
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun fetchAllPreferences(): Flow<AppPreferencesEntity>
    fun getRefreshInterval(): Flow<Long>
    fun changeStatusOnAllConnection(): Flow<Boolean>
    suspend fun updateSelectedTheme(theme: AppThemeEntity): Result<Unit>
    suspend fun updateDynamicTheme(useDynamicTheme: Boolean): Result<Unit>
    suspend fun updateRefreshInterval(interval: Long): Result<Unit>
    suspend fun updateStatusChangeOnAllConnections(applyOnAll: Boolean): Result<Unit>
}