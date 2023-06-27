package eu.wedgess.mihole.data

import eu.wedgess.mihole.data.api.PiHoleApi
import eu.wedgess.mihole.data.db.MiHolesDao
import eu.wedgess.mihole.data.model.MiHolesInfo
import eu.wedgess.mihole.data.toMiHole
import eu.wedgess.mihole.utils.DispatcherProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PiHoleRepository @Inject constructor(
    private val api: PiHoleApi,
    private val dao: MiHolesDao,
    private val dispatcherProvider: DispatcherProvider
) {
    private val currentPiHole = dao.fetchActive()

    suspend fun fetchStatusSummary() = withContext(dispatcherProvider.io) {
        api.fetchStatusSummary(currentPiHole.first())
    }

    suspend fun fetchOverTimeData() = withContext(dispatcherProvider.io) {
        api.fetchOverTimeData10Minutes(currentPiHole.first())
    }

    suspend fun fetchOverTimeDataClients() = withContext(dispatcherProvider.io) {
        api.fetchOverTimeDataClients(currentPiHole.first())
    }

    suspend fun fetchStatistics() = withContext(dispatcherProvider.io) {
        api.fetchStatistics(currentPiHole.first())
    }

    suspend fun insertMiHole(miHolesInfo: MiHolesInfo) = withContext(dispatcherProvider.io) {
        return@withContext kotlin.runCatching {
            dao.insert(miHolesInfo.toMiHole())
        }
    }

    suspend fun fetchAll(): Result<List<MiHolesInfo>> = withContext(dispatcherProvider.io) {
        return@withContext kotlin.runCatching {
            dao.fetchAll()
        }
    }

}