package eu.wedgess.piholecontrol.presentation.filters.model

import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity

data class FilterRuleIdentity(
    val domain: String,
    val type: FilterRuleTypeEntity
)
