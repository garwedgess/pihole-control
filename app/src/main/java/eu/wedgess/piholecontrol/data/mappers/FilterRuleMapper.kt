package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.enums.PiHoleFilterRuleType
import eu.wedgess.piholecontrol.data.model.responses.PiHoleFilterRulesResponseData
import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

fun PiHoleFilterRulesResponseData.PiHoleFilterRuleData.toFilterRuleEntity() = FilterRuleEntity(
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
    type = PiHoleFilterRuleType[this.type, this.kind].toFilterTypeRuleEntity()
)
