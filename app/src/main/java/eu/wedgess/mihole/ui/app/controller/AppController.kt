package eu.wedgess.mihole.ui.app.controller

import androidx.datastore.core.DataStore
import eu.wedgess.mihole.data.api.PiHoleApi
import eu.wedgess.mihole.data.db.MiHolesDao
import eu.wedgess.mihole.data.model.PiHoleInfo
import eu.wedgess.mihole.data.model.UserPreferences
import eu.wedgess.mihole.data.model.enums.PiHoleStatus
import eu.wedgess.mihole.data.model.responses.PiHoleStatusResponse
import eu.wedgess.mihole.data.toPiHoleInfo
import eu.wedgess.mihole.ui.app.model.PiHoleAppInfo
import eu.wedgess.mihole.ui.base.RefreshableController
import eu.wedgess.mihole.utils.DispatcherProvider
import eu.wedgess.mihole.utils.extensions.resultOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class AppController @Inject constructor(
    private val piHolesDao: MiHolesDao,
    private val api: PiHoleApi,
    private val preferences: DataStore<UserPreferences>,
    private val dispatcherProvider: DispatcherProvider
) : RefreshableController<PiHoleStatusResponse>(piHolesDao, preferences) {

    override suspend fun fetchRefreshableData(activePiHoleInfo: PiHoleInfo): PiHoleStatusResponse {
        return api.fetchStatus(activePiHoleInfo).onFailure {
            Timber.e(
                "Failed to check the active connections blocking status, defaulting to unknown", it
            )
        }.getOrNull() ?: PiHoleStatusResponse(PiHoleStatus.UNKNOWN)
    }

    fun fetchAppInfo() = combine(
        piHolesDao.fetchActiveFlow(),
        piHolesDao.fetchAllAsFlow(),
        preferences.data,
        listenToDataChanges()
    ) { activeConnection, allConnections, preferences, status ->
        val activePiHole = activeConnection?.toPiHoleInfo() ?: PiHoleInfo.default

        PiHoleAppInfo(
            currentConnection = activePiHole,
            connections = allConnections.map { it.toPiHoleInfo() },
            status = status.getOrNull()?.status ?: PiHoleStatus.UNKNOWN,
            refreshInterval = preferences.refreshTime
        )
    }

    suspend fun setConnectionAsActive(connectionId: Long) = withContext(dispatcherProvider.io) {
        resultOf { piHolesDao.setActive(connectionId) }
    }

    suspend fun disableAdBlocking(duration: Long) =
        withContext(dispatcherProvider.io) {
            resultOf {
                if (preferences.data.first().changeStatusOnAllConnection) {
                    piHolesDao.fetchAll()
                        .map { it.toPiHoleInfo() }
                        .forEach { connection ->
                            disableAdBlocking(connection, duration)
                        }
                } else {
                    disableAdBlocking(piHolesDao.fetchActive().toPiHoleInfo(), duration)
                }
                delay(500)
                triggerRefresh()
            }
        }

    private suspend fun disableAdBlocking(connection: PiHoleInfo, duration: Long) =
        withContext(dispatcherProvider.io) {
            resultOf {
                api.disableAdBlocking(connection, duration.toDuration(DurationUnit.MILLISECONDS))
            }
        }

    suspend fun enableAdBlocking() =
        withContext(dispatcherProvider.io) {
            resultOf {
                if (preferences.data.first().changeStatusOnAllConnection) {
                    piHolesDao.fetchAll()
                        .map { it.toPiHoleInfo() }
                        .forEach { connection ->
                            enableAdBlocking(connection)
                        }
                } else {
                    enableAdBlocking(piHolesDao.fetchActive().toPiHoleInfo())
                }
                delay(500)
                triggerRefresh()
            }
        }

    private suspend fun enableAdBlocking(connection: PiHoleInfo) =
        withContext(dispatcherProvider.io) {
            api.enableAdBlocking(connection)
        }

}