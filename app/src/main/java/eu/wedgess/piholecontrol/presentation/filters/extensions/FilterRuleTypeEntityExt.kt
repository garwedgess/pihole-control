package eu.wedgess.piholecontrol.presentation.filters.extensions

import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity

fun FilterRuleTypeEntity.toBaseFilterRuleType(): FilterRuleTypeEntity = when (this) {
    FilterRuleTypeEntity.ALLOW, FilterRuleTypeEntity.REGEX_ALLOW -> FilterRuleTypeEntity.ALLOW
    FilterRuleTypeEntity.DENY, FilterRuleTypeEntity.REGEX_DENY -> FilterRuleTypeEntity.DENY
}

fun FilterRuleTypeEntity.isRegexFilterRuleType(): Boolean = when (this) {
    FilterRuleTypeEntity.REGEX_ALLOW, FilterRuleTypeEntity.REGEX_DENY -> true
    FilterRuleTypeEntity.ALLOW, FilterRuleTypeEntity.DENY -> false
}

fun FilterRuleTypeEntity.withRegexFilterRuleType(isRegex: Boolean): FilterRuleTypeEntity = when (this) {
    FilterRuleTypeEntity.ALLOW -> if (isRegex) {
        FilterRuleTypeEntity.REGEX_ALLOW
    } else {
        FilterRuleTypeEntity.ALLOW
    }

    FilterRuleTypeEntity.DENY -> if (isRegex) {
        FilterRuleTypeEntity.REGEX_DENY
    } else {
        FilterRuleTypeEntity.DENY
    }

    FilterRuleTypeEntity.REGEX_ALLOW -> FilterRuleTypeEntity.REGEX_ALLOW
    FilterRuleTypeEntity.REGEX_DENY -> FilterRuleTypeEntity.REGEX_DENY
}
