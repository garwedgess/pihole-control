package eu.wedgess.piholecontrol.data.model.responses.v6

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleClientsOverTimeResponseDataV6(
    @SerialName("history") val history: List<PiHoleClientsOverTimeHistoryData> = listOf(),
    @SerialName("clients") val clients: Map<String, PiHoleClientsOverTimeClientData>,
    @SerialName("took") val took: Double? = null
) {

    @Serializable
    data class PiHoleClientsOverTimeHistoryData(
        @SerialName("timestamp") val timestamp: Int? = null,
        @SerialName("data") val data: Map<String, Int> = emptyMap()
    )

    @Serializable
    data class PiHoleClientsOverTimeClientData(
        val name: String? = null,
        val total: Int
    )
}
