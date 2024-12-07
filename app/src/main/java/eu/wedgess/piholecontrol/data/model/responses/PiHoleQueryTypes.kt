package eu.wedgess.piholecontrol.data.model.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleQueryTypes(
    @SerialName("querytypes")
    val queryTypes: QueryTypes = QueryTypes(),
)

@Serializable
data class QueryTypes(
    @SerialName("A (IPv4)")
    val AIPv4: Float = 0f,
    @SerialName("AAAA (IPv6)")
    val AAAAIPv6: Float = 0f,
    @SerialName("ANY")
    val any: Float = 0f,
    @SerialName("SRV")
    val SRV: Float = 0f,
    @SerialName("SOA")
    val SOA: Float = 0f,
    @SerialName("PTR")
    val PTR: Float = 0f,
    @SerialName("TXT")
    val TXT: Float = 0f,
    @SerialName("NAPTR")
    val NAPTR: Float = 0f,
    @SerialName("MX")
    val MX: Float = 0f,
    @SerialName("DS")
    val DS: Float = 0f,
    @SerialName("RRSIG")
    val RRSIG: Float = 0f,
    @SerialName("DNSKEY")
    val DNSKey: Float = 0f,
    @SerialName("NS")
    val NS: Float = 0f,
    @SerialName("OTHER")
    val other: Float = 0f,
    @SerialName("SVCB")
    val SVCB: Float = 0f,
    @SerialName("HTTPS")
    val HTTPS: Float = 0f
) {
    fun asList(): List<Pair<String, Float>> = listOf(
        Pair("A (IPv4)", AIPv4),
        Pair("AAAA (IPv6)", AAAAIPv6),
        Pair("ANY", any),
        Pair("SRV", SRV),
        Pair("SOA", SOA),
        Pair("PTR", PTR),
        Pair("TXT", TXT),
        Pair("NAPTR", NAPTR),
        Pair("MX", MX),
        Pair("DS", DS),
        Pair("RRSIG", RRSIG),
        Pair("DNSKey", DNSKey),
        Pair("NS", NS),
        Pair("OTHER", other),
        Pair("SVCB", SVCB),
        Pair("HTTPS", HTTPS)
    ).filterNot { it.second == 0f }
}
