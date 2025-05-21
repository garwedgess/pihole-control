package eu.wedgess.piholecontrol.data.model.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleTopClientsResponseData(
    val clients: List<PiHoleTopClientData>,
    @SerialName("total_queries") val totalQueries: Int,
    @SerialName("blocked_queries") val blockedQueries: Int,
    val took: Double
)

@Serializable
data class PiHoleTopClientData(
    val name: String,
    val ip: String,
    val count: Int
) {
    val combinedName: String get() = if (name.isNotBlank() && name != ip) "$name|$ip" else ip
}
