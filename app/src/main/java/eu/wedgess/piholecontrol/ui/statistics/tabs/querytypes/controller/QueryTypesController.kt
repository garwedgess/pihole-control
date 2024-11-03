package eu.wedgess.piholecontrol.ui.statistics.tabs.querytypes.controller

import androidx.datastore.core.DataStore
import eu.wedgess.piholecontrol.data.api.PiHoleApi
import eu.wedgess.piholecontrol.data.db.ConnectionDao
import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.data.model.responses.PiHoleQueryTypes
import eu.wedgess.piholecontrol.data.model.responses.QueryTypes
import eu.wedgess.piholecontrol.data.model.UserPreferences
import eu.wedgess.piholecontrol.ui.base.RefreshableController
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class QueryTypesController @Inject constructor(
    private val api: PiHoleApi,
    dao: ConnectionDao,
    userPreferences: DataStore<UserPreferences>,
    private val dispatcherProvider: DispatcherProvider
) : RefreshableController<QueryTypes>(dao, userPreferences) {

    fun queryTypes(): Flow<Result<QueryTypes>> = listenToDataChanges()

    private suspend fun fetchQueryTypes(activeConnectionInfo: ConnectionInfo): Result<PiHoleQueryTypes> =
        withContext(dispatcherProvider.io) {
            api.fetchQueryTypes(activeConnectionInfo)
        }


    override suspend fun fetchRefreshableData(activeConnectionInfo: ConnectionInfo): QueryTypes =
        fetchQueryTypes(activeConnectionInfo).getOrThrow().queryTypes
}