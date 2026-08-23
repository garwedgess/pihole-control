package eu.wedgess.piholecontrol.presentation.filters.model

import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.model.GroupEntity
import eu.wedgess.piholecontrol.presentation.filters.tab.model.FilterRuleInfo

sealed interface FilterDialogType {
    data object None : FilterDialogType
    data class AddFilterRule(
        val type: FilterRuleTypeEntity,
        val groups: List<GroupEntity>,
        val draft: FilterRuleDraft
    ) : FilterDialogType
    data class EditFilterRule(
        val original: FilterRuleIdentity,
        val draft: FilterRuleInfo,
        val groups: List<GroupEntity>
    ) : FilterDialogType
    data class ShowFilterRuleInfo(val filterRule: FilterRuleInfo) : FilterDialogType
    data class OnConfirmFilterDelete(val filterRule: FilterRuleInfo) : FilterDialogType
}
