package eu.wedgess.piholecontrol.ui.statistics.tabs.topdomains.controller

import androidx.datastore.core.DataStore
import eu.wedgess.piholecontrol.data.api.PiHoleApi
import eu.wedgess.piholecontrol.data.db.ConnectionDao
import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopQueries
import eu.wedgess.piholecontrol.data.model.UserPreferences
import eu.wedgess.piholecontrol.ui.base.RefreshableController
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class TopDomainsStatsController @Inject constructor(
    private val api: PiHoleApi,
    dao: ConnectionDao,
    userPreferences: DataStore<UserPreferences>,
    private val dispatcherProvider: DispatcherProvider
) : RefreshableController<PiHoleTopQueries>(dao, userPreferences) {

    fun topQueries(): Flow<Result<PiHoleTopQueries>> = listenToDataChanges()

    private suspend fun fetchTopQueries(activeConnectionInfo: ConnectionInfo): Result<PiHoleTopQueries> =
        withContext(dispatcherProvider.io) {
            api.fetchTopQueries(activeConnectionInfo)
        }

    override suspend fun fetchRefreshableData(activeConnectionInfo: ConnectionInfo): PiHoleTopQueries =
        fetchTopQueries(activeConnectionInfo).getOrThrow()
}