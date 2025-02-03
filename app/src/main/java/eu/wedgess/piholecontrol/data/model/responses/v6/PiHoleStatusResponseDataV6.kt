package eu.wedgess.piholecontrol.data.model.responses.v6

import eu.wedgess.piholecontrol.data.model.enums.PiHoleStatusV6
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleStatusResponseDataV6(
    @SerialName("blocking")
    val blocking: PiHoleStatusV6 = PiHoleStatusV6.UNKNOWN,
    @SerialName("timer")
    val timer: Double? = 0.0,
    @SerialName("took")
    val took: Double = 0.0
)
