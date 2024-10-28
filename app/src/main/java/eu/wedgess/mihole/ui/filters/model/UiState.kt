package eu.wedgess.mihole.ui.filters.model

import eu.wedgess.mihole.data.model.responses.PiHoleFilterRules

data class UiState(val filterRules: List<PiHoleFilterRules.PiHoleFilterRule>)