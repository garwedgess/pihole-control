package eu.wedgess.piholecontrol.data.model.responses.v5

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleModifyFilterRuleResponse(
    @SerialName("success")
    val success: Boolean = false,
    @SerialName("message")
    val message: String? = null
)
