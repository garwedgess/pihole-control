package eu.wedgess.piholecontrol.data.model.enums

enum class PiHoleFilterRuleType(val type: String, val kind: String) {
    ALLOW("allow", "exact"),
    DENY("deny", "exact"),
    REGEX_ALLOW("allow", "regex"),
    REGEX_DENY("deny", "regex");

    companion object {

        operator fun get(type: String, kind: String): PiHoleFilterRuleType = checkNotNull(
            PiHoleFilterRuleType.entries.firstOrNull { it.type == type && it.kind == kind }
        ) { "No ${PiHoleFilterRuleType::class.simpleName} found for key: $type and kind: $kind" }
    }
}
