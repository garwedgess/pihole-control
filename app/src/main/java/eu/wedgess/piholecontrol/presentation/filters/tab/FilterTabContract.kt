package eu.wedgess.piholecontrol.presentation.filters.tab

import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity
import eu.wedgess.piholecontrol.utils.UiText

interface FilterTabContract {

    data class UiState(val filterRules: List<FilterRuleEntity>)

    sealed interface Effect {
        data class ShowErrorSnackbar(val errorMessages: List<UiText>) : Effect
    }
}
