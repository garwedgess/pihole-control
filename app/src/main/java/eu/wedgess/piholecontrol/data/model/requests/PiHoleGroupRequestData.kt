package eu.wedgess.piholecontrol.data.model.requests

import kotlinx.serialization.Serializable

@Serializable
data class PiHoleGroupRequestData(
    val name: String,
    val enabled: Boolean,
    val comment: String? = null
)
