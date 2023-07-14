package eu.wedgess.mihole.data.model

import eu.wedgess.mihole.data.model.enums.LogsAnswerType
import eu.wedgess.mihole.data.utils.serializers.PiHoleLogSerializer
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