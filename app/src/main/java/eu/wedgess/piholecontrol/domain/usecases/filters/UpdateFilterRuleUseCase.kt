package eu.wedgess.piholecontrol.domain.usecases.filters

import eu.wedgess.piholecontrol.domain.model.FilterRuleUpdateEntity
import eu.wedgess.piholecontrol.domain.model.ModifyFilterRuleResponseEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.repository.FilterRulesRepository
import javax.inject.Inject

class UpdateFilterRuleUseCase @Inject constructor(
    private val filterRuleRepository: FilterRulesRepository,
    private val connectionRepository: ConnectionRepository
) {
    suspend operator fun invoke(
        update: FilterRuleUpdateEntity
    ): Result<ModifyFilterRuleResponseEntity> {
        return connectionRepository.fetchActive().fold(
            onSuccess = { activeConnection ->
                filterRuleRepository.updateFilterRule(
                    activeConnection = activeConnection,
                    update = update
                )
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
}
