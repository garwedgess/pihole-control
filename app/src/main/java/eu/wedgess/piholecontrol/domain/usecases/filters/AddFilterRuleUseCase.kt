package eu.wedgess.piholecontrol.domain.usecases.filters

import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.model.ModifyFilterRuleResponseEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.repository.FilterRulesRepository

class AddFilterRuleUseCase(
    private val filterRuleRepository: FilterRulesRepository,
    private val connectionRepository: ConnectionRepository
) {
    suspend operator fun invoke(
        rule: String,
        ruleType: FilterRuleTypeEntity
    ): Result<ModifyFilterRuleResponseEntity> {
        val activeConnection = connectionRepository.fetchActive().getOrThrow()
        return filterRuleRepository.addFilterRule(activeConnection, rule, ruleType)
    }
}
