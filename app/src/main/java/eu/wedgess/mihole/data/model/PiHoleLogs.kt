package eu.wedgess.mihole.data.model

import eu.wedgess.mihole.data.model.enums.LogsAnswerType
import eu.wedgess.mihole.data.utils.serializers.PiHoleLogSerializer
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleLogsResponse(
    val data: List<PiHoleLog> = listOf()
)