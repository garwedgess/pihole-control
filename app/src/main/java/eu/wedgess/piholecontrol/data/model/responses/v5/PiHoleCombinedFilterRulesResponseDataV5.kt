package eu.wedgess.piholecontrol.data.model.responses.v5

data class PiHoleCombinedFilterRulesResponseDataV5(
    val rules: List<PiHoleFilterRulesResponseDataV5.PiHoleFilterRule>,
    val regexRules: List<PiHoleFilterRulesResponseDataV5.PiHoleFilterRule>,
)
