package eu.wedgess.piholecontrol.data.model.responses

import eu.wedgess.piholecontrol.data.model.enums.PiHoleStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleStatusResponseData(
    @SerialName("blocking")
    val blocking: PiHoleStatus = PiHoleStatus.UNKNOWN,
    @SerialName("timer")
    val timer: Double? = 0.0,
    @SerialName("took")
    val took: Double = 0.0
)
