package eu.wedgess.piholecontrol.data.model.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleAuthPasswordRequestData(
    @SerialName("password") val password: String
)
