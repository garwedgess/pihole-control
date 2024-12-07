package eu.wedgess.piholecontrol.presentation.filters.model

import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity

sealed interface FilterDialogType {
    data object None : FilterDialogType
    data class AddFilterRule(val type: FilterRuleTypeEntity) : FilterDialogType
    data class ShowFilterRuleInfo(val filterRule: FilterRuleEntity) : FilterDialogType
}