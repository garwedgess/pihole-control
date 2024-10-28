package eu.wedgess.mihole.data.model.responses

import kotlinx.serialization.Serializable

@Serializable
data class PiHoleLogsResponse(
    val data: List<PiHoleLog> = listOf()
)