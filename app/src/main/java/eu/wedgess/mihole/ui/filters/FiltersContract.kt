package eu.wedgess.mihole.ui.filters

import eu.wedgess.mihole.data.model.PiHoleFilterRules
import eu.wedgess.mihole.data.model.enums.FilterRuleType
import eu.wedgess.mihole.ui.base.Resource
import eu.wedgess.mihole.ui.base.UnidirectionalViewModel

interface FiltersContract :
    UnidirectionalViewModel<FiltersContract.UiState, FiltersContract.Event, FiltersContract.Effect> {

    data class UiState(
        val allowList: Resource<List<PiHoleFilterRules.PiHoleFilterRule>>,
        val blockList: Resource<List<PiHoleFilterRules.PiHoleFilterRule>>,
        val selectedRule: PiHoleFilterRules.PiHoleFilterRule?,
        val currentFilterRuleType: FilterRuleType,
        val displayAddRuleDialog: Boolean
    ) {

        fun showAddRuleDialog(): UiState = this.copy(displayAddRuleDialog = true)

        fun dismissAddRuleDialog(): UiState = this.copy(displayAddRuleDialog = false)

        fun filterRuleType(tabIndex: Int): UiState =
            this.copy(currentFilterRuleType = if (tabIndex == 0) FilterRuleType.WHITE else FilterRuleType.BLACK)

        fun allowList(allowList: List<PiHoleFilterRules.PiHoleFilterRule>): UiState =
            this.copy(allowList = Resource.Success(allowList))

        fun blockList(blockList: List<PiHoleFilterRules.PiHoleFilterRule>): UiState =
            this.copy(blockList = Resource.Success(blockList))

        fun filterListError(errorMessage: String): UiState =
            this.copy(
                allowList = Resource.Error(errorMessage),
                blockList = Resource.Error(errorMessage)
            )

        fun selectedRule(ruleInfo: PiHoleFilterRules.PiHoleFilterRule): UiState =
            this.copy(selectedRule = ruleInfo)

        fun ruleDeselected(): UiState = this.copy(selectedRule = null)

        companion object {
            fun initial() = UiState(
                allowList = Resource.Loading,
                blockList = Resource.Loading,
                selectedRule = null,
                displayAddRuleDialog = false,
                currentFilterRuleType = FilterRuleType.WHITE
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
        data class AddRule(val rule: String, val isRegex: Boolean) : Event
        data class RemoveRule(val rule: String, val ruleType: FilterRuleType) : Event
        data class OnRuleSelected(val rule: PiHoleFilterRules.PiHoleFilterRule) : Event
        data class OnTabIndexChanged(val tabIndex: Int) : Event
    }
}