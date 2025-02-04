package eu.wedgess.piholecontrol.data.model.responses.v5

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias UpstreamDestinationsWithPercentagesV5 = Map<String, Float>

@Serializable
data class PiHoleUpstreamsResponseDataV5(
    @SerialName("forward_destinations")
    val upstreamDestinations: UpstreamDestinationsWithPercentagesV5 = mapOf()
)
