package eu.wedgess.piholecontrol.ui.filters.model

import eu.wedgess.piholecontrol.data.model.enums.FilterRuleType
import eu.wedgess.piholecontrol.data.model.responses.PiHoleFilterRules

sealed interface FilterDialogType {
    data object None : FilterDialogType
    data class AddFilterRule(val type: FilterRuleType) : FilterDialogType
    data class ShowFilterRuleInfo(
        val filterRule: PiHoleFilterRules.PiHoleFilterRule
    ) : FilterDialogType
}