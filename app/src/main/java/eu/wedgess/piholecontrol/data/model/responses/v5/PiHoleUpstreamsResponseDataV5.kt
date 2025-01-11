package eu.wedgess.piholecontrol.data.model.responses.v5

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleUpstreamsResponseDataV5(
    @SerialName("forward_destinations")
    val forwardDestinations: Map<String, Float> = mapOf()
)
