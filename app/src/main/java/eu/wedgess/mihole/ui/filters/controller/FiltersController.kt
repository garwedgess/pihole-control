package eu.wedgess.mihole.ui.filters.controller

import eu.wedgess.mihole.data.model.enums.FilterRuleType
import eu.wedgess.mihole.data.model.responses.ModifyFilterRuleResponse

interface FiltersController {

    suspend fun addFilterRule(rule: String, type: FilterRuleType): Result<ModifyFilterRuleResponse>

    suspend fun removeFilterRule(
        rule: String,
        type: FilterRuleType
    ): Result<ModifyFilterRuleResponse>
}