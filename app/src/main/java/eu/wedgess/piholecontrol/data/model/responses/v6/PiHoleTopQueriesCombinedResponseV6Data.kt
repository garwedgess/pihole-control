package eu.wedgess.piholecontrol.data.model.responses.v6

import eu.wedgess.piholecontrol.data.model.TopQueryData
import kotlin.math.floor

data class PiHoleTopQueriesCombinedResponseV6Data(
    val permitted: List<PiHoleTopQueryDataV6>,
    val blocked: List<PiHoleTopQueryDataV6>,
) {
    val permittedWithPercentages: List<TopQueryData>
        get() = calculateWithPercentages(permitted)

    val blockedWithPercentages: List<TopQueryData>
        get() = calculateWithPercentages(blocked)

    private fun calculateWithPercentages(data: List<PiHoleTopQueryDataV6>): List<TopQueryData> {
        val combinedData = data.groupBy { it.domain }
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
            TopQueryData(entry.domain, entry.count, truncatedPercentage)
        }.sortedByDescending { it.hits }
    }
}
