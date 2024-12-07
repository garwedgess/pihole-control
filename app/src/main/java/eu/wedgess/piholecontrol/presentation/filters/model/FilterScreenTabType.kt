package eu.wedgess.piholecontrol.presentation.filters.model

import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity

enum class FilterScreenTabType {
    ALLOW, BLOCK;

    fun toFilterTypePair(): Pair<FilterRuleTypeEntity, FilterRuleTypeEntity> = when (this) {
        ALLOW -> Pair(FilterRuleTypeEntity.ALLOW, FilterRuleTypeEntity.REGEX_ALLOW)
        BLOCK -> Pair(FilterRuleTypeEntity.BLOCK, FilterRuleTypeEntity.REGEX_BLOCK)
    }
}