package eu.wedgess.mihole.data.model.responses

import eu.wedgess.mihole.data.model.enums.FilterRuleType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleFilterRules(
    @SerialName("data")
    val rulesList: List<PiHoleFilterRule> = listOf()
) {
    @Serializable
    data class PiHoleFilterRule(
        @SerialName("comment")
        val comment: String? = null,
        @SerialName("date_added")
        val dateAdded: Long = 0,
        @SerialName("date_modified")
        val dateModified: Long = 0,
        @SerialName("domain")
        val domain: String = "",
        @SerialName("enabled")
        val enabled: Int = 0,
        @SerialName("groups")
        val groups: List<Int> = listOf(),
        @SerialName("id")
        val id: Int = 0,
        @SerialName("type")
        val type: FilterRuleType = FilterRuleType.REGEX_ALLOW
    )
}