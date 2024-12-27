package eu.wedgess.piholecontrol.presentation.filters.model

import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.presentation.filters.tab.model.FilterRuleInfo

sealed interface FilterDialogType {
    data object None : FilterDialogType
    data class AddFilterRule(val type: FilterRuleTypeEntity) : FilterDialogType
    data class ShowFilterRuleInfo(val filterRule: FilterRuleInfo) : FilterDialogType
    data class OnConfirmFilterDelete(val filterRule: FilterRuleInfo) : FilterDialogType
}
