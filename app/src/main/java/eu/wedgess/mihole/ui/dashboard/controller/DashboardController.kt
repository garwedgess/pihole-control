package eu.wedgess.mihole.ui.dashboard.controller

import androidx.datastore.core.DataStore
import eu.wedgess.mihole.data.api.PiHoleApi
import eu.wedgess.mihole.data.db.MiHolesDao
import eu.wedgess.mihole.data.model.responses.PiHoleClientsOverTimeData
import eu.wedgess.mihole.data.model.PiHoleInfo
import eu.wedgess.mihole.data.model.responses.PiHoleOverTimeData
import eu.wedgess.mihole.data.model.responses.PiHoleSummary
import eu.wedgess.mihole.data.model.UserPreferences
import eu.wedgess.mihole.ui.base.RefreshableController
import eu.wedgess.mihole.ui.dashboard.model.DashboardInfo
import eu.wedgess.mihole.utils.DispatcherProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DashboardController @Inject constructor(
    private val api: PiHoleApi,
    dao: MiHolesDao,
    userPreferences: DataStore<UserPreferences>,
    private val dispatcherProvider: DispatcherProvider
) : RefreshableController<DashboardInfo>(dao, userPreferences) {

    fun dashboardInfo(): Flow<Result<DashboardInfo>> = listenToDataChanges()

    private suspend fun fetchSummary(activePiHoleInfo: PiHoleInfo): Result<PiHoleSummary> =
        withContext(dispatcherProvider.io) {
            api.fetchStatusSummary(activePiHoleInfo)
        }

    private suspend fun fetchOverTimeData(activePiHoleInfo: PiHoleInfo): Result<PiHoleOverTimeData> =
        withContext(dispatcherProvider.io) {
            api.fetchOverTimeData10Minutes(activePiHoleInfo)
        }

    private suspend fun fetchOverTimeDataClients(activePiHoleInfo: PiHoleInfo): Result<PiHoleClientsOverTimeData> =
        withContext(dispatcherProvider.io) {
            api.fetchOverTimeDataClients(activePiHoleInfo)
        }


    override suspend fun fetchRefreshableData(activePiHoleInfo: PiHoleInfo) = supervisorScope {
        val deferredSummary = async { fetchSummary(activePiHoleInfo) }
        val deferredOverTimeData = async { fetchOverTimeData(activePiHoleInfo) }
        val deferredClientsOverTimeData = async { fetchOverTimeDataClients(activePiHoleInfo) }

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