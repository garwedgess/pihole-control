package eu.wedgess.piholecontrol.data.model.responses.v5

import eu.wedgess.piholecontrol.data.model.TopClientData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleTopClientsResponseDataV5(
    @SerialName("top_sources")
    val topSources: Map<String, Int> = mapOf()
) {
    val topClientsWithPercentages: List<TopClientData>
        get() = calculatePercentages(topSources)

    private fun calculatePercentages(data: Map<String, Int>): List<TopClientData> {
        val globalTotal = data.values.sum()

        return data.map { (key, count) ->
            val percentage = if (globalTotal == 0) {
                0f
            } else {
                (count / globalTotal.toFloat()) * 100f
            }
            TopClientData(key, count, percentage)
        }.sortedByDescending { it.percentage }
    }
}
