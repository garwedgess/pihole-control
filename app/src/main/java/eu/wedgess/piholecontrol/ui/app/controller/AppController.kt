package eu.wedgess.piholecontrol.ui.app.controller

import androidx.datastore.core.DataStore
import eu.wedgess.piholecontrol.data.api.PiHoleApi
import eu.wedgess.piholecontrol.data.db.ConnectionDao
import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.data.model.UserPreferences
import eu.wedgess.piholecontrol.data.model.enums.PiHoleStatus
import eu.wedgess.piholecontrol.data.model.responses.PiHoleStatusResponse
import eu.wedgess.piholecontrol.data.toConnectionInfo
import eu.wedgess.piholecontrol.ui.app.model.PiHoleAppInfo
import eu.wedgess.piholecontrol.ui.base.RefreshableController
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import eu.wedgess.piholecontrol.utils.extensions.resultOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class AppController @Inject constructor(
    private val piHolesDao: ConnectionDao,
    private val api: PiHoleApi,
    private val preferences: DataStore<UserPreferences>,
    private val dispatcherProvider: DispatcherProvider
) : RefreshableController<PiHoleStatusResponse>(piHolesDao, preferences) {

    override suspend fun fetchRefreshableData(activeConnectionInfo: ConnectionInfo): PiHoleStatusResponse {
        return api.fetchStatus(activeConnectionInfo).onFailure {
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
        val activePiHole = activeConnection?.toConnectionInfo() ?: ConnectionInfo.default

        PiHoleAppInfo(
            currentConnection = activePiHole,
            connections = allConnections.map { it.toConnectionInfo() },
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
                        .map { it.toConnectionInfo() }
                        .forEach { connection ->
                            disableAdBlocking(connection, duration)
                        }
                } else {
                    disableAdBlocking(piHolesDao.fetchActive().toConnectionInfo(), duration)
                }
                delay(500)
                triggerRefresh()
            }
        }

    private suspend fun disableAdBlocking(connection: ConnectionInfo, duration: Long) =
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
                        .map { it.toConnectionInfo() }
                        .forEach { connection ->
                            enableAdBlocking(connection)
                        }
                } else {
                    enableAdBlocking(piHolesDao.fetchActive().toConnectionInfo())
                }
                delay(500)
                triggerRefresh()
            }
        }

    private suspend fun enableAdBlocking(connection: ConnectionInfo) =
        withContext(dispatcherProvider.io) {
            api.enableAdBlocking(connection)
        }

}