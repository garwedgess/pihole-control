package eu.wedgess.piholecontrol.data.model.responses.v5

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class PiHoleSummaryResponseDataV5(
    @SerialName("domains_being_blocked")
    @Serializable(with = StringWithCommaToIntSerializer::class)
    val domainsBeingBlocked: Int = 0,
    @SerialName("dns_queries_today")
    @Serializable(with = StringWithCommaToIntSerializer::class)
    val dnsQueriesToday: Int = 0,
    @SerialName("ads_blocked_today")
    @Serializable(with = StringWithCommaToIntSerializer::class)
    val adsBlockedToday: Int = 0,
    @SerialName("ads_percentage_today")
    val adsPercentageToday: Float = 0f,
    @SerialName("unique_domains")
    @Serializable(with = StringWithCommaToIntSerializer::class)
    val uniqueDomains: Int = 0,
    @SerialName("queries_forwarded")
    @Serializable(with = StringWithCommaToIntSerializer::class)
    val queriesForwarded: Int = 0,
    @SerialName("queries_cached")
    @Serializable(with = StringWithCommaToIntSerializer::class)
    val queriesCached: Int = 0,
    @SerialName("clients_ever_seen")
    @Serializable(with = StringWithCommaToIntSerializer::class)
    val clientsEverSeen: Int = 0,
    @SerialName("unique_clients")
    @Serializable(with = StringWithCommaToIntSerializer::class)
    val uniqueClients: Int = 0,
    @SerialName("dns_queries_all_types")
    @Serializable(with = StringWithCommaToIntSerializer::class)
    val dnsQueriesAllTypes: Int = 0,
    @SerialName("reply_NODATA")
    @Serializable(with = StringWithCommaToIntSerializer::class)
    val replyNODATA: Int = 0,
    @SerialName("reply_NXDOMAIN")
    @Serializable(with = StringWithCommaToIntSerializer::class)
    val replyNXDOMAIN: Int = 0,
    @SerialName("reply_CNAME")
    @Serializable(with = StringWithCommaToIntSerializer::class)
    val replyCNAME: Int = 0,
    @SerialName("reply_IP")
    @Serializable(with = StringWithCommaToIntSerializer::class)
    val replyIP: Int = 0,
    @SerialName("privacy_level")
    val privacyLevel: Int = 0,
    @SerialName("status")
    val status: String = "",
    @SerialName("gravity_last_updated")
    val gravityLastUpdated: GravityLastUpdated = GravityLastUpdated()
) {
    @Serializable
    data class GravityLastUpdated(
        @SerialName("file_exists")
        val fileExists: Boolean = false,
        @SerialName("absolute")
        val absolute: Int = 0,
        @SerialName("relative")
        val relative: Relative = Relative()
    ) {
        @Serializable
        data class Relative(
            @SerialName("days")
            val days: Int = 0,
            @SerialName("hours")
            val hours: Int = 0,
            @SerialName("minutes")
            val minutes: Int = 0
        )
    }
}

private object StringWithCommaToIntSerializer : KSerializer<Int> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("StringWithCommaToInt", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Int) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Int {
        val string = decoder.decodeString()
        return string.replace(",", "").toInt()
    }
}
