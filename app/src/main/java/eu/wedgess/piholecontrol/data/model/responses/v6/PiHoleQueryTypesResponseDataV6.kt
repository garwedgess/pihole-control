package eu.wedgess.piholecontrol.data.model.responses.v6

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.floor

typealias QueryTypeValueDataV6 = Pair<String, Float>

@Serializable
data class PiHoleQueryTypesResponseDataV6(
    @SerialName("types")
    val queryTypes: QueryTypes,
    val took: Double
) {

    @Serializable
    data class QueryTypes(
        @SerialName("A") val a: Int,
        @SerialName("AAAA") val aaaa: Int,
        @SerialName("ANY") val any: Int,
        @SerialName("SRV") val srv: Int,
        @SerialName("SOA") val soa: Int,
        @SerialName("PTR") val ptr: Int,
        @SerialName("TXT") val txt: Int,
        @SerialName("NAPTR") val naptr: Int,
        @SerialName("MX") val mx: Int,
        @SerialName("DS") val ds: Int,
        @SerialName("RRSIG") val rrsig: Int,
        @SerialName("DNSKEY") val dnskey: Int,
        @SerialName("NS") val ns: Int,
        @SerialName("SVCB") val svcb: Int,
        @SerialName("HTTPS") val https: Int,
        @SerialName("OTHER") val other: Int
    ) {
        fun asList(): List<QueryTypeValueDataV6> {
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

            val total = queryTypePairs.sumOf { it.second }.toFloat()

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
