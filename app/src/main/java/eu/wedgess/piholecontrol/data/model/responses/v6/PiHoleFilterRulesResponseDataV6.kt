package eu.wedgess.piholecontrol.data.model.responses.v6

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleFilterRulesResponseDataV6(
    val domains: List<PiHoleFilterRuleData>,
    val took: Double
) {

    @Serializable
    data class PiHoleFilterRuleData(
        val domain: String,
        val unicode: String,
        val type: String,
        val kind: String,
        val comment: String?,
        val groups: List<Int>,
        val enabled: Boolean,
        val id: Int,
        @SerialName("date_added") val dateAdded: Long,
        @SerialName("date_modified") val dateModified: Long
    )
}
