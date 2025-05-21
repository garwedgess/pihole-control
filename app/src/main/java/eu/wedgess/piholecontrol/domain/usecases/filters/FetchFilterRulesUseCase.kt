package eu.wedgess.piholecontrol.domain.usecases.filters

import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.model.RefreshMode
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchFilterRulesUseCase @Inject constructor(
    private val fetchFilterRuleUseCase: FetchFilterRuleUseCase,
    private val periodicRefreshUseCase: PeriodicRefreshUseCase
) {
    operator fun invoke(ruleType: FilterRuleTypeEntity): Flow<Result<List<FilterRuleEntity>>> {
        return periodicRefreshUseCase
            .apply { setRefreshMode(RefreshMode.Automatic(FILTER_RULES_REFRESH_DELAY)) }
            .invoke { connection ->
                fetchFilterRuleUseCase(connection, ruleType)
            }
    }

    fun refreshRules() = periodicRefreshUseCase.triggerRefresh()

    companion object {
        private const val FILTER_RULES_REFRESH_DELAY = 60_000L
    }
}
