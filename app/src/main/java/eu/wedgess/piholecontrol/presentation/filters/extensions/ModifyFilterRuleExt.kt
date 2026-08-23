package eu.wedgess.piholecontrol.presentation.filters.extensions

import eu.wedgess.piholecontrol.domain.model.FilterRuleUpdateEntity
import eu.wedgess.piholecontrol.presentation.filters.model.ModifyFilterRule

fun ModifyFilterRule.Update.toFilterRuleUpdateEntity() = FilterRuleUpdateEntity(
    originalDomain = originalDomain,
    domain = domain,
    groups = groups,
    comment = comment,
    enabled = enabled,
    originalType = originalType,
    type = type
)
