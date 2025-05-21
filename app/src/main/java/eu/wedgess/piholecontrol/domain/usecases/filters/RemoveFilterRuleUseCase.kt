package eu.wedgess.piholecontrol.domain.usecases.filters

import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.model.ModifyFilterRuleResponseEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.repository.FilterRulesRepository
import javax.inject.Inject

class RemoveFilterRuleUseCase @Inject constructor(
    private val filterRuleRepository: FilterRulesRepository,
    private val connectionRepository: ConnectionRepository
) {
    suspend operator fun invoke(
        rule: String,
        ruleType: FilterRuleTypeEntity
    ): Result<ModifyFilterRuleResponseEntity> {
        return connectionRepository.fetchActive().fold(
            onSuccess = { activeConnection ->
                filterRuleRepository.removeFilterRule(
                    activeConnection,
                    rule,
                    ruleType
                )
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
}
