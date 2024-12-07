package eu.wedgess.piholecontrol.domain.usecases.filters

import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.model.ModifyFilterRuleResponseEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.repository.FilterRulesRepository
import eu.wedgess.piholecontrol.utils.extensions.resultOf

class AddFilterRuleUseCase(
    private val filterRuleRepository: FilterRulesRepository,
    private val connectionRepository: ConnectionRepository
) {
    suspend operator fun invoke(
        rule: String,
        ruleType: FilterRuleTypeEntity
    ): Result<ModifyFilterRuleResponseEntity> = resultOf {
        val activeConnection = connectionRepository.fetchActive().getOrThrow()
        filterRuleRepository.addFilterRule(activeConnection, rule, ruleType).getOrThrow()
    }
}
