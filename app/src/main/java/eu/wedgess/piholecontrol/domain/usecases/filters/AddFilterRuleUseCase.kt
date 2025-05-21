package eu.wedgess.piholecontrol.domain.usecases.filters

import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.model.ModifyFilterRuleResponseEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.repository.FilterRulesRepository
import javax.inject.Inject

class AddFilterRuleUseCase @Inject constructor(
    private val filterRuleRepository: FilterRulesRepository,
    private val connectionRepository: ConnectionRepository
) {
    suspend operator fun invoke(
        rule: String,
        groups: List<Int>,
        comment: String?,
        ruleType: FilterRuleTypeEntity
    ): Result<ModifyFilterRuleResponseEntity> {
        return connectionRepository.fetchActive().fold(
            onSuccess = { activeConnection ->
                filterRuleRepository.addFilterRule(
                    activeConnection = activeConnection,
                    domain = rule,
                    groups = groups,
                    comment = comment,
                    domainType = ruleType
                )
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
}
