package eu.wedgess.piholecontrol.data.model.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleAuthPasswordRequestDataV6(
    @SerialName("password") val password: String
)
