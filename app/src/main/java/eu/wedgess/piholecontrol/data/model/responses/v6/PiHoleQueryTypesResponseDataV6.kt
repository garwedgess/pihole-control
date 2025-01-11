package eu.wedgess.piholecontrol.data.model.responses.v6

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.floor

@Serializable
data class PiHoleQueryTypesResponseDataV6(
    val types: QueryTypes,
    val took: Double
) {

    @Serializable
    data class QueryTypes(
        @SerialName("A") val a: Float,
        @SerialName("AAAA") val aaaa: Float,
        @SerialName("ANY") val any: Float,
        @SerialName("SRV") val srv: Float,
        @SerialName("SOA") val soa: Float,
        @SerialName("PTR") val ptr: Float,
        @SerialName("TXT") val txt: Float,
        @SerialName("NAPTR") val naptr: Float,
        @SerialName("MX") val mx: Float,
        @SerialName("DS") val ds: Float,
        @SerialName("RRSIG") val rrsig: Float,
        @SerialName("DNSKEY") val dnskey: Float,
        @SerialName("NS") val ns: Float,
        @SerialName("SVCB") val svcb: Float,
        @SerialName("HTTPS") val https: Float,
        @SerialName("OTHER") val other: Float
    ) {
        fun asList(): List<Pair<String, Float>> {
            val queryTypePairs = listOf(
                Pair("A (IPv4)", a),
                Pair("AAAA (IPv6)", aaaa),
                Pair("ANY", any),
                Pair("SRV", srv),
                Pair("SOA", soa),
                Pair("PTR", ptr),
                Pair("TXT", txt),
                Pair("NAPTR", naptr),
                Pair("MX", mx),
                Pair("DS", ds),
                Pair("RRSIG", rrsig),
                Pair("DNSKey", dnskey),
                Pair("NS", ns),
                Pair("OTHER", other),
                Pair("SVCB", svcb),
                Pair("HTTPS", https)
            )

            val total = queryTypePairs.sumOf { it.second.toDouble() }.toFloat()

            return if (total == 0f) {
                emptyList()
            } else {
                queryTypePairs.map { (name, count) ->
                    val percentage = (count / total * 100)
                    val truncatedPercentage = floor(percentage * 100) / 100
                    name to truncatedPercentage
                }.filterNot { it.second == 0f }
            }
        }
    }
}
