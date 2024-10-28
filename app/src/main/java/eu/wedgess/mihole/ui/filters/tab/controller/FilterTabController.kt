package eu.wedgess.mihole.ui.filters.tab.controller

import androidx.annotation.VisibleForTesting
import androidx.datastore.core.DataStore
import eu.wedgess.mihole.data.api.PiHoleApi
import eu.wedgess.mihole.data.db.MiHolesDao
import eu.wedgess.mihole.data.model.PiHoleInfo
import eu.wedgess.mihole.data.model.UserPreferences
import eu.wedgess.mihole.data.model.enums.FilterRuleType
import eu.wedgess.mihole.ui.base.RefreshableController
import eu.wedgess.mihole.ui.filters.model.FilterScreenTabType
import eu.wedgess.mihole.ui.filters.tab.model.FilterRulesResult
import eu.wedgess.mihole.utils.DispatcherProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class FilterTabController @Inject constructor(
    dao: MiHolesDao,
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
    suspend fun fetchFilterRule(activePiHoleInfo: PiHoleInfo, type: FilterRuleType) =
        withContext(dispatcherProvider.io) {
            api.fetchFilterRules(activePiHoleInfo, type)
        }

    override suspend fun fetchData(activePiHoleInfo: PiHoleInfo): FilterRulesResult =
        supervisorScope {
            val (rule, regexRule) = filterScreenType.toFilterTypePair()
            val deferredRuleType = async { fetchFilterRule(activePiHoleInfo, rule) }
            val deferredRegexRuleType = async { fetchFilterRule(activePiHoleInfo, regexRule) }


            val ruleTypeResult = deferredRuleType.await()
            val regexRuleTypeResult = deferredRegexRuleType.await()

            return@supervisorScope FilterRulesResult(
                rules = ruleTypeResult,
                regexRules = regexRuleTypeResult
            )
        }
}