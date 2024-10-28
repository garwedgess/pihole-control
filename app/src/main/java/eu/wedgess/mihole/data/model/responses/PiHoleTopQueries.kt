package eu.wedgess.mihole.data.model.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleTopQueries(
    @SerialName("top_ads")
    val topAds: Map<String, Int> = mapOf(),
    @SerialName("top_queries")
    val topQueries: Map<String, Int> = mapOf()
)
