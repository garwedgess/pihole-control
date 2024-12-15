package eu.wedgess.piholecontrol.presentation.filters

import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.presentation.filters.model.FilterDialogType
import eu.wedgess.piholecontrol.presentation.filters.model.ModifyFilterRule
import eu.wedgess.piholecontrol.utils.UiText


interface FiltersContract {

    data class UiState(
        val dialogType: FilterDialogType,
        val searchQuery: String,
        val showSearchView: Boolean,
    ) {

        companion object {
            fun initial() = UiState(
                dialogType = FilterDialogType.None,
                searchQuery = "",
                showSearchView = false
            )
        }
    }

    sealed interface Effect {
        sealed class Toast(val message: UiText) : Effect {
            data object RuleAdded : Toast(UiText.DynamicString("Successfully added rule"))
            data object RuleAddFailed : Toast(UiText.DynamicString("Failed to add rule"))
            data object RuleRemoved : Toast(UiText.DynamicString("Successfully removed rule"))
            data object RuleRemovalFailed : Toast(UiText.DynamicString("Failed to remove rule"))
        }
    }

    sealed interface Event {
        data object OnShowSearchView : Event
        data object OnSearchClick : Event
        data object OnDismissDialog : Event
        data object AddFilterRuleClick : Event
        data class OnFilterRuleItemClick(val item: FilterRuleEntity) : Event
        data class OnFilterTabChanged(val type: FilterRuleTypeEntity) : Event
        data class OnAddFilterRule(val rule: ModifyFilterRule.Add) : Event
        data class OnDeleteFilterRule(val rule: ModifyFilterRule.Delete) : Event
        data class OnClearSearchQuery(val query: String) : Event
        data class OnSearchExpandedChanged(val expanded: Boolean) : Event
        data class OnSearchQueryChanged(val query: String) : Event
    }
}