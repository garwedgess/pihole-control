package eu.wedgess.mihole.ui.filters.model

import eu.wedgess.mihole.data.model.enums.FilterRuleType

enum class FilterScreenTabType {
    ALLOW, BLOCK;

     fun toFilterTypePair(): Pair<FilterRuleType, FilterRuleType> = when (this) {
         ALLOW -> Pair(FilterRuleType.ALLOW, FilterRuleType.REGEX_ALLOW)
         BLOCK -> Pair(FilterRuleType.BLOCK, FilterRuleType.REGEX_BLOCK)
    }
}