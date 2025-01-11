package eu.wedgess.piholecontrol.domain.usecases.filters

import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import kotlinx.coroutines.flow.Flow

class FetchFilterRulesUseCase(
    private val fetchFilterRuleUseCase: FetchFilterRuleUseCase,
    private val periodicRefreshUseCase: PeriodicRefreshUseCase
) {
    operator fun invoke(ruleType: FilterRuleTypeEntity): Flow<Result<List<FilterRuleEntity>>> {
        return periodicRefreshUseCase.invoke { connection ->
            fetchFilterRuleUseCase(connection, ruleType)
        }
    }

    fun refreshRules() = periodicRefreshUseCase.triggerRefresh()
}
