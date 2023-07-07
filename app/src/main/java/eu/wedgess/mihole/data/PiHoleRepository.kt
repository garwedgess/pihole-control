package eu.wedgess.mihole.data

import eu.wedgess.mihole.data.api.PiHoleApi
import eu.wedgess.mihole.data.db.MiHolesDao
import eu.wedgess.mihole.data.model.MiHolesInfo
import eu.wedgess.mihole.data.model.PiHoleFilterRules
import eu.wedgess.mihole.data.model.ResponseResult
import eu.wedgess.mihole.data.model.enums.FilterRuleType
import eu.wedgess.mihole.utils.DispatcherProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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

    suspend fun fetchFilterRules() =
        withContext(dispatcherProvider.io) {
            val result = FilterRuleType.values().map {
                async { api.fetchFilterRules(currentPiHole.first(), it) }
            }.awaitAll()

            return@withContext result
                .filterIsInstance<ResponseResult.Error<*>>(ResponseResult.Error::class.java)
                .firstOrNull<ResponseResult.Error<*>>()
                ?: ResponseResult.Success(result
                    .filterIsInstance(ResponseResult.Success::class.java)
                    .flatMap { (it.data as PiHoleFilterRules).rulesList }
                )
        }

    suspend fun addFilterRules(rule: String, filterRuleType: FilterRuleType) =
        withContext(dispatcherProvider.io) {
            api.addFilterRule(currentPiHole.first(), rule, filterRuleType)
        }

    suspend fun removeFilterRules(rule: String, filterRuleType: FilterRuleType) =
        withContext(dispatcherProvider.io) {
            api.removeFilterRule(currentPiHole.first(), rule, filterRuleType)
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