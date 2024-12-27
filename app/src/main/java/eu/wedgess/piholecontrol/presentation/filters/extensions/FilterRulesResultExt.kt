package eu.wedgess.piholecontrol.presentation.filters.extensions

import eu.wedgess.piholecontrol.domain.model.FilterRulesResultEntity
import eu.wedgess.piholecontrol.presentation.filters.tab.model.FilterRulesResult

fun FilterRulesResultEntity.toFilterRulesResult() = FilterRulesResult(
    rules = this.rules,
    regexRules = this.regexRules
)
