package eu.wedgess.piholecontrol.presentation.filters.tab

import eu.wedgess.piholecontrol.presentation.filters.tab.model.FilterRuleInfo
import eu.wedgess.piholecontrol.utils.UiText

interface FilterTabContract {

    data class UiState(val filterRules: List<FilterRuleInfo>)

    sealed interface Event {
        data class OnSearchQueryChanged(val query: String) : Event
        data object OnRefresh : Event
    }

    sealed interface Effect {
        data class ShowErrorSnackbar(val errorMessages: List<UiText>) : Effect
    }
}
