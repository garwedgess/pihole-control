package eu.wedgess.piholecontrol.ui.statserver.controller

import androidx.datastore.core.DataStore
import eu.wedgess.piholecontrol.data.api.PiHoleApi
import eu.wedgess.piholecontrol.data.db.ConnectionDao
import eu.wedgess.piholecontrol.data.model.responses.PiHoleForwardDestinations
import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.data.model.UserPreferences
import eu.wedgess.piholecontrol.ui.base.RefreshableController
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ServerStatsController @Inject constructor(
    private val api: PiHoleApi,
    dao: ConnectionDao,
    userPreferences: DataStore<UserPreferences>,
    private val dispatcherProvider: DispatcherProvider
) : RefreshableController<PiHoleForwardDestinations>(dao, userPreferences) {

    fun forwardDestinations(): Flow<Result<PiHoleForwardDestinations>> = listenToDataChanges()

    private suspend fun fetchForwardDestinations(activeConnectionInfo: ConnectionInfo): Result<PiHoleForwardDestinations> =
        withContext(dispatcherProvider.io) {
            api.fetchForwardDestinations(activeConnectionInfo)
        }


    override suspend fun fetchRefreshableData(activeConnectionInfo: ConnectionInfo): PiHoleForwardDestinations =
        fetchForwardDestinations(activeConnectionInfo).getOrThrow()
}