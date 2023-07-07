package eu.wedgess.mihole.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModifyFilterRuleResponse(
    @SerialName("success")
    val success: Boolean = false,
    @SerialName("message")
    val message: String? = null
)