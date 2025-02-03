package eu.wedgess.piholecontrol.data.model.responses.v5

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleOverTimeResponseDataV5(
    @SerialName("domains_over_time")
    val domainsOverTime: Map<Long, Long> = mapOf(),
    @SerialName("ads_over_time")
    val adsOverTime: Map<Long, Long> = mapOf(),
)
