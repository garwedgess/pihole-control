package eu.wedgess.piholecontrol.data.repository

import androidx.datastore.core.DataStore
import eu.wedgess.piholecontrol.data.model.UserPreferences
import eu.wedgess.piholecontrol.data.model.UserPreferences.Theme
import eu.wedgess.piholecontrol.domain.model.AppPreferencesEntity
import eu.wedgess.piholecontrol.domain.model.AppThemeEntity
import eu.wedgess.piholecontrol.domain.repository.SettingsRepository
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import eu.wedgess.piholecontrol.utils.extensions.runWithErrorHandling
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SettingsRepositoryImpl(
    private val preferences: DataStore<UserPreferences>,
    private val dispatcherProvider: DispatcherProvider
) : SettingsRepository {

    override fun fetchAllPreferences(): Flow<AppPreferencesEntity> {
        return preferences.data.map {
            AppPreferencesEntity(
                theme = when (it.theme) {
                    null,
                    Theme.SYSTEM,
                    Theme.UNRECOGNIZED -> AppThemeEntity.SYSTEM

                    Theme.DARK -> AppThemeEntity.DARK
                    Theme.LIGHT -> AppThemeEntity.LIGHT
                },
                useDynamicColors = it.useDynamicColors,
                refreshInterval = it.refreshTime,
                multiStatusChange = it.changeStatusOnAllConnection
            )
        }
    }

    override fun getRefreshInterval(): Flow<Long> {
        return preferences.data.map { it.refreshTime }
    }

    override fun changeStatusOnAllConnection(): Flow<Boolean> {
        return preferences.data.map { it.changeStatusOnAllConnection }
    }

    override suspend fun updateSelectedTheme(theme: AppThemeEntity): Result<Unit> =
        withContext(dispatcherProvider.io) {
            runWithErrorHandling {
                preferences.updateData { preferences ->
                    preferences.toBuilder().setTheme(Theme.entries.first { it.number == theme.key })
                        .build()
                }
            }
        }

    override suspend fun updateDynamicTheme(useDynamicTheme: Boolean): Result<Unit> =
        withContext(dispatcherProvider.io) {
            runWithErrorHandling {
                preferences.updateData { preferences ->
                    preferences.toBuilder().setUseDynamicColors(useDynamicTheme).build()
                }
            }
        }

    override suspend fun updateRefreshInterval(interval: Long): Result<Unit> =
        withContext(dispatcherProvider.io) {
            runWithErrorHandling {
                preferences.updateData { preferences ->
                    preferences.toBuilder().setRefreshTime(interval).build()
                }
            }
        }

    override suspend fun updateStatusChangeOnAllConnections(applyOnAll: Boolean): Result<Unit> =
        withContext(dispatcherProvider.io) {
            runWithErrorHandling {
                preferences.updateData { preferences ->
                    preferences.toBuilder().setChangeStatusOnAllConnection(applyOnAll).build()
                }
            }
        }
}
