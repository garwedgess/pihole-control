package eu.wedgess.piholecontrol.presentation.filters.tab

import eu.wedgess.piholecontrol.presentation.filters.model.FilterByOption
import eu.wedgess.piholecontrol.presentation.filters.tab.model.FilterRuleInfo

interface FilterTabContract {

    data class UiState(val filterRules: List<FilterRuleInfo>)

    sealed interface Event {
        data class OnSearchQueryChanged(val query: String) : Event
        data class OnFilterByOptionsChanged(val filterByOptions: List<FilterByOption>) : Event
        data object OnRefresh : Event
    }
}
