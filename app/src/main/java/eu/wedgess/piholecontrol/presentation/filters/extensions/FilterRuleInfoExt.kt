package eu.wedgess.piholecontrol.presentation.filters.extensions

import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity
import eu.wedgess.piholecontrol.presentation.filters.tab.model.FilterRuleInfo

fun FilterRuleEntity.toInfo() = FilterRuleInfo(
    id = this.id,
    enabled = this.enabled,
    comment = this.comment,
    dateAdded = this.dateAdded,
    dateModified = this.dateModified,
    domain = this.domain,
    groups = this.groups,
    type = this.type
)
