package eu.wedgess.piholecontrol.data.model.enums

enum class PiHoleFilterRuleTypeV6(val type: String, val kind: String) {
    ALLOW("allow", "exact"),
    DENY("deny", "exact"),
    REGEX_ALLOW("allow", "regex"),
    REGEX_DENY("deny", "regex");

    companion object {

        operator fun get(type: String, kind: String): PiHoleFilterRuleTypeV6 = checkNotNull(
            PiHoleFilterRuleTypeV6.entries.firstOrNull { it.type == type && it.kind == kind }
        ) { "No ${PiHoleFilterRuleTypeV6::class.simpleName} found for key: $type and kind: $kind" }
    }
}
