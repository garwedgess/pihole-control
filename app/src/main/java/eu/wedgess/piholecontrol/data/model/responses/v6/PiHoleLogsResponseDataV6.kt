package eu.wedgess.piholecontrol.data.model.responses.v6

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class PiHoleLogsResponseDataV6(
    val queries: List<PiHoleLogEntryData>,
    val cursor: Int,
    val recordsTotal: Int,
    val recordsFiltered: Int,
    val draw: Int,
    val took: Double
) {

    @Serializable
    data class PiHoleLogEntryData(
        val id: Int,
        val time: Double,
        @Serializable(with = PiHoleLogEntryTypeV6Serializer::class)
        val type: PiHoleLogEntryTypeV6,
        @Serializable(with = PiHoleLogEntryStatusV6Serializer::class)
        val status: PiHoleLogEntryStatusV6,
        @Serializable(with = PiHoleLogEntryDnssecV6Serializer::class)
        val dnssec: PiHoleLogEntryDnssecV6,
        val domain: String,
        val upstream: String?,
        val reply: PiHoleLogEntryReply,
        val client: PiHoleLogEntryClient,
        @SerialName("list_id")
        val listId: Int?,
        val ede: PiHoleLogEntryEde,
        val cname: String?
    ) {
        @Serializable
        data class PiHoleLogEntryClient(
            val ip: String,
            val name: String?
        ) {
            val combinedName: String get() = if (name.isNullOrBlank()) ip else "$name|$ip"
        }

        @Serializable
        data class PiHoleLogEntryReply(
            @Serializable(with = PiHoleLogEntryReplyTypeV6Serializer::class)
            val type: PiHoleLogEntryReplyTypeV6,
            val time: Double
        )

        @Serializable
        data class PiHoleLogEntryEde(
            val code: Int,
            val text: String?
        )
    }
}

@Serializable
enum class PiHoleLogEntryTypeV6(val key: String) {
    @SerialName("A")
    A("A"),

    @SerialName("AAAA")
    AAAA("AAAA"),

    @SerialName("ANY")
    ANY("ANY"),

    @SerialName("SRV")
    SRV("SRV"),

    @SerialName("SOA")
    SOA("SOA"),

    @SerialName("PTR")
    PTR("PTR"),

    @SerialName("TXT")
    TXT("TXT"),

    @SerialName("NAPTR")
    NAPTR("NAPTR"),

    @SerialName("MX")
    MX("MX"),

    @SerialName("DS")
    DS("DS"),

    @SerialName("RRSIG")
    RRSIG("RRSIG"),

    @SerialName("DNSKEY")
    DNSKEY("DNSKEY"),

    @SerialName("NS")
    NS("NS"),

    @SerialName("OTHER")
    OTHER("OTHER"),

    @SerialName("SVCB")
    SVCB("SVCB"),

    @SerialName("HTTPS")
    HTTPS("HTTPS");

    companion object {
        operator fun get(value: String): PiHoleLogEntryTypeV6 =
            requireNotNull(entries.find { it.key == value }) {
                "No ${PiHoleLogEntryTypeV6::class.java.simpleName} found for value: $value"
            }
    }
}

@Serializable
enum class PiHoleLogEntryStatusV6(val key: String) {
    @SerialName("UNKNOWN")
    UNKNOWN("UNKNOWN"),

    @SerialName("GRAVITY")
    GRAVITY("GRAVITY"),

    @SerialName("FORWARDED")
    FORWARDED("FORWARDED"),

    @SerialName("CACHE")
    CACHE("CACHE"),

    @SerialName("REGEX")
    REGEX("REGEX"),

    @SerialName("DENYLIST")
    DENYLIST("DENYLIST"),

    @SerialName("EXTERNAL_BLOCKED_IP")
    EXTERNAL_BLOCKED_IP("EXTERNAL_BLOCKED_IP"),

    @SerialName("EXTERNAL_BLOCKED_NULL")
    EXTERNAL_BLOCKED_NULL("EXTERNAL_BLOCKED_NULL"),

    @SerialName("EXTERNAL_BLOCKED_NXRA")
    EXTERNAL_BLOCKED_NXRA("EXTERNAL_BLOCKED_NXRA"),

    @SerialName("GRAVITY_CNAME")
    GRAVITY_CNAME("GRAVITY_CNAME"),

    @SerialName("REGEX_CNAME")
    REGEX_CNAME("REGEX_CNAME"),

    @SerialName("DENYLIST_CNAME")
    DENYLIST_CNAME("DENYLIST_CNAME"),

    @SerialName("RETRIED")
    RETRIED("RETRIED"),

    @SerialName("RETRIED_DNSSEC")
    RETRIED_DNSSEC("RETRIED_DNSSEC"),

    @SerialName("IN_PROGRESS")
    IN_PROGRESS("IN_PROGRESS"),

    @SerialName("DBBUSY")
    DBBUSY("DBBUSY"),

    @SerialName("SPECIAL_DOMAIN")
    SPECIAL_DOMAIN("SPECIAL_DOMAIN"),

    @SerialName("CACHE_STALE")
    CACHE_STALE("CACHE_STALE"),

    @SerialName("EXTERNAL_BLOCKED_EDE15")
    EXTERNAL_BLOCKED_EDE15("EXTERNAL_BLOCKED_EDE15");

    companion object {
        operator fun get(value: String): PiHoleLogEntryStatusV6 =
            requireNotNull(PiHoleLogEntryStatusV6.entries.find { it.key == value }) {
                "No ${PiHoleLogEntryStatusV6::class.java.simpleName} found for value: $value"
            }
    }
}

@Serializable
enum class PiHoleLogEntryReplyTypeV6(val key: String) {
    @SerialName("UNKNOWN")
    UNKNOWN("UNKNOWN"),

    @SerialName("NODATA")
    NODATA("NODATA"),

    @SerialName("NXDOMAIN")
    NXDOMAIN("NXDOMAIN"),

    @SerialName("CNAME")
    CNAME("CNAME"),

    @SerialName("IP")
    IP("IP"),

    @SerialName("DOMAIN")
    DOMAIN("DOMAIN"),

    @SerialName("RRNAME")
    RRNAME("RRNAME"),

    @SerialName("SERVFAIL")
    SERVFAIL("SERVFAIL"),

    @SerialName("REFUSED")
    REFUSED("REFUSED"),

    @SerialName("NOTIMP")
    NOTIMP("NOTIMP"),

    @SerialName("OTHER")
    OTHER("OTHER"),

    @SerialName("DNSSEC")
    DNSSEC("DNSSEC"),

    @SerialName("NONE")
    NONE("NONE"),

    @SerialName("BLOB")
    BLOB("BLOB");

    companion object {
        operator fun get(value: String): PiHoleLogEntryReplyTypeV6 =
            requireNotNull(PiHoleLogEntryReplyTypeV6.entries.find { it.key == value }) {
                "No ${PiHoleLogEntryReplyTypeV6::class.java.simpleName} found for value: $value"
            }
    }
}

@Serializable
enum class PiHoleLogEntryDnssecV6(val key: String) {
    @SerialName("UNKNOWN")
    UNKNOWN("UNKNOWN"),

    @SerialName("SECURE")
    SECURE("SECURE"),

    @SerialName("INSECURE")
    INSECURE("INSECURE"),

    @SerialName("BOGUS")
    BOGUS("BOGUS"),

    @SerialName("ABANDONED")
    ABANDONED("ABANDONED"),

    @SerialName("TRUNCATED")
    TRUNCATED("TRUNCATED");

    companion object {
        operator fun get(value: String): PiHoleLogEntryDnssecV6 =
            requireNotNull(PiHoleLogEntryDnssecV6.entries.find { it.key == value }) {
                "No ${PiHoleLogEntryDnssecV6::class.java.simpleName} found for value: $value"
            }
    }
}

// Custom Serializers
class PiHoleLogEntryTypeV6Serializer : KSerializer<PiHoleLogEntryTypeV6> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(PiHoleLogEntryTypeV6::class.java.simpleName, PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: PiHoleLogEntryTypeV6) {
        encoder.encodeString(value.key)
    }

    override fun deserialize(decoder: Decoder): PiHoleLogEntryTypeV6 {
        return PiHoleLogEntryTypeV6[decoder.decodeString()]
    }
}

class PiHoleLogEntryStatusV6Serializer : KSerializer<PiHoleLogEntryStatusV6> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(
            PiHoleLogEntryStatusV6::class.java.simpleName,
            PrimitiveKind.STRING
        )

    override fun serialize(encoder: Encoder, value: PiHoleLogEntryStatusV6) {
        encoder.encodeString(value.key)
    }

    override fun deserialize(decoder: Decoder): PiHoleLogEntryStatusV6 {
        return PiHoleLogEntryStatusV6[decoder.decodeString()]
    }
}

class PiHoleLogEntryReplyTypeV6Serializer : KSerializer<PiHoleLogEntryReplyTypeV6> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(
            PiHoleLogEntryReplyTypeV6::class.java.simpleName,
            PrimitiveKind.STRING
        )

    override fun serialize(encoder: Encoder, value: PiHoleLogEntryReplyTypeV6) {
        encoder.encodeString(value.key)
    }

    override fun deserialize(decoder: Decoder): PiHoleLogEntryReplyTypeV6 {
        return PiHoleLogEntryReplyTypeV6[decoder.decodeString()]
    }
}

class PiHoleLogEntryDnssecV6Serializer : KSerializer<PiHoleLogEntryDnssecV6> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(
            PiHoleLogEntryDnssecV6::class.java.simpleName,
            PrimitiveKind.STRING
        )

    override fun serialize(encoder: Encoder, value: PiHoleLogEntryDnssecV6) {
        encoder.encodeString(value.key)
    }

    override fun deserialize(decoder: Decoder): PiHoleLogEntryDnssecV6 {
        return PiHoleLogEntryDnssecV6[decoder.decodeString()]
    }
}
