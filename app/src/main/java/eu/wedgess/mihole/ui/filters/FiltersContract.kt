package eu.wedgess.mihole.ui.filters

import androidx.compose.ui.text.input.TextFieldValue
import eu.wedgess.mihole.data.model.PiHoleFilterRules
import eu.wedgess.mihole.data.model.enums.FilterRuleType
import eu.wedgess.mihole.ui.base.UiResult
import eu.wedgess.mihole.ui.base.UnidirectionalViewModel
import eu.wedgess.mihole.ui.common.search.SearchState
import eu.wedgess.mihole.utils.UiText

interface FiltersContract :
    UnidirectionalViewModel<FiltersContract.UiState, FiltersContract.Event, FiltersContract.Effect> {

    data class UiState(
        val allowList: UiResult<List<PiHoleFilterRules.PiHoleFilterRule>>,
        val blockList: UiResult<List<PiHoleFilterRules.PiHoleFilterRule>>,
        val selectedRule: PiHoleFilterRules.PiHoleFilterRule?,
        val currentFilterRuleType: FilterRuleType,
        val displayAddRuleDialog: Boolean,
        val showSearchView: Boolean,
        val searchState: SearchState<PiHoleFilterRules.PiHoleFilterRule>
    ) {

        private fun resetSearchState(): UiState =
            this.copy(searchState = SearchState())

        fun setSearchResults(result: List<PiHoleFilterRules.PiHoleFilterRule>): UiState =
            this.copy(
                searchState = this.searchState.copy(
                    searchResults = result,
                    searching = false
                )
            )

        fun setSearchStateQuery(query: TextFieldValue): UiState =
            this.copy(searchState = this.searchState.copy(query = query))

        fun setSearchStateAsInProgress(): UiState =
            this.copy(searchState = this.searchState.copy(searching = true))

        fun showSearchView(): UiState =
            this.copy(showSearchView = true)

        fun hideSearchView(): UiState = this.copy(showSearchView = false).resetSearchState()

        fun showAddRuleDialog(): UiState = this.copy(displayAddRuleDialog = true)

        fun dismissAddRuleDialog(): UiState = this.copy(displayAddRuleDialog = false)

        fun filterRuleType(tabIndex: Int): UiState =
            this.copy(currentFilterRuleType = if (tabIndex == 0) FilterRuleType.WHITE else FilterRuleType.BLACK)
                .resetSearchState()

        fun allowList(allowList: List<PiHoleFilterRules.PiHoleFilterRule>): UiState =
            this.copy(allowList = UiResult.Success(allowList))

        fun blockList(blockList: List<PiHoleFilterRules.PiHoleFilterRule>): UiState =
            this.copy(blockList = UiResult.Success(blockList))

        fun filterListError(errorMessage: UiText): UiState =
            this.copy(
                allowList = UiResult.Error(errorMessage),
                blockList = UiResult.Error(errorMessage)
            )

        fun selectedRule(ruleInfo: PiHoleFilterRules.PiHoleFilterRule): UiState =
            this.copy(selectedRule = ruleInfo)

        fun ruleDeselected(): UiState = this.copy(selectedRule = null)

        companion object {
            fun initial() = UiState(
                allowList = UiResult.Loading,
                blockList = UiResult.Loading,
                selectedRule = null,
                displayAddRuleDialog = false,
                currentFilterRuleType = FilterRuleType.WHITE,
                showSearchView = false,
                searchState = SearchState()
            )
        }
    }

    sealed interface Effect {
        sealed class Toast(val message: String) : Effect {
            object RuleAdded : Toast("Successfully added rule")
            object RuleAddFailed : Toast("Failed to add rule")
            object RuleRemoved : Toast("Successfully removed rule")
            object RuleRemovalFailed : Toast("Failed to remove rule")
        }
    }

    sealed interface Event {
        object FetchRulesList : Event
        object OnRuleDeselected : Event
        object OnAddRuleClick : Event
        object OnDismissAddRuleDialog : Event
        object OnShowSearchView : Event
        object OnHideShowSearchView : Event
        object ListenForConnectionChanges : Event
        data class OnSearchQueryChanged(val query: TextFieldValue) : Event
        data class AddRule(val rule: String, val isRegex: Boolean) : Event
        data class RemoveRule(val rule: String, val ruleType: FilterRuleType) : Event
        data class OnRuleSelected(val rule: PiHoleFilterRules.PiHoleFilterRule) : Event
        data class OnTabIndexChanged(val tabIndex: Int) : Event
    }
}