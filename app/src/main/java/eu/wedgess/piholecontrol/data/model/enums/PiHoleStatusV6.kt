package eu.wedgess.piholecontrol.data.model.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PiHoleStatusV6 {
    @SerialName("enabled")
    ENABLED,

    @SerialName("disabled")
    DISABLED,

    @SerialName("failed")
    FAILED,

    @SerialName("unknown")
    UNKNOWN
}
