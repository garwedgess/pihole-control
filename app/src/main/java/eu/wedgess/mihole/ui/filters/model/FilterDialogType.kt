package eu.wedgess.mihole.ui.filters.model

import eu.wedgess.mihole.data.model.enums.FilterRuleType
import eu.wedgess.mihole.data.model.responses.PiHoleFilterRules

sealed interface FilterDialogType {
    data object None : FilterDialogType
    data class AddFilterRule(val type: FilterRuleType) : FilterDialogType
    data class ShowFilterRuleInfo(
        val filterRule: PiHoleFilterRules.PiHoleFilterRule
    ) : FilterDialogType
}