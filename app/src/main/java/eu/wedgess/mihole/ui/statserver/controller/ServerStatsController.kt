package eu.wedgess.mihole.ui.statserver.controller

import androidx.datastore.core.DataStore
import eu.wedgess.mihole.data.api.PiHoleApi
import eu.wedgess.mihole.data.db.MiHolesDao
import eu.wedgess.mihole.data.model.responses.PiHoleForwardDestinations
import eu.wedgess.mihole.data.model.PiHoleInfo
import eu.wedgess.mihole.data.model.UserPreferences
import eu.wedgess.mihole.ui.base.RefreshableController
import eu.wedgess.mihole.utils.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ServerStatsController @Inject constructor(
    private val api: PiHoleApi,
    dao: MiHolesDao,
    userPreferences: DataStore<UserPreferences>,
    private val dispatcherProvider: DispatcherProvider
) : RefreshableController<PiHoleForwardDestinations>(dao, userPreferences) {

    fun forwardDestinations(): Flow<Result<PiHoleForwardDestinations>> = listenToDataChanges()

    private suspend fun fetchForwardDestinations(activePiHoleInfo: PiHoleInfo): Result<PiHoleForwardDestinations> =
        withContext(dispatcherProvider.io) {
            api.fetchForwardDestinations(activePiHoleInfo)
        }


    override suspend fun fetchData(activePiHoleInfo: PiHoleInfo): PiHoleForwardDestinations =
        fetchForwardDestinations(activePiHoleInfo).getOrThrow()
}