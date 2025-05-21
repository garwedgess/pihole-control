package eu.wedgess.piholecontrol.data.model.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.floor

typealias UpstreamDestinationsWithPercentages = Pair<PiHoleUpstreamData, Float>

@Serializable
data class PiHoleUpstreamsResponseData(
    val upstreams: List<PiHoleUpstreamData>,
    @SerialName("total_queries") val totalQueries: Int,
    @SerialName("forwarded_queries") val forwardedQueries: Int,
    val took: Double
) {

    val combinedUpstreamPercentages: List<UpstreamDestinationsWithPercentages>
        get() {
            val combinedUpstreams = upstreams.groupBy { it.combinedName }
                .mapValues { (_, group) ->
                    group.reduce { acc, upstream ->
                        acc.copy(count = acc.count + upstream.count)
                    }
                }

            val globalTotalQueries = upstreams.sumOf { it.count }

            return combinedUpstreams.values.map { upstream ->
                val percentage = if (globalTotalQueries == 0) {
                    0f
                } else {
                    (upstream.count / globalTotalQueries.toFloat()) * 100f
                }
                val truncatedPercentage = floor(percentage * 100) / 100
                upstream to truncatedPercentage
            }.sortedByDescending { it.second }
        }
}

@Serializable
data class PiHoleUpstreamData(
    val ip: String,
    val name: String,
    val port: Int,
    val count: Int,
    val statistics: PiHoleUpstreamStatisticsData
) {
    val combinedName: String get() = if (name.isNotBlank() && name != ip) "$name|$ip" else ip
}

@Serializable
data class PiHoleUpstreamStatisticsData(
    val response: Double,
    val variance: Double
)
