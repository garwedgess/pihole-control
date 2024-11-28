package eu.wedgess.piholecontrol.domain.mappers

import eu.wedgess.piholecontrol.data.model.enums.PiHoleFilterRuleType
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity

fun PiHoleFilterRuleType.toFilterTypeRuleEntity() = when (this) {
    PiHoleFilterRuleType.ALLOW -> FilterRuleTypeEntity.ALLOW
    PiHoleFilterRuleType.BLOCK -> FilterRuleTypeEntity.BLOCK
    PiHoleFilterRuleType.REGEX_ALLOW -> FilterRuleTypeEntity.REGEX_ALLOW
    PiHoleFilterRuleType.REGEX_BLOCK -> FilterRuleTypeEntity.REGEX_BLOCK
}

fun FilterRuleTypeEntity.toPiHoleFilterRuleType() = when (this) {
    FilterRuleTypeEntity.ALLOW -> PiHoleFilterRuleType.ALLOW
    FilterRuleTypeEntity.BLOCK -> PiHoleFilterRuleType.BLOCK
    FilterRuleTypeEntity.REGEX_ALLOW -> PiHoleFilterRuleType.REGEX_ALLOW
    FilterRuleTypeEntity.REGEX_BLOCK -> PiHoleFilterRuleType.REGEX_BLOCK
}