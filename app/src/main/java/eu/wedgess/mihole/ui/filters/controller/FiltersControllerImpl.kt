package eu.wedgess.mihole.ui.filters.controller

import eu.wedgess.mihole.data.api.PiHoleApi
import eu.wedgess.mihole.data.db.MiHolesDao
import eu.wedgess.mihole.data.model.enums.FilterRuleType
import eu.wedgess.mihole.data.toPiHoleInfo
import eu.wedgess.mihole.utils.DispatcherProvider
import eu.wedgess.mihole.utils.extensions.resultOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FiltersControllerImpl @Inject constructor(
    private val api: PiHoleApi,
    private val dao: MiHolesDao,
    private val dispatcherProvider: DispatcherProvider
) : FiltersController {

    override suspend fun addFilterRule(rule: String, type: FilterRuleType) =
        withContext(dispatcherProvider.io) {
            resultOf {
                val activePiHole = dao.fetchActive().toPiHoleInfo()
                api.addFilterRule(activePiHole, rule, type).getOrThrow()
            }
        }

    override suspend fun removeFilterRule(rule: String, type: FilterRuleType) =
        withContext(dispatcherProvider.io) {
            resultOf {
                val activePiHole = dao.fetchActive().toPiHoleInfo()
                api.removeFilterRule(activePiHole, rule, type).getOrThrow()
            }
        }
}