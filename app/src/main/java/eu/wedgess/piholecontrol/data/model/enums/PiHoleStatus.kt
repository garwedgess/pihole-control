package eu.wedgess.piholecontrol.data.model.enums

import kotlinx.serialization.SerialName

enum class PiHoleStatus {
    @SerialName("enabled")
    ENABLED,

    @SerialName("disabled")
    DISABLED,

    UNKNOWN
}
