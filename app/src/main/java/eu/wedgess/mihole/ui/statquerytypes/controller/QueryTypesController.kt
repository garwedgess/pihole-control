package eu.wedgess.mihole.ui.statquerytypes.controller

import androidx.datastore.core.DataStore
import eu.wedgess.mihole.data.api.PiHoleApi
import eu.wedgess.mihole.data.db.MiHolesDao
import eu.wedgess.mihole.data.model.PiHoleInfo
import eu.wedgess.mihole.data.model.responses.PiHoleQueryTypes
import eu.wedgess.mihole.data.model.responses.QueryTypes
import eu.wedgess.mihole.data.model.UserPreferences
import eu.wedgess.mihole.ui.base.RefreshableController
import eu.wedgess.mihole.utils.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class QueryTypesController @Inject constructor(
    private val api: PiHoleApi,
    dao: MiHolesDao,
    userPreferences: DataStore<UserPreferences>,
    private val dispatcherProvider: DispatcherProvider
) : RefreshableController<QueryTypes>(dao, userPreferences) {

    fun queryTypes(): Flow<Result<QueryTypes>> = listenToDataChanges()

    private suspend fun fetchQueryTypes(activePiHoleInfo: PiHoleInfo): Result<PiHoleQueryTypes> =
        withContext(dispatcherProvider.io) {
            api.fetchQueryTypes(activePiHoleInfo)
        }


    override suspend fun fetchRefreshableData(activePiHoleInfo: PiHoleInfo): QueryTypes =
        fetchQueryTypes(activePiHoleInfo).getOrThrow().queryTypes
}