package eu.wedgess.piholecontrol.data.model.responses.v5

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class PiHoleClientsOverTimeV5Data(
    @SerialName("clients") val clients: List<PiHoleClientsOverTimeClientV5Data>,
    @SerialName("over_time") val clientsOverTime: Map<Long, List<Int>>
) {
    @Serializable
    data class PiHoleClientsOverTimeClientV5Data(
        @SerialName("name") val name: String,
        @SerialName("ip") val ip: String,
        @Transient val index: Int = 0
    )
}
