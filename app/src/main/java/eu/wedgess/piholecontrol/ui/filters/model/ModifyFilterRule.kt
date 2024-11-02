package eu.wedgess.piholecontrol.ui.filters.model

import eu.wedgess.piholecontrol.data.model.enums.FilterRuleType

sealed class ModifyFilterRule(val rule: String, val ruleType: FilterRuleType) {
    data class Add(val domain: String, val type: FilterRuleType) : ModifyFilterRule(domain, type)
    data class Delete(val domain: String, val type: FilterRuleType) : ModifyFilterRule(domain, type)
}