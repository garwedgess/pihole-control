package eu.wedgess.mihole.data.model.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleForwardDestinations(
    @SerialName("forward_destinations")
    val forwardDestinations: Map<String, Float> = mapOf()
)
