package eu.wedgess.mihole.data.model.enums

import kotlinx.serialization.Serializable

const val WILDCARD_REGEX_PREFIX = "(\\.|^)"
const val WILDCARD_REGEX_SUFFIX = "$"

@Serializable(FilterRuleType.Companion.Serializer::class)
enum class FilterRuleType {
    WHITE,
    BLACK,
    REGEX_WHITE,
    REGEX_BLACK;

    companion object {
        object Serializer : EnumIntSerializer<FilterRuleType>(FilterRuleType::class, WHITE)
    }
}