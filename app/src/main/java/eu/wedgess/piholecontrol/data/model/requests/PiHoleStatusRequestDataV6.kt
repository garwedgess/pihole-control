package eu.wedgess.piholecontrol.data.model.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleStatusRequestDataV6(
    @SerialName("blocking")
    val blocking: Boolean,
    val timer: Long? = null
)
