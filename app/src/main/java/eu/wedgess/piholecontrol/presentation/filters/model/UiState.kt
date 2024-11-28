package eu.wedgess.piholecontrol.presentation.filters.model

import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity

data class UiState(val filterRules: List<FilterRuleEntity>)