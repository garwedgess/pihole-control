package eu.wedgess.mihole.data.model.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleTopClients(
    @SerialName("top_sources")
    val topSources: Map<String, Int> = mapOf()
)
