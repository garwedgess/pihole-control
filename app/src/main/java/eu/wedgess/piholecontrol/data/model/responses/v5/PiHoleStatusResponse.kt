package eu.wedgess.piholecontrol.data.model.responses.v5

import eu.wedgess.piholecontrol.data.model.enums.PiHoleStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleStatusResponse(
    @SerialName("status")
    val status: PiHoleStatus = PiHoleStatus.UNKNOWN
)
