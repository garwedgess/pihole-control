package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.enums.PiHoleFilterRuleType
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity

fun FilterRuleTypeEntity.toPiHoleFilterRuleTypeV6() = when (this) {
    FilterRuleTypeEntity.ALLOW -> PiHoleFilterRuleType.ALLOW
    FilterRuleTypeEntity.DENY -> PiHoleFilterRuleType.DENY
    FilterRuleTypeEntity.REGEX_ALLOW -> PiHoleFilterRuleType.REGEX_ALLOW
    FilterRuleTypeEntity.REGEX_DENY -> PiHoleFilterRuleType.REGEX_DENY
}

fun PiHoleFilterRuleType.toFilterTypeRuleEntity() = when (this) {
    PiHoleFilterRuleType.ALLOW -> FilterRuleTypeEntity.ALLOW
    PiHoleFilterRuleType.DENY -> FilterRuleTypeEntity.DENY
    PiHoleFilterRuleType.REGEX_ALLOW -> FilterRuleTypeEntity.REGEX_ALLOW
    PiHoleFilterRuleType.REGEX_DENY -> FilterRuleTypeEntity.REGEX_DENY
}
