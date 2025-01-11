package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.enums.PiHoleFilterRuleTypeV6
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleFilterRulesResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleFilterRulesResponseDataV6
import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

fun PiHoleFilterRulesResponseDataV5.toFilterRulesEntity() = this.rulesList.map {
    it.toFilterRuleEntity()
}

fun PiHoleFilterRulesResponseDataV5.PiHoleFilterRule.toFilterRuleEntity() = FilterRuleEntity(
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

fun PiHoleFilterRulesResponseDataV6.PiHoleFilterRuleData.toFilterRuleEntity() = FilterRuleEntity(
    id = this.id,
    enabled = this.enabled,
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
    type = PiHoleFilterRuleTypeV6[this.type, this.kind].toFilterTypeRuleEntity()
)
