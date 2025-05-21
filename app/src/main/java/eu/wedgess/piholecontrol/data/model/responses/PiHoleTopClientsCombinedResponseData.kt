package eu.wedgess.piholecontrol.data.model.responses

import eu.wedgess.piholecontrol.data.model.TopClientData
import kotlin.math.floor

data class PiHoleTopClientsCombinedResponseData(
    val all: List<PiHoleTopClientData>,
    val blocked: List<PiHoleTopClientData>,
) {
    val allWithPercentages: List<TopClientData>
        get() = calculateWithPercentages(all)

    val blockedWithPercentages: List<TopClientData>
        get() = calculateWithPercentages(blocked)

    private fun calculateWithPercentages(data: List<PiHoleTopClientData>): List<TopClientData> {
        val combinedData = data.groupBy { it.combinedName }
            .mapValues { (_, group) ->
                group.reduce { acc, upstream ->
                    acc.copy(count = acc.count + upstream.count)
                }
            }

        val globalTotalQueries = data.sumOf { it.count }

        return combinedData.values.map { entry ->
            val percentage = if (globalTotalQueries == 0) {
                0f
            } else {
                (entry.count / globalTotalQueries.toFloat()) * 100f
            }
            val truncatedPercentage = floor(percentage * 100) / 100
            TopClientData(entry.combinedName, entry.count, truncatedPercentage)
        }.sortedByDescending { it.hits }
    }
}
