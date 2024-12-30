package eu.wedgess.piholecontrol.data.model.responses.v6

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class PiHoleClientsOverTimeV6Data(
    @SerialName("history") val history: ArrayList<PiHoleClientsOverTimeHistoryV6Data> = arrayListOf(),
    @SerialName("clients") val clients: Map<String, PiHoleClientsOverTimeClientInfoV6Data>,
    @SerialName("took") val took: Double? = null
)

@Serializable
data class PiHoleClientsOverTimeHistoryV6Data(
    @SerialName("timestamp") val timestamp: Int? = null,
    @SerialName("data") val data: Map<String, Int> = emptyMap()
)

@Serializable
data class PiHoleClientsOverTimeClientInfoV6Data(
    val name: String,
    val total: Int
)