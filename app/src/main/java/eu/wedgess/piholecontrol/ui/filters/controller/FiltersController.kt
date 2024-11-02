package eu.wedgess.piholecontrol.ui.filters.controller

import eu.wedgess.piholecontrol.data.model.enums.FilterRuleType
import eu.wedgess.piholecontrol.data.model.responses.ModifyFilterRuleResponse

interface FiltersController {

    suspend fun addFilterRule(rule: String, type: FilterRuleType): Result<ModifyFilterRuleResponse>

    suspend fun removeFilterRule(
        rule: String,
        type: FilterRuleType
    ): Result<ModifyFilterRuleResponse>
}