package eu.wedgess.piholecontrol.data.model.responses.v6

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleTopQueriesResponseDataV6(
    val domains: List<PiHoleTopQueryDataV6>,
    @SerialName("total_queries") val totalQueries: Int,
    @SerialName("blocked_queries") val blockedQueries: Int,
    val took: Double
)

@Serializable
data class PiHoleTopQueryDataV6(
    val domain: String,
    val count: Int
)
