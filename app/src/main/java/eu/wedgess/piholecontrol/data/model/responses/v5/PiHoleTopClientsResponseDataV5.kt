package eu.wedgess.piholecontrol.data.model.responses.v5

import eu.wedgess.piholecontrol.data.model.TopClientData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.floor

@Serializable
data class PiHoleTopClientsResponseDataV5(
    @SerialName("top_sources")
    val topSources: Map<String, Int> = mapOf(),
    @SerialName("top_sources_blocked")
    val topSourcesBlocked: Map<String, Int> = mapOf()
) {
    val topClientsWithPercentages: List<TopClientData>
        get() = calculatePercentages(topSources)

    val topClientsBlockedWithPercentages: List<TopClientData>
        get() = calculatePercentages(topSourcesBlocked)

    private fun calculatePercentages(data: Map<String, Int>): List<TopClientData> {
        val globalTotal = data.values.sum()

        return data.map { (key, count) ->
            val percentage = if (globalTotal == 0) {
                0f
            } else {
                (count / globalTotal.toFloat()) * 100f
            }
            val truncatedPercentage = floor(percentage * 100) / 100
            TopClientData(key, count, truncatedPercentage)
        }.sortedByDescending { it.percentage }
    }
}
