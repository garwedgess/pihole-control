package eu.wedgess.piholecontrol.data.model.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleOverTimeData(
    @SerialName("domains_over_time")
    val domainsOverTime: Map<Float, Float> = mapOf(),
    @SerialName("ads_over_time")
    val adsOverTime: Map<Float, Float> = mapOf(),
)