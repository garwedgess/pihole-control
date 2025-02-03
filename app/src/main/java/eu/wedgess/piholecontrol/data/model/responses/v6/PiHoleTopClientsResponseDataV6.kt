package eu.wedgess.piholecontrol.data.model.responses.v6

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleTopClientsResponseDataV6(
    val clients: List<PiHoleTopClientDataV6>,
    @SerialName("total_queries") val totalQueries: Int,
    @SerialName("blocked_queries") val blockedQueries: Int,
    val took: Double
)

@Serializable
data class PiHoleTopClientDataV6(
    val name: String,
    val ip: String,
    val count: Int
) {
    val combinedName: String get() = if (name.isNotBlank() && name != ip) "$name|$ip" else ip
}
