package eu.wedgess.piholecontrol.domain.repository

import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.model.ModifyFilterRuleResponseEntity

interface FilterRulesRepository {
    suspend fun fetchFilterRules(
        activeConnection: ConnectionInfo,
        ruleType: FilterRuleTypeEntity
    ): Result<List<FilterRuleEntity>>

    suspend fun addFilterRule(
        activeConnection: ConnectionInfo,
        rule: String,
        ruleType: FilterRuleTypeEntity
    ): Result<ModifyFilterRuleResponseEntity>

    suspend fun removeFilterRule(
        activeConnection: ConnectionInfo,
        rule: String,
        ruleType: FilterRuleTypeEntity
    ): Result<ModifyFilterRuleResponseEntity>
}