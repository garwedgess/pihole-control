package eu.wedgess.mihole.ui.settings.controller

import androidx.datastore.core.DataStore
import eu.wedgess.mihole.data.db.MiHolesDao
import eu.wedgess.mihole.data.model.UserPreferences
import eu.wedgess.mihole.data.model.UserPreferences.Theme
import eu.wedgess.mihole.utils.extensions.resultOf
import javax.inject.Inject

class SettingsController @Inject constructor(
    private val piHolesDao: MiHolesDao,
    private val userPreferences: DataStore<UserPreferences>
) {

    fun fetchActiveConnection() = piHolesDao.fetchActiveFlow()

    fun fetchUserPreferences() = userPreferences.data

    suspend fun updateSelectedTheme(theme: Theme) = resultOf {
        userPreferences.updateData { preferences ->
            preferences.toBuilder().setTheme(theme).build()
        }
    }

    suspend fun updateDynamicTheme(useDynamicTheme: Boolean) = resultOf {
        userPreferences.updateData { preferences ->
            preferences.toBuilder().setUseDynamicColors(useDynamicTheme).build()
        }
    }

    suspend fun updateRefreshInterval(interval: Long) = resultOf {
        userPreferences.updateData { preferences ->
            preferences.toBuilder().setRefreshTime(interval).build()
        }
    }

    suspend fun updateStatusChangeOnAllConnections(applyOnAll: Boolean) = resultOf {
        userPreferences.updateData { preferences ->
            preferences.toBuilder().setChangeStatusOnAllConnection(applyOnAll).build()
        }
    }
}