package eu.wedgess.mihole.data.model.responses

import eu.wedgess.mihole.data.model.enums.PiHoleStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleStatusResponse(
    @SerialName("status")
    val status: PiHoleStatus = PiHoleStatus.UNKNOWN
)