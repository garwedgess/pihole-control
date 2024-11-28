package eu.wedgess.piholecontrol.data.model.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class PiHoleClientsOverTimeData(
    @SerialName("clients") val clients: List<ClientData>,
    @SerialName("over_time") val clientsOverTime: Map<Long, List<Int>>
) {
    @Serializable
    data class ClientData(
        @SerialName("name") val name: String,
        @SerialName("ip") val ip: String,
        @Transient val index: Int = 0
    )
}