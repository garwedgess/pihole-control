package eu.wedgess.piholecontrol.domain.usecases.filters

import eu.wedgess.piholecontrol.data.model.enums.PiHoleFilterRuleType
import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.presentation.filters.tab.model.FilterRulesResult
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.supervisorScope

class FetchFilterRulesUseCase(
    private val fetchFilterRuleUseCase: FetchFilterRuleUseCase,
    private val periodicRefreshUseCase: PeriodicRefreshUseCase
) {
    operator fun invoke(ruleType: FilterRuleTypeEntity): Flow<Result<FilterRulesResult>> {
        return periodicRefreshUseCase.invoke { connection ->
            supervisorScope {
                val (rule, regexRule) = when (ruleType) {
                    FilterRuleTypeEntity.ALLOW,
                    FilterRuleTypeEntity.REGEX_ALLOW -> Pair(
                        FilterRuleTypeEntity.ALLOW,
                        FilterRuleTypeEntity.REGEX_ALLOW
                    )

                    FilterRuleTypeEntity.BLOCK,
                    FilterRuleTypeEntity.REGEX_BLOCK -> Pair(
                        FilterRuleTypeEntity.BLOCK,
                        FilterRuleTypeEntity.REGEX_BLOCK
                    )
                }
                val deferredRuleType =
                    async { fetchFilterRuleUseCase(connection, rule) }
                val deferredRegexRuleType =
                    async { fetchFilterRuleUseCase(connection, regexRule) }


                val ruleTypeResult = deferredRuleType.await()
                val regexRuleTypeResult = deferredRegexRuleType.await()

                return@supervisorScope Result.success(
                    FilterRulesResult(
                        rules = ruleTypeResult,
                        regexRules = regexRuleTypeResult
                    )
                )
            }
        }
    }

    fun refreshRules() = periodicRefreshUseCase.triggerRefresh()
}