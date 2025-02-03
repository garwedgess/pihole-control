package eu.wedgess.piholecontrol.data.model.enums

import kotlinx.serialization.SerialName

enum class PiHoleStatusV5 {
    @SerialName("enabled")
    ENABLED,

    @SerialName("disabled")
    DISABLED,

    UNKNOWN
}
