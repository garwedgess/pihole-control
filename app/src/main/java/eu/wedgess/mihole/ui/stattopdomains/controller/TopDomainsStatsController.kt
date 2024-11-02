package eu.wedgess.mihole.ui.stattopdomains.controller

import androidx.datastore.core.DataStore
import eu.wedgess.mihole.data.api.PiHoleApi
import eu.wedgess.mihole.data.db.MiHolesDao
import eu.wedgess.mihole.data.model.PiHoleInfo
import eu.wedgess.mihole.data.model.responses.PiHoleTopQueries
import eu.wedgess.mihole.data.model.UserPreferences
import eu.wedgess.mihole.ui.base.RefreshableController
import eu.wedgess.mihole.utils.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class TopDomainsStatsController @Inject constructor(
    private val api: PiHoleApi,
    dao: MiHolesDao,
    userPreferences: DataStore<UserPreferences>,
    private val dispatcherProvider: DispatcherProvider
) : RefreshableController<PiHoleTopQueries>(dao, userPreferences) {

    fun topQueries(): Flow<Result<PiHoleTopQueries>> = listenToDataChanges()

    private suspend fun fetchTopQueries(activePiHoleInfo: PiHoleInfo): Result<PiHoleTopQueries> =
        withContext(dispatcherProvider.io) {
            api.fetchTopQueries(activePiHoleInfo)
        }

    override suspend fun fetchRefreshableData(activePiHoleInfo: PiHoleInfo): PiHoleTopQueries =
        fetchTopQueries(activePiHoleInfo).getOrThrow()
}