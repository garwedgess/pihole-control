package eu.wedgess.piholecontrol.ui.logs

import androidx.datastore.core.DataStore
import eu.wedgess.piholecontrol.data.api.PiHoleApi
import eu.wedgess.piholecontrol.data.db.ConnectionDao
import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.data.model.UserPreferences
import eu.wedgess.piholecontrol.data.model.responses.PiHoleLogsResponse
import eu.wedgess.piholecontrol.ui.base.RefreshableController
import eu.wedgess.piholecontrol.ui.filters.controller.FiltersController
import eu.wedgess.piholecontrol.ui.filters.controller.FiltersControllerImpl
import eu.wedgess.piholecontrol.ui.logs.model.LogEntryStatus
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LogsController @Inject constructor(
    private val api: PiHoleApi,
    dao: ConnectionDao,
    userPreferences: DataStore<UserPreferences>,
    private val dispatcherProvider: DispatcherProvider
) : RefreshableController<PiHoleLogsResponse>(dao, userPreferences),
    FiltersController by FiltersControllerImpl(api, dao, dispatcherProvider) {

    private var limit: Int = 500
    private var statusFilter: LogEntryStatus = LogEntryStatus.ALL

    fun logs(): Flow<Result<PiHoleLogsResponse>> = listenToDataChanges()

    private suspend fun fetchPiHoleLogs(activeConnectionInfo: ConnectionInfo, limit: Int) =
        withContext(dispatcherProvider.io) {
            api.fetchLogs(activeConnectionInfo, limit)
        }


    override suspend fun fetchRefreshableData(activeConnectionInfo: ConnectionInfo): PiHoleLogsResponse {
        return fetchPiHoleLogs(activeConnectionInfo, limit)
            .onSuccess { logsResponse ->
                return logsResponse.applyStatusFilter(statusFilter)
            }.getOrThrow()
    }

    fun setLimit(limit: Int) {
        this.limit = limit
        triggerRefresh()
    }

    fun setStatusFilter(status: LogEntryStatus) {
        this.statusFilter = status
        triggerRefresh()
    }


}