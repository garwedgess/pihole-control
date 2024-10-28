package eu.wedgess.mihole.ui.logs

import androidx.datastore.core.DataStore
import eu.wedgess.mihole.data.api.PiHoleApi
import eu.wedgess.mihole.data.db.MiHolesDao
import eu.wedgess.mihole.data.model.PiHoleInfo
import eu.wedgess.mihole.data.model.UserPreferences
import eu.wedgess.mihole.data.model.responses.PiHoleLogsResponse
import eu.wedgess.mihole.ui.base.RefreshableController
import eu.wedgess.mihole.utils.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LogsController @Inject constructor(
    private val api: PiHoleApi,
    dao: MiHolesDao,
    userPreferences: DataStore<UserPreferences>,
    private val dispatcherProvider: DispatcherProvider
) : RefreshableController<PiHoleLogsResponse>(
    dao, userPreferences
) {

    private var limit = 500

    fun setLimit(limit: Int) {
        this.limit = limit
        triggerRefresh()
    }

    fun logs(): Flow<Result<PiHoleLogsResponse>> = listenToDataChanges()

    private suspend fun fetchPiHoleLogs(activePiHoleInfo: PiHoleInfo, limit: Int) =
        withContext(dispatcherProvider.io) {
            api.fetchLogs(activePiHoleInfo, limit)
        }


    override suspend fun fetchData(activePiHoleInfo: PiHoleInfo): PiHoleLogsResponse {
        return fetchPiHoleLogs(activePiHoleInfo, limit).getOrThrow()
    }


}