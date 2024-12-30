package eu.wedgess.piholecontrol.data.model.responses.v6

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class PiHoleSummaryV6Data(
    @SerialName("queries") val queries: PiHoleSummaryQueriesV6Data = PiHoleSummaryQueriesV6Data(),
    @SerialName("clients") val clients: PiHoleSummaryClientsV6Data = PiHoleSummaryClientsV6Data(),
    @SerialName("gravity") val gravity: PiHoleSummaryGravityV6Data = PiHoleSummaryGravityV6Data(),
    @SerialName("took") val took: Double = 0.0
)

@Serializable
data class PiHoleSummaryClientsV6Data(
    @SerialName("active") val active: Int = 0,
    @SerialName("total") val total: Int = 0
)

@Serializable
data class PiHoleSummaryGravityV6Data(
    @SerialName("domains_being_blocked") val domainsBeingBlocked: Int = 0,
    @SerialName("last_update") val lastUpdate: Int = 0
)

@Serializable
data class PiHoleSummaryQueriesV6Data(
    @SerialName("total") val total: Int = 0,
    @SerialName("blocked") val blocked: Int = 0,
    @SerialName("percent_blocked") val percentBlocked: Float = 0f,
    @SerialName("unique_domains") val uniqueDomains: Int = 0,
    @SerialName("forwarded") val forwarded: Int = 0,
    @SerialName("cached") val cached: Int = 0,
    @SerialName("frequency") val frequency: Int = 0,
    @SerialName("types") val types: PiHoleSummaryTypesV6Data = PiHoleSummaryTypesV6Data(),
    @SerialName("status") val status: PiHoleSummaryStatusV6Data = PiHoleSummaryStatusV6Data(),
    @SerialName("replies") val replies: PiHoleSummaryRepliesV6Data = PiHoleSummaryRepliesV6Data()
)

@Serializable
data class PiHoleSummaryTypesV6Data(
    @SerialName("A") val A: Int = 0,
    @SerialName("AAAA") val AAAA: Int = 0,
    @SerialName("ANY") val ANY: Int = 0,
    @SerialName("SRV") val SRV: Int = 0,
    @SerialName("SOA") val SOA: Int = 0,
    @SerialName("PTR") val PTR: Int = 0,
    @SerialName("TXT") val TXT: Int = 0,
    @SerialName("NAPTR") val NAPTR: Int = 0,
    @SerialName("MX") val MX: Int = 0,
    @SerialName("DS") val DS: Int = 0,
    @SerialName("RRSIG") val RRSIG: Int = 0,
    @SerialName("DNSKEY") val DNSKEY: Int = 0,
    @SerialName("NS") val NS: Int = 0,
    @SerialName("SVCB") val SVCB: Int = 0,
    @SerialName("HTTPS") val HTTPS: Int = 0,
    @SerialName("OTHER") val OTHER: Int = 0
)

@Serializable
data class PiHoleSummaryStatusV6Data(
    @SerialName("UNKNOWN") val UNKNOWN: Int = 0,
    @SerialName("GRAVITY") val GRAVITY: Int = 0,
    @SerialName("FORWARDED") val FORWARDED: Int = 0,
    @SerialName("CACHE") val CACHE: Int = 0,
    @SerialName("REGEX") val REGEX: Int = 0,
    @SerialName("DENYLIST") val DENYLIST: Int = 0,
    @SerialName("EXTERNAL_BLOCKED_IP") val EXTERNALBLOCKEDIP: Int = 0,
    @SerialName("EXTERNAL_BLOCKED_NULL") val EXTERNALBLOCKEDNULL: Int = 0,
    @SerialName("EXTERNAL_BLOCKED_NXRA") val EXTERNALBLOCKEDNXRA: Int = 0,
    @SerialName("GRAVITY_CNAME") val GRAVITYCNAME: Int = 0,
    @SerialName("REGEX_CNAME") val REGEXCNAME: Int = 0,
    @SerialName("DENYLIST_CNAME") val DENYLISTCNAME: Int = 0,
    @SerialName("RETRIED") val RETRIED: Int = 0,
    @SerialName("RETRIED_DNSSEC") val RETRIEDDNSSEC: Int = 0,
    @SerialName("IN_PROGRESS") val INPROGRESS: Int = 0,
    @SerialName("DBBUSY") val DBBUSY: Int = 0,
    @SerialName("SPECIAL_DOMAIN") val SPECIALDOMAIN: Int = 0,
    @SerialName("CACHE_STALE") val CACHESTALE: Int = 0,
    @SerialName("EXTERNAL_BLOCKED_EDE15") val EXTERNALBLOCKEDEDE15: Int = 0
)

@Serializable
data class PiHoleSummaryRepliesV6Data(
    @SerialName("UNKNOWN") val UNKNOWN: Int = 0,
    @SerialName("NODATA") val NODATA: Int = 0,
    @SerialName("NXDOMAIN") val NXDOMAIN: Int = 0,
    @SerialName("CNAME") val CNAME: Int = 0,
    @SerialName("IP") val IP: Int = 0,
    @SerialName("DOMAIN") val DOMAIN: Int = 0,
    @SerialName("RRNAME") val RRNAME: Int = 0,
    @SerialName("SERVFAIL") val SERVFAIL: Int = 0,
    @SerialName("REFUSED") val REFUSED: Int = 0,
    @SerialName("NOTIMP") val NOTIMP: Int = 0,
    @SerialName("OTHER") val OTHER: Int = 0,
    @SerialName("DNSSEC") val DNSSEC: Int = 0,
    @SerialName("NONE") val NONE: Int = 0,
    @SerialName("BLOB") val BLOB: Int = 0
)
