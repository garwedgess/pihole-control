package eu.wedgess.piholecontrol.data.model.responses.v5

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias QueryTypeValueDataV5 = Pair<String, Float>

@Serializable
data class PiHoleQueryTypesResponseDataV5(
    @SerialName("querytypes")
    val queryTypes: QueryTypeData = QueryTypeData(),
) {

    @Serializable
    data class QueryTypeData(
        @SerialName("A (IPv4)")
        val aipV4: Float = 0f,
        @SerialName("AAAA (IPv6)")
        val aaaaIpV6: Float = 0f,
        @SerialName("ANY")
        val any: Float = 0f,
        @SerialName("SRV")
        val srv: Float = 0f,
        @SerialName("SOA")
        val soa: Float = 0f,
        @SerialName("PTR")
        val ptr: Float = 0f,
        @SerialName("TXT")
        val txt: Float = 0f,
        @SerialName("NAPTR")
        val naptr: Float = 0f,
        @SerialName("MX")
        val mx: Float = 0f,
        @SerialName("DS")
        val ds: Float = 0f,
        @SerialName("RRSIG")
        val rrsig: Float = 0f,
        @SerialName("DNSKEY")
        val dnsKey: Float = 0f,
        @SerialName("NS")
        val ns: Float = 0f,
        @SerialName("OTHER")
        val other: Float = 0f,
        @SerialName("SVCB")
        val svcb: Float = 0f,
        @SerialName("HTTPS")
        val http: Float = 0f
    ) {
        fun asList(): List<Pair<String, Float>> = listOf(
            QueryTypeValueDataV5("A (IPv4)", aipV4),
            QueryTypeValueDataV5("AAAA (IPv6)", aaaaIpV6),
            QueryTypeValueDataV5("ANY", any),
            QueryTypeValueDataV5("SRV", srv),
            QueryTypeValueDataV5("SOA", soa),
            QueryTypeValueDataV5("PTR", ptr),
            QueryTypeValueDataV5("TXT", txt),
            QueryTypeValueDataV5("NAPTR", naptr),
            QueryTypeValueDataV5("MX", mx),
            QueryTypeValueDataV5("DS", ds),
            QueryTypeValueDataV5("RRSIG", rrsig),
            QueryTypeValueDataV5("DNSKey", dnsKey),
            QueryTypeValueDataV5("NS", ns),
            QueryTypeValueDataV5("OTHER", other),
            QueryTypeValueDataV5("SVCB", svcb),
            QueryTypeValueDataV5("HTTPS", http)
        ).filterNot { it.second == 0f }
    }
}
