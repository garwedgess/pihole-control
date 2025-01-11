package eu.wedgess.piholecontrol.data.model.responses.v6

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.floor

@Serializable
data class PiHoleUpstreamsResponseDataV6(
    val upstreams: List<PiHoleUpstreamV6Data>,
    @SerialName("total_queries") val totalQueries: Int,
    @SerialName("forwarded_queries") val forwardedQueries: Int,
    val took: Double
) {

    val combinedUpstreamPercentages: List<Pair<PiHoleUpstreamV6Data, Float>>
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
data class PiHoleUpstreamV6Data(
    val ip: String,
    val name: String,
    val port: Int,
    val count: Int,
    val statistics: PiHoleUpstreamStatisticsV6Data
) {
    val combinedName: String get() = if (name.isNotBlank() && name != ip) "$name|$ip" else ip
}

@Serializable
data class PiHoleUpstreamStatisticsV6Data(
    val response: Double,
    val variance: Double
)
