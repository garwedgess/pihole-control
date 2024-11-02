package eu.wedgess.piholecontrol.ui.dashboard.controller

import androidx.datastore.core.DataStore
import eu.wedgess.piholecontrol.data.api.PiHoleApi
import eu.wedgess.piholecontrol.data.db.ConnectionDao
import eu.wedgess.piholecontrol.data.model.responses.PiHoleClientsOverTimeData
import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.data.model.responses.PiHoleOverTimeData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleSummary
import eu.wedgess.piholecontrol.data.model.UserPreferences
import eu.wedgess.piholecontrol.ui.base.RefreshableController
import eu.wedgess.piholecontrol.ui.dashboard.model.DashboardInfo
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DashboardController @Inject constructor(
    private val api: PiHoleApi,
    dao: ConnectionDao,
    userPreferences: DataStore<UserPreferences>,
    private val dispatcherProvider: DispatcherProvider
) : RefreshableController<DashboardInfo>(dao, userPreferences) {

    fun dashboardInfo(): Flow<Result<DashboardInfo>> = listenToDataChanges()

    private suspend fun fetchSummary(activeConnectionInfo: ConnectionInfo): Result<PiHoleSummary> =
        withContext(dispatcherProvider.io) {
            api.fetchStatusSummary(activeConnectionInfo)
        }

    private suspend fun fetchOverTimeData(activeConnectionInfo: ConnectionInfo): Result<PiHoleOverTimeData> =
        withContext(dispatcherProvider.io) {
            api.fetchOverTimeData10Minutes(activeConnectionInfo)
        }

    private suspend fun fetchOverTimeDataClients(activeConnectionInfo: ConnectionInfo): Result<PiHoleClientsOverTimeData> =
        withContext(dispatcherProvider.io) {
            api.fetchOverTimeDataClients(activeConnectionInfo)
        }


    override suspend fun fetchRefreshableData(activeConnectionInfo: ConnectionInfo) = supervisorScope {
        val deferredSummary = async { fetchSummary(activeConnectionInfo) }
        val deferredOverTimeData = async { fetchOverTimeData(activeConnectionInfo) }
        val deferredClientsOverTimeData = async { fetchOverTimeDataClients(activeConnectionInfo) }

        val summaryResult = deferredSummary.await()
        val overTimeDataResult = deferredOverTimeData.await()
        val clientsOverTimeDataResult = deferredClientsOverTimeData.await()

        return@supervisorScope DashboardInfo(
            summaryResult,
            overTimeDataResult,
            clientsOverTimeDataResult
        )
    }
}