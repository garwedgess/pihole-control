package eu.wedgess.piholecontrol.presentation.filters

import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.GroupEntity
import eu.wedgess.piholecontrol.presentation.common.model.SelectionMode
import eu.wedgess.piholecontrol.presentation.filters.model.FilterByOption
import eu.wedgess.piholecontrol.presentation.filters.model.FilterDialogType
import eu.wedgess.piholecontrol.presentation.filters.model.FilterRuleIdentity
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
        val selectedTabType: FilterTab,
        val selectionMode: SelectionMode<FilterRuleIdentity>
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
                selectedTabType = FilterTab.AllowList,
                selectionMode = SelectionMode.Inactive
            )
        }
    }

    sealed interface Effect {
        sealed class Toast(val message: UiText) : Effect {
            data object RuleAdded :
                Toast(UiText.StringResource(R.string.filters_toast_rule_added))

            data object RuleAddFailed :
                Toast(UiText.StringResource(R.string.filters_toast_rule_add_failed))

            data object RuleRemoved :
                Toast(UiText.StringResource(R.string.filters_toast_rule_removed))

            data object SelectedRulesRemoved :
                Toast(UiText.StringResource(R.string.filters_toast_selected_rules_removed))

            data object RuleRemovalFailed :
                Toast(UiText.StringResource(R.string.filters_toast_rule_removal_failed))

            data object RuleUpdated :
                Toast(UiText.StringResource(R.string.filters_toast_rule_updated))

            data object RuleUpdateFailed :
                Toast(UiText.StringResource(R.string.filters_toast_rule_update_failed))
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
        data class OnFilterRuleItemLongClick(val item: FilterRuleInfo) : Event
        data class OnFilterTabChanged(val type: FilterTab) : Event
        data object OnAddFilterRuleConfirmed : Event
        data object OnUpdateFilterRuleConfirmed : Event
        data class OnEditFilterRuleClick(val rule: FilterRuleInfo) : Event
        data class OnDeleteFilterRuleClick(val rule: FilterRuleInfo) : Event
        data class OnDeleteFilterRuleConfirmed(val rule: ModifyFilterRule.Delete) : Event
        data object OnDeleteSelectedFilterRulesClick : Event
        data object OnDeleteSelectedFilterRulesConfirmed : Event
        data object OnClearSelection : Event
        data class OnClearSearchQuery(val query: String) : Event
        data class OnSearchExpandedChanged(val expanded: Boolean) : Event
        data class OnSearchQueryChanged(val query: String) : Event
        data class OnFilterRuleDomainChanged(val domain: String) : Event
        data class OnFilterRuleGroupsChanged(val groups: Set<GroupEntity>) : Event
        data class OnFilterRuleCommentChanged(val comment: String) : Event
        data class OnFilterRuleEnabledChanged(val enabled: Boolean) : Event
        data class OnFilterRuleRegexChanged(val isRegex: Boolean) : Event
    }
}
