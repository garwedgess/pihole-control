package eu.wedgess.piholecontrol.data.model.requests

import kotlinx.serialization.Serializable

@Serializable
data class PiHoleAddFilterRuleRequestDataV6(
    val domain: String,
    val comment: String,
    val groups: List<Int>,
    val enabled: Boolean
)
