package eu.wedgess.piholecontrol.data.model.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleTopQueriesResponseData(
    val domains: List<PiHoleTopQueryData>,
    @SerialName("total_queries") val totalQueries: Int,
    @SerialName("blocked_queries") val blockedQueries: Int,
    val took: Double
)

@Serializable
data class PiHoleTopQueryData(
    val domain: String,
    val count: Int
)
