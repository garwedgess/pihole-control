package eu.wedgess.piholecontrol.presentation.filters.model

import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity

sealed class ModifyFilterRule(val rule: String, val ruleType: FilterRuleTypeEntity) {
    data class Add(
        val domain: String,
        val groups: List<Int>,
        val comment: String?,
        val type: FilterRuleTypeEntity
    ) :
        ModifyFilterRule(domain, type)

    data class Delete(val domain: String, val type: FilterRuleTypeEntity) :
        ModifyFilterRule(domain, type)
}
