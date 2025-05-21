package eu.wedgess.piholecontrol.data.model.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleOverTimeResponseData(
    @SerialName("history") val history: List<PiHoleOverTimeHistoryData> = emptyList(),
    @SerialName("took") val took: Double = 0.0
) {
    @Serializable
    data class PiHoleOverTimeHistoryData(
        @SerialName("timestamp") val timestamp: Int = 0,
        @SerialName("total") val total: Int = 0,
        @SerialName("cached") val cached: Int = 0,
        @SerialName("blocked") val blocked: Int = 0,
        @SerialName("forwarded") val forwarded: Int = 0
    ) {
        fun sumOfPermitted(): Long = cached.toLong() + forwarded.toLong()
        fun sumOfBlocked(): Long = blocked.toLong()
    }
}
