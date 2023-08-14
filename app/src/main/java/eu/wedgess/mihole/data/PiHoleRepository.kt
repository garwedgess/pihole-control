package eu.wedgess.mihole.data

import androidx.datastore.core.DataStore
import eu.wedgess.mihole.data.api.PiHoleApi
import eu.wedgess.mihole.data.db.MiHolesDao
import eu.wedgess.mihole.data.model.MiHolesInfo
import eu.wedgess.mihole.data.model.PiHoleFilterRules
import eu.wedgess.mihole.data.model.PiHoleStatusResponse
import eu.wedgess.mihole.data.model.ResponseResult
import eu.wedgess.mihole.data.model.UserPreferences
import eu.wedgess.mihole.data.model.UserPreferences.Theme
import eu.wedgess.mihole.data.model.enums.FilterRuleType
import eu.wedgess.mihole.data.model.enums.PiHoleStatus
import eu.wedgess.mihole.utils.DispatcherProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class PiHoleRepository @Inject constructor(
    private val api: PiHoleApi,
    private val dao: MiHolesDao,
    private val userPreferences: DataStore<UserPreferences>,
    private val dispatcherProvider: DispatcherProvider
) {
    private val currentPiHole = dao.fetchActive()

    suspend fun fetchStatus() = withContext(dispatcherProvider.io) {
        api.fetchStatus(currentPiHole.first())
    }

    suspend fun fetchStatusSummary() = withContext(dispatcherProvider.io) {
        api.fetchStatusSummary(currentPiHole.first())
    }

    suspend fun fetchOverTimeData() = withContext(dispatcherProvider.io) {
        api.fetchOverTimeData10Minutes(currentPiHole.first())
    }

    suspend fun fetchOverTimeDataClients() = withContext(dispatcherProvider.io) {
        api.fetchOverTimeDataClients(currentPiHole.first())
    }

    suspend fun fetchStatistics() = withContext(dispatcherProvider.io) {
        api.fetchStatistics(currentPiHole.first())
    }

    suspend fun fetchFilterRules() =
        withContext(dispatcherProvider.io) {
            val result = FilterRuleType.values().map {
                async { api.fetchFilterRules(currentPiHole.first(), it) }
            }.awaitAll()

            return@withContext result
                .filterIsInstance<ResponseResult.Error<*>>(ResponseResult.Error::class.java)
                .firstOrNull<ResponseResult.Error<*>>()
                ?: ResponseResult.Success(result
                    .filterIsInstance(ResponseResult.Success::class.java)
                    .flatMap { (it.data as PiHoleFilterRules).rulesList }
                )
        }

    suspend fun addFilterRules(rule: String, filterRuleType: FilterRuleType) =
        withContext(dispatcherProvider.io) {
            api.addFilterRule(currentPiHole.first(), rule, filterRuleType)
        }

    suspend fun removeFilterRules(rule: String, filterRuleType: FilterRuleType) =
        withContext(dispatcherProvider.io) {
            api.removeFilterRule(currentPiHole.first(), rule, filterRuleType)
        }

    suspend fun fetchLogs(limit: Int) =
        withContext(dispatcherProvider.io) {
            api.fetchLogs(currentPiHole.first(), limit)
        }

    suspend fun disableAdBlocking(duration: Long) =
        withContext(dispatcherProvider.io) {
            return@withContext if (userPreferences.data.first().changeStatusOnAllConnection) {
                fetchAll().onSuccess {
                    it.forEach { connection ->
                        disableAdBlocking(connection, duration)
                    }
                }
                ResponseResult.Success(PiHoleStatusResponse(PiHoleStatus.DISABLED))
            } else {
                disableAdBlocking(currentPiHole.first(), duration)
            }
        }

    private suspend fun disableAdBlocking(connection: MiHolesInfo, duration: Long) = withContext(dispatcherProvider.io) {
        api.disableAdBlocking(connection, duration.toDuration(DurationUnit.MILLISECONDS))
    }

    suspend fun enableAdBlocking() =
        withContext(dispatcherProvider.io) {
            return@withContext if (userPreferences.data.first().changeStatusOnAllConnection) {
                fetchAll().onSuccess {
                    it.forEach { connection ->
                        enableAdBlocking(connection)
                    }
                }
                ResponseResult.Success(PiHoleStatusResponse(PiHoleStatus.ENABLED))
            } else {
                enableAdBlocking(currentPiHole.first())
            }
        }

    private suspend fun enableAdBlocking(connection: MiHolesInfo) = withContext(dispatcherProvider.io) {
        api.enableAdBlocking(connection)
    }

    suspend fun insertMiHole(miHolesInfo: MiHolesInfo) = withContext(dispatcherProvider.io) {
        return@withContext kotlin.runCatching {
            dao.insert(miHolesInfo.toMiHole())
        }
    }

    suspend fun setConnectionAsActive(miHolesInfo: MiHolesInfo) =
        withContext(dispatcherProvider.io) {
            return@withContext kotlin.runCatching {
                dao.setActive(miHolesInfo.id)
            }
        }

    suspend fun fetchAll(): Result<List<MiHolesInfo>> = withContext(dispatcherProvider.io) {
        return@withContext kotlin.runCatching {
            dao.fetchAll().firstOrNull() ?: emptyList<MiHolesInfo>()
        }
    }

    suspend fun fetchAllFlow(): Flow<List<MiHolesInfo>> = withContext(dispatcherProvider.io) {
        return@withContext dao.fetchAll()
    }

    suspend fun fetchById(id: Long): Result<MiHolesInfo?> = withContext(dispatcherProvider.io) {
        return@withContext kotlin.runCatching {
            dao.fetchById(id)
        }
    }

    suspend fun fetchActive(): Result<MiHolesInfo> = withContext(dispatcherProvider.io) {
        return@withContext kotlin.runCatching {
            dao.fetchActive().firstOrNull() ?: MiHolesInfo.default
        }
    }

    suspend fun fetchActiveFlow(): Flow<MiHolesInfo> = withContext(dispatcherProvider.io) {
        return@withContext dao.fetchActive()
    }

    fun fetchUserPreferences() = userPreferences.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Timber.e("Error reading sort order preferences.", exception)
                emit(UserPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }

    suspend fun updateSelectedTheme(theme: Theme) = kotlin.runCatching {
        userPreferences.updateData { preferences ->
            preferences.toBuilder().setTheme(theme).build()
        }
    }

    suspend fun updateDynamicTheme(useDynamicTheme: Boolean) = kotlin.runCatching {
        userPreferences.updateData { preferences ->
            preferences.toBuilder().setUseDynamicColors(useDynamicTheme).build()
        }
    }

    suspend fun updateRefreshInterval(interval: Long) = kotlin.runCatching {
        userPreferences.updateData { preferences ->
            preferences.toBuilder().setRefreshTime(interval).build()
        }
    }

    suspend fun updateStatusChangeOnAllConnections(applyOnAll: Boolean) = kotlin.runCatching {
        userPreferences.updateData { preferences ->
            preferences.toBuilder().setChangeStatusOnAllConnection(applyOnAll).build()
        }
    }
}