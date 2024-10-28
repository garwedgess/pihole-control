package eu.wedgess.mihole.ui.stattopclients.controller

import androidx.datastore.core.DataStore
import eu.wedgess.mihole.data.api.PiHoleApi
import eu.wedgess.mihole.data.db.MiHolesDao
import eu.wedgess.mihole.data.model.PiHoleInfo
import eu.wedgess.mihole.data.model.responses.PiHoleTopClients
import eu.wedgess.mihole.data.model.UserPreferences
import eu.wedgess.mihole.ui.base.RefreshableController
import eu.wedgess.mihole.utils.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class TopClientsStatsController @Inject constructor(
    private val api: PiHoleApi,
    dao: MiHolesDao,
    userPreferences: DataStore<UserPreferences>,
    private val dispatcherProvider: DispatcherProvider
) : RefreshableController<PiHoleTopClients>(dao, userPreferences) {

    fun topClients(): Flow<Result<PiHoleTopClients>> = listenToDataChanges()

    private suspend fun fetchTopClients(activePiHoleInfo: PiHoleInfo): Result<PiHoleTopClients> =
        withContext(dispatcherProvider.io) {
            api.fetchTopClients(activePiHoleInfo)
        }

    override suspend fun fetchData(activePiHoleInfo: PiHoleInfo): PiHoleTopClients =
        fetchTopClients(activePiHoleInfo).getOrThrow()
}