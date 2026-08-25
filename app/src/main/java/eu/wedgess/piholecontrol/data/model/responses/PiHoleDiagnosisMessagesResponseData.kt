package eu.wedgess.piholecontrol.data.model.responses

import kotlinx.serialization.Serializable

@Serializable
data class PiHoleDiagnosisMessagesResponseData(
    val messages: List<MessageData> = emptyList()
) {
    @Serializable
    data class MessageData(
        val id: Int,
        val timestamp: Double,
        val type: String,
        val plain: String,
        val html: String = "",
        val url: String? = null
    )
}
