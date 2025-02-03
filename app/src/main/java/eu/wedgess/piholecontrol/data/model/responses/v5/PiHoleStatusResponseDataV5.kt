package eu.wedgess.piholecontrol.data.model.responses.v5

import eu.wedgess.piholecontrol.data.model.enums.PiHoleStatusV5
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleStatusResponseDataV5(
    @SerialName("status")
    val status: PiHoleStatusV5 = PiHoleStatusV5.UNKNOWN
)
