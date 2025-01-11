package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.enums.PiHoleFilterRuleType
import eu.wedgess.piholecontrol.data.model.enums.PiHoleFilterRuleTypeV6
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity

fun PiHoleFilterRuleType.toFilterTypeRuleEntity() = when (this) {
    PiHoleFilterRuleType.ALLOW -> FilterRuleTypeEntity.ALLOW
    PiHoleFilterRuleType.DENY -> FilterRuleTypeEntity.DENY
    PiHoleFilterRuleType.REGEX_ALLOW -> FilterRuleTypeEntity.REGEX_ALLOW
    PiHoleFilterRuleType.REGEX_DENY -> FilterRuleTypeEntity.REGEX_DENY
}

fun FilterRuleTypeEntity.toPiHoleFilterRuleType() = when (this) {
    FilterRuleTypeEntity.ALLOW -> PiHoleFilterRuleType.ALLOW
    FilterRuleTypeEntity.DENY -> PiHoleFilterRuleType.DENY
    FilterRuleTypeEntity.REGEX_ALLOW -> PiHoleFilterRuleType.REGEX_ALLOW
    FilterRuleTypeEntity.REGEX_DENY -> PiHoleFilterRuleType.REGEX_DENY
}

fun FilterRuleTypeEntity.toPiHoleFilterRuleTypeV6() = when (this) {
    FilterRuleTypeEntity.ALLOW -> PiHoleFilterRuleTypeV6.ALLOW
    FilterRuleTypeEntity.DENY -> PiHoleFilterRuleTypeV6.DENY
    FilterRuleTypeEntity.REGEX_ALLOW -> PiHoleFilterRuleTypeV6.REGEX_ALLOW
    FilterRuleTypeEntity.REGEX_DENY -> PiHoleFilterRuleTypeV6.REGEX_DENY
}

fun PiHoleFilterRuleTypeV6.toFilterTypeRuleEntity() = when (this) {
    PiHoleFilterRuleTypeV6.ALLOW -> FilterRuleTypeEntity.ALLOW
    PiHoleFilterRuleTypeV6.DENY -> FilterRuleTypeEntity.DENY
    PiHoleFilterRuleTypeV6.REGEX_ALLOW -> FilterRuleTypeEntity.REGEX_ALLOW
    PiHoleFilterRuleTypeV6.REGEX_DENY -> FilterRuleTypeEntity.REGEX_DENY
}
