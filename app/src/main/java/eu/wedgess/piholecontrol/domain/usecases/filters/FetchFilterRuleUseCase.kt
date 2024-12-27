package eu.wedgess.piholecontrol.domain.usecases.filters

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.repository.FilterRulesRepository

class FetchFilterRuleUseCase(
    private val filterRulesRepository: FilterRulesRepository
) {
    suspend operator fun invoke(
        activeConnection: ConnectionEntity,
        ruleType: FilterRuleTypeEntity
    ): Result<List<FilterRuleEntity>> {
        return filterRulesRepository.fetchFilterRules(activeConnection, ruleType)
    }
}
