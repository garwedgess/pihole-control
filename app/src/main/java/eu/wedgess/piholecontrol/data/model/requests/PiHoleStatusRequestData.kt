package eu.wedgess.piholecontrol.data.model.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleStatusRequestData(
    @SerialName("blocking")
    val blocking: Boolean,
    val timer: Long? = null
)
