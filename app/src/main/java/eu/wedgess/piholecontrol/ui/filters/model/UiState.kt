package eu.wedgess.piholecontrol.ui.filters.model

import eu.wedgess.piholecontrol.data.model.responses.PiHoleFilterRules

data class UiState(val filterRules: List<PiHoleFilterRules.PiHoleFilterRule>)