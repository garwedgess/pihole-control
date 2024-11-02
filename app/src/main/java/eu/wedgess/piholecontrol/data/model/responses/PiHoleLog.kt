package eu.wedgess.piholecontrol.data.model.responses

import eu.wedgess.piholecontrol.data.model.enums.LogsAnswerType
import eu.wedgess.piholecontrol.data.utils.serializers.PiHoleLogSerializer
import kotlinx.serialization.Serializable

@Serializable(PiHoleLogSerializer::class)
data class PiHoleLog(
    val timestamp: Long,
    val queryType: String,
    val requestedDomain: String,
    val client: String,
    val answerType: LogsAnswerType,
    val responseTime: Int
)