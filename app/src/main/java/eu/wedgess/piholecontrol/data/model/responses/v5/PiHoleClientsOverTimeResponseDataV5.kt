package eu.wedgess.piholecontrol.data.model.responses.v5

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class PiHoleClientsOverTimeResponseDataV5(
    @SerialName("clients") val clients: List<PiHoleClientsOverTimeClientData>,
    @SerialName("over_time") val clientsOverTime: Map<Long, List<Int>>
) {
    @Serializable
    data class PiHoleClientsOverTimeClientData(
        @SerialName("name") val name: String,
        @SerialName("ip") val ip: String,
        @Transient val index: Int = 0
    )
}
