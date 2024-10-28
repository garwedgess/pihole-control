package eu.wedgess.mihole.ui.filters.controller

import eu.wedgess.mihole.data.api.PiHoleApi
import eu.wedgess.mihole.data.db.MiHolesDao
import eu.wedgess.mihole.data.model.enums.FilterRuleType
import eu.wedgess.mihole.data.toMiHoleInfo
import eu.wedgess.mihole.utils.DispatcherProvider
import eu.wedgess.mihole.utils.extensions.resultOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FiltersController @Inject constructor(
    private val api: PiHoleApi,
    private val dao: MiHolesDao,
    private val dispatcherProvider: DispatcherProvider
) {

    suspend fun addFilterRule(rule: String, type: FilterRuleType) =
        withContext(dispatcherProvider.io) {
            resultOf {
                val activePiHole = dao.fetchActive().toMiHoleInfo()
                api.addFilterRule(activePiHole, rule, type)
            }
        }

    suspend fun removeFilterRule(rule: String, type: FilterRuleType) =
        withContext(dispatcherProvider.io) {
            resultOf {
                val activePiHole = dao.fetchActive().toMiHoleInfo()
                api.removeFilterRule(activePiHole, rule, type)
            }
        }
}