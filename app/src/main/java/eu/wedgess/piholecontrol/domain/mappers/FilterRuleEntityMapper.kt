package eu.wedgess.piholecontrol.domain.mappers

import eu.wedgess.piholecontrol.data.model.responses.PiHoleFilterRules
import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

fun PiHoleFilterRules.toFilterRulesEntity() = this.rulesList.map {
    it.toFilterRuleEntity()
}

fun PiHoleFilterRules.PiHoleFilterRule.toFilterRuleEntity() = FilterRuleEntity(
    id = this.id,
    enabled = this.enabled == 1,
    comment = this.comment,
    dateAdded = Instant.ofEpochSecond(this.dateAdded)
        .atZone(ZoneId.systemDefault())
        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        .replace("T", " "),
    dateModified = Instant.ofEpochSecond(this.dateModified)
        .atZone(ZoneId.systemDefault())
        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        .replace("T", " "),
    domain = this.domain,
    groups = this.groups,
    type = this.type.toFilterTypeRuleEntity()
)