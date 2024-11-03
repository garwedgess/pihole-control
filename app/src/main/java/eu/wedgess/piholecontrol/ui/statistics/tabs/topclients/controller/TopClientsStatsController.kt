package eu.wedgess.piholecontrol.ui.statistics.tabs.topclients.controller

import androidx.datastore.core.DataStore
import eu.wedgess.piholecontrol.data.api.PiHoleApi
import eu.wedgess.piholecontrol.data.db.ConnectionDao
import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopClients
import eu.wedgess.piholecontrol.data.model.UserPreferences
import eu.wedgess.piholecontrol.ui.base.RefreshableController
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class TopClientsStatsController @Inject constructor(
    private val api: PiHoleApi,
    dao: ConnectionDao,
    userPreferences: DataStore<UserPreferences>,
    private val dispatcherProvider: DispatcherProvider
) : RefreshableController<PiHoleTopClients>(dao, userPreferences) {

    fun topClients(): Flow<Result<PiHoleTopClients>> = listenToDataChanges()

    private suspend fun fetchTopClients(activeConnectionInfo: ConnectionInfo): Result<PiHoleTopClients> =
        withContext(dispatcherProvider.io) {
            api.fetchTopClients(activeConnectionInfo)
        }

    override suspend fun fetchRefreshableData(activeConnectionInfo: ConnectionInfo): PiHoleTopClients =
        fetchTopClients(activeConnectionInfo).getOrThrow()
}