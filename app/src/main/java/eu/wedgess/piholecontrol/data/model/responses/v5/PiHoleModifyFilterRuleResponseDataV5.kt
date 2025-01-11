package eu.wedgess.piholecontrol.data.model.responses.v5

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleModifyFilterRuleResponseDataV5(
    @SerialName("success")
    val success: Boolean = false,
    @SerialName("message")
    val message: String? = null
)
