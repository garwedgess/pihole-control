package eu.wedgess.piholecontrol.data.model.responses

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class PiHoleLogsResponseData(
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
        @Serializable(with = PiHoleLogEntryTypeSerializer::class)
        val type: PiHoleLogEntryType,
        @Serializable(with = PiHoleLogEntryStatusSerializer::class)
        val status: PiHoleLogEntryStatus,
        @Serializable(with = PiHoleLogEntryDnssecSerializer::class)
        val dnssec: PiHoleLogEntryDnssec,
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
            @Serializable(with = PiHoleLogEntryReplyTypeSerializer::class)
            val type: PiHoleLogEntryReplyType,
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
enum class PiHoleLogEntryType(val key: String) {
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
    HTTPS("HTTPS"),

    @SerialName("NONE")
    NONE("NONE");

    companion object {
        operator fun get(value: String): PiHoleLogEntryType =
            requireNotNull(entries.find { it.key == value }) {
                "No ${PiHoleLogEntryType::class.java.simpleName} found for value: $value"
            }
    }
}

@Serializable
enum class PiHoleLogEntryStatus(val key: String) {
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
        operator fun get(value: String): PiHoleLogEntryStatus =
            requireNotNull(PiHoleLogEntryStatus.entries.find { it.key == value }) {
                "No ${PiHoleLogEntryStatus::class.java.simpleName} found for value: $value"
            }
    }
}

@Serializable
enum class PiHoleLogEntryReplyType(val key: String) {
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
        operator fun get(value: String): PiHoleLogEntryReplyType =
            requireNotNull(PiHoleLogEntryReplyType.entries.find { it.key == value }) {
                "No ${PiHoleLogEntryReplyType::class.java.simpleName} found for value: $value"
            }
    }
}

@Serializable
enum class PiHoleLogEntryDnssec(val key: String) {
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
        operator fun get(value: String): PiHoleLogEntryDnssec =
            requireNotNull(PiHoleLogEntryDnssec.entries.find { it.key == value }) {
                "No ${PiHoleLogEntryDnssec::class.java.simpleName} found for value: $value"
            }
    }
}

// Custom Serializers
class PiHoleLogEntryTypeSerializer : KSerializer<PiHoleLogEntryType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(PiHoleLogEntryType::class.java.simpleName, PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: PiHoleLogEntryType) {
        encoder.encodeString(value.key)
    }

    override fun deserialize(decoder: Decoder): PiHoleLogEntryType {
        return PiHoleLogEntryType[decoder.decodeString()]
    }
}

class PiHoleLogEntryStatusSerializer : KSerializer<PiHoleLogEntryStatus> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(
            PiHoleLogEntryStatus::class.java.simpleName,
            PrimitiveKind.STRING
        )

    override fun serialize(encoder: Encoder, value: PiHoleLogEntryStatus) {
        encoder.encodeString(value.key)
    }

    override fun deserialize(decoder: Decoder): PiHoleLogEntryStatus {
        return PiHoleLogEntryStatus[decoder.decodeString()]
    }
}

class PiHoleLogEntryReplyTypeSerializer : KSerializer<PiHoleLogEntryReplyType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(
            PiHoleLogEntryReplyType::class.java.simpleName,
            PrimitiveKind.STRING
        )

    override fun serialize(encoder: Encoder, value: PiHoleLogEntryReplyType) {
        encoder.encodeString(value.key)
    }

    override fun deserialize(decoder: Decoder): PiHoleLogEntryReplyType {
        return PiHoleLogEntryReplyType[decoder.decodeString()]
    }
}

class PiHoleLogEntryDnssecSerializer : KSerializer<PiHoleLogEntryDnssec> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(
            PiHoleLogEntryDnssec::class.java.simpleName,
            PrimitiveKind.STRING
        )

    override fun serialize(encoder: Encoder, value: PiHoleLogEntryDnssec) {
        encoder.encodeString(value.key)
    }

    override fun deserialize(decoder: Decoder): PiHoleLogEntryDnssec {
        return PiHoleLogEntryDnssec[decoder.decodeString()]
    }
}
