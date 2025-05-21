package eu.wedgess.piholecontrol.domain.repository

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.model.ModifyFilterRuleResponseEntity

interface FilterRulesRepository {
    suspend fun fetchFilterRules(
        activeConnection: ConnectionEntity,
        domainType: FilterRuleTypeEntity
    ): Result<List<FilterRuleEntity>>

    suspend fun addFilterRule(
        activeConnection: ConnectionEntity,
        domain: String,
        groups: List<Int>,
        comment: String?,
        domainType: FilterRuleTypeEntity
    ): Result<ModifyFilterRuleResponseEntity>

    suspend fun removeFilterRule(
        activeConnection: ConnectionEntity,
        domain: String,
        domainType: FilterRuleTypeEntity
    ): Result<ModifyFilterRuleResponseEntity>
}
