package eu.wedgess.piholecontrol.ui.filters.tab.controller

import androidx.annotation.VisibleForTesting
import androidx.datastore.core.DataStore
import eu.wedgess.piholecontrol.data.api.PiHoleApi
import eu.wedgess.piholecontrol.data.db.ConnectionDao
import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.data.model.UserPreferences
import eu.wedgess.piholecontrol.data.model.enums.FilterRuleType
import eu.wedgess.piholecontrol.ui.base.RefreshableController
import eu.wedgess.piholecontrol.ui.filters.model.FilterScreenTabType
import eu.wedgess.piholecontrol.ui.filters.tab.model.FilterRulesResult
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FilterTabController @Inject constructor(
    dao: ConnectionDao,
    preferences: DataStore<UserPreferences>,
    private val api: PiHoleApi,
    private val dispatcherProvider: DispatcherProvider
) : RefreshableController<FilterRulesResult>(dao, preferences) {

    private lateinit var filterScreenType: FilterScreenTabType

    fun filterRulesResult(type: FilterScreenTabType): Flow<Result<FilterRulesResult>> {
        filterScreenType = type
        return listenToDataChanges(overrideDelayMillis = 60_000)
    }

    @VisibleForTesting
    suspend fun fetchFilterRule(activeConnectionInfo: ConnectionInfo, type: FilterRuleType) =
        withContext(dispatcherProvider.io) {
            api.fetchFilterRules(activeConnectionInfo, type)
        }

    override suspend fun fetchRefreshableData(activeConnectionInfo: ConnectionInfo): FilterRulesResult =
        supervisorScope {
            val (rule, regexRule) = filterScreenType.toFilterTypePair()
            val deferredRuleType = async { fetchFilterRule(activeConnectionInfo, rule) }
            val deferredRegexRuleType = async { fetchFilterRule(activeConnectionInfo, regexRule) }


            val ruleTypeResult = deferredRuleType.await()
            val regexRuleTypeResult = deferredRegexRuleType.await()

            return@supervisorScope FilterRulesResult(
                rules = ruleTypeResult,
                regexRules = regexRuleTypeResult
            )
        }
}