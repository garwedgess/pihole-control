package eu.wedgess.piholecontrol.ui.filters.controller

import eu.wedgess.piholecontrol.data.api.PiHoleApi
import eu.wedgess.piholecontrol.data.db.ConnectionDao
import eu.wedgess.piholecontrol.data.model.enums.FilterRuleType
import eu.wedgess.piholecontrol.data.toConnectionInfo
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import eu.wedgess.piholecontrol.utils.extensions.resultOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FiltersControllerImpl @Inject constructor(
    private val api: PiHoleApi,
    private val dao: ConnectionDao,
    private val dispatcherProvider: DispatcherProvider
) : FiltersController {

    override suspend fun addFilterRule(rule: String, type: FilterRuleType) =
        withContext(dispatcherProvider.io) {
            resultOf {
                val activePiHole = dao.fetchActive().toConnectionInfo()
                api.addFilterRule(activePiHole, rule, type).getOrThrow()
            }
        }

    override suspend fun removeFilterRule(rule: String, type: FilterRuleType) =
        withContext(dispatcherProvider.io) {
            resultOf {
                val activePiHole = dao.fetchActive().toConnectionInfo()
                api.removeFilterRule(activePiHole, rule, type).getOrThrow()
            }
        }
}