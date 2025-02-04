package eu.wedgess.piholecontrol.data.model.responses.v5

import kotlinx.serialization.Serializable

@Serializable
data class PiHoleLogSuggestionsResponseDataV5(
    val clients: List<PiHoleClientsOverTimeResponseDataV5.PiHoleClientsOverTimeClientData>
)
