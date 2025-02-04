package eu.wedgess.piholecontrol.presentation.filters

import eu.wedgess.piholecontrol.presentation.filters.model.FilterByOption
import eu.wedgess.piholecontrol.presentation.filters.model.FilterDialogType
import eu.wedgess.piholecontrol.presentation.filters.model.ModifyFilterRule
import eu.wedgess.piholecontrol.presentation.filters.tab.model.FilterRuleInfo
import eu.wedgess.piholecontrol.presentation.navigation.tabs.FilterTab
import eu.wedgess.piholecontrol.utils.UiText

interface FiltersContract {

    data class UiState(
        val dialogType: FilterDialogType,
        val searchQuery: String,
        val showSearchView: Boolean,
        val showFilterByMenu: Boolean,
        val filterByOptions: List<FilterByOption>,
        val allowSelectedFilterBy: List<FilterByOption>,
        val denySelectedFilterBy: List<FilterByOption>,
        val tabOptionItems: List<FilterTab>,
        val selectedTabType: FilterTab
    ) {

        val selectedFilterByOptions: List<FilterByOption>
            get() = when (selectedTabType) {
                is FilterTab.AllowList -> allowSelectedFilterBy
                is FilterTab.DenyList -> denySelectedFilterBy
            }

        companion object {
            fun initial() = UiState(
                dialogType = FilterDialogType.None,
                searchQuery = "",
                showSearchView = false,
                showFilterByMenu = false,
                filterByOptions = emptyList(),
                allowSelectedFilterBy = FilterByOption.getByTab(FilterTab.AllowList),
                denySelectedFilterBy = FilterByOption.getByTab(FilterTab.DenyList),
                tabOptionItems = FilterTab.all(),
                selectedTabType = FilterTab.AllowList
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
        data object OnDismissDialog : Event
        data object AddFilterRuleClick : Event
        data object OnShowFilterByMenu : Event
        data class OnFilterByOptionClick(val option: FilterByOption) : Event
        data object OnDismissFilterBy : Event
        data class OnFilterRuleItemClick(val item: FilterRuleInfo) : Event
        data class OnFilterTabChanged(val type: FilterTab) : Event
        data class OnAddFilterRule(val rule: ModifyFilterRule.Add) : Event
        data class OnDeleteFilterRuleClick(val rule: FilterRuleInfo) : Event
        data class OnDeleteFilterRuleConfirmed(val rule: ModifyFilterRule.Delete) : Event
        data class OnClearSearchQuery(val query: String) : Event
        data class OnSearchExpandedChanged(val expanded: Boolean) : Event
        data class OnSearchQueryChanged(val query: String) : Event
    }
}
