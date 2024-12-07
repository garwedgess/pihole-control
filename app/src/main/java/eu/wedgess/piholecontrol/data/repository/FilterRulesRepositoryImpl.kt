package eu.wedgess.piholecontrol.data.repository

import eu.wedgess.piholecontrol.data.api.FilterRulesApiService
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.mappers.toFilterRuleEntity
import eu.wedgess.piholecontrol.domain.mappers.toModifyFilterRuleResponseEntity
import eu.wedgess.piholecontrol.domain.mappers.toPiHoleFilterRuleType
import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.model.ModifyFilterRuleResponseEntity
import eu.wedgess.piholecontrol.domain.repository.FilterRulesRepository
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import kotlinx.coroutines.withContext

class FilterRulesRepositoryImpl(
    private val apiService: FilterRulesApiService,
    private val dispatcherProvider: DispatcherProvider
) : FilterRulesRepository {

    override suspend fun fetchFilterRules(
        activeConnection: ConnectionEntity,
        ruleType: FilterRuleTypeEntity
    ): Result<List<FilterRuleEntity>> = withContext(dispatcherProvider.io) {
        apiService.fetchFilterRules(activeConnection, ruleType.toPiHoleFilterRuleType())
            .mapCatching { response ->
                response.rulesList.map { it.toFilterRuleEntity() }
            }
    }

    override suspend fun addFilterRule(
        activeConnection: ConnectionEntity,
        rule: String,
        ruleType: FilterRuleTypeEntity
    ): Result<ModifyFilterRuleResponseEntity> = withContext(dispatcherProvider.io) {
        apiService.addFilterRule(activeConnection, rule, ruleType.toPiHoleFilterRuleType())
            .mapCatching {
                it.toModifyFilterRuleResponseEntity()
            }
    }

    override suspend fun removeFilterRule(
        activeConnection: ConnectionEntity,
        rule: String,
        ruleType: FilterRuleTypeEntity
    ): Result<ModifyFilterRuleResponseEntity> = withContext(dispatcherProvider.io) {
        apiService.removeFilterRule(activeConnection, rule, ruleType.toPiHoleFilterRuleType())
            .mapCatching {
                it.toModifyFilterRuleResponseEntity()
            }
    }

}
