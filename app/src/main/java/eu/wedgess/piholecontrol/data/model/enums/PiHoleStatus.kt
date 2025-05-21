package eu.wedgess.piholecontrol.data.model.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PiHoleStatus {
    @SerialName("enabled")
    ENABLED,

    @SerialName("disabled")
    DISABLED,

    @SerialName("failed")
    FAILED,

    @SerialName("unknown")
    UNKNOWN
}
