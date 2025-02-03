package eu.wedgess.piholecontrol.domain.model

sealed class PiHoleLogsEntity {

    abstract val timestamp: Long
    abstract val client: String
    abstract val domain: String
    abstract val time: String
    abstract val replyTime: Double
    abstract val queryType: LogEntryQueryTypeEntity

    data class Version5(
        override val timestamp: Long,
        override val client: String,
        override val domain: String,
        override val time: String,
        override val replyTime: Double,
        override val queryType: LogEntryQueryTypeEntity,
        val answerType: LogsAnswerTypeEntity,
    ) : PiHoleLogsEntity()

    data class Version6(
        override val timestamp: Long,
        override val client: String,
        override val domain: String,
        override val time: String,
        override val replyTime: Double,
        override val queryType: LogEntryQueryTypeEntity,
        val id: Int,
        val status: LogEntryStatusEntity,
        val dnssec: LogEntryDnssecEntity,
        val replyType: LogEntryReplyTypeEntity,
        val listId: Int?,
        val edeCode: Int,
        val edeText: String?,
        val cname: String?
    ) : PiHoleLogsEntity()

    enum class LogsAnswerTypeEntity(val key: String, val category: LogAnswerCategoryEntity) {
        GRAVITY_BLOCK("GRAVITY_BLOCK", LogAnswerCategoryEntity.BLOCK),
        UPSTREAM("UPSTREAM", LogAnswerCategoryEntity.ALLOW),
        LOCAL_CACHE("LOCAL_CACHE", LogAnswerCategoryEntity.CACHE),
        REGEX_BLOCK("REGEX_BLOCK", LogAnswerCategoryEntity.BLOCK),
        EXACT_BLOCK("EXACT_BLOCK", LogAnswerCategoryEntity.BLOCK),
        EXTERNAL_IP_BLOCK("EXTERNAL_IP_BLOCK", LogAnswerCategoryEntity.BLOCK),
        EXTERNAL_NULL_BLOCK("EXTERNAL_NULL_BLOCK", LogAnswerCategoryEntity.BLOCK),
        EXTERNAL_NXRA_BLOCK("EXTERNAL_NXRA_BLOCK", LogAnswerCategoryEntity.BLOCK),
        CNAME_GRAVITY_BLOCK("CNAME_GRAVITY_BLOCK", LogAnswerCategoryEntity.BLOCK),
        CNAME_REGEX_BLOCK("CNAME_REGEX_BLOCK", LogAnswerCategoryEntity.BLOCK),
        CNAME_EXACT_BLOCK("CNAME_EXACT_BLOCK", LogAnswerCategoryEntity.BLOCK),
        RETRIED("RETRIED", LogAnswerCategoryEntity.ALLOW),
        RETRIED_IGNORED("RETRIED_IGNORED", LogAnswerCategoryEntity.ALLOW),
        ALREADY_FORWARDED("ALREADY_FORWARDED", LogAnswerCategoryEntity.ALLOW),
        UNKNOWN("UNKNOWN", LogAnswerCategoryEntity.UNKNOWN);

        companion object {
            operator fun get(key: String) =
                requireNotNull(LogsAnswerTypeEntity.entries.find { it.key == key }) {
                    "No value found for ${LogsAnswerTypeEntity::class.java.simpleName} with key: $key"
                }
        }
    }

    enum class LogEntryStatusEntity(val key: String, val category: LogAnswerCategoryEntity) {
        UNKNOWN("UNKNOWN", LogAnswerCategoryEntity.UNKNOWN),
        GRAVITY("GRAVITY", LogAnswerCategoryEntity.BLOCK),
        FORWARDED("FORWARDED", LogAnswerCategoryEntity.ALLOW),
        CACHE("CACHE", LogAnswerCategoryEntity.CACHE),
        REGEX("REGEX", LogAnswerCategoryEntity.BLOCK),
        DENYLIST("DENYLIST", LogAnswerCategoryEntity.BLOCK),
        EXTERNAL_BLOCKED_IP("EXTERNAL_BLOCKED_IP", LogAnswerCategoryEntity.BLOCK),
        EXTERNAL_BLOCKED_NULL("EXTERNAL_BLOCKED_NULL", LogAnswerCategoryEntity.BLOCK),
        EXTERNAL_BLOCKED_NXRA("EXTERNAL_BLOCKED_NXRA", LogAnswerCategoryEntity.BLOCK),
        GRAVITY_CNAME("GRAVITY_CNAME", LogAnswerCategoryEntity.BLOCK),
        REGEX_CNAME("REGEX_CNAME", LogAnswerCategoryEntity.BLOCK),
        DENYLIST_CNAME("DENYLIST_CNAME", LogAnswerCategoryEntity.BLOCK),
        RETRIED("RETRIED", LogAnswerCategoryEntity.ALLOW),
        RETRIED_DNSSEC("RETRIED_DNSSEC", LogAnswerCategoryEntity.ALLOW),
        IN_PROGRESS("IN_PROGRESS", LogAnswerCategoryEntity.ALLOW),
        DBBUSY("DBBUSY", LogAnswerCategoryEntity.UNKNOWN),
        SPECIAL_DOMAIN("SPECIAL_DOMAIN", LogAnswerCategoryEntity.UNKNOWN),
        CACHE_STALE("CACHE_STALE", LogAnswerCategoryEntity.CACHE),
        EXTERNAL_BLOCKED_EDE15("EXTERNAL_BLOCKED_EDE15", LogAnswerCategoryEntity.BLOCK);

        companion object {
            operator fun get(key: String) =
                requireNotNull(LogEntryStatusEntity.entries.find { it.key == key }) {
                    "No value found for ${LogEntryStatusEntity::class.java.simpleName} with key: $key"
                }
        }
    }

    enum class LogEntryDnssecEntity(val key: String) {
        UNKNOWN("UNKNOWN"),
        SECURE("SECURE"),
        INSECURE("INSECURE"),
        BOGUS("BOGUS"),
        ABANDONED("ABANDONED"),
        TRUNCATED("TRUNCATED");

        companion object {
            operator fun get(key: String) =
                requireNotNull(LogEntryDnssecEntity.entries.find { it.key == key }) {
                    "No value found for ${LogEntryDnssecEntity::class.java.simpleName} with key: $key"
                }
        }
    }

    enum class LogEntryQueryTypeEntity(val key: String) {
        A("A"),
        AAAA("AAAA"),
        ANY("ANY"),
        SRV("SRV"),
        SOA("SOA"),
        PTR("PTR"),
        TXT("TXT"),
        NAPTR("NAPTR"),
        MX("MX"),
        DS("DS"),
        RRSIG("RRSIG"),
        DNSKEY("DNSKEY"),
        NS("NS"),
        OTHER("OTHER"),
        SVCB("SVCB"),
        HTTPS("HTTPS");

        companion object {
            operator fun get(key: String) =
                requireNotNull(LogEntryQueryTypeEntity.entries.find { it.key == key }) {
                    "No value found for ${LogEntryQueryTypeEntity::class.java.simpleName} with key: $key"
                }
        }
    }

    enum class LogEntryReplyTypeEntity(val key: String) {
        UNKNOWN("UNKNOWN"),
        NODATA("NODATA"),
        NXDOMAIN("NXDOMAIN"),
        CNAME("CNAME"),
        IP("IP"),
        DOMAIN("DOMAIN"),
        RRNAME("RRNAME"),
        SERVFAIL("SERVFAIL"),
        REFUSED("REFUSED"),
        NOTIMP("NOTIMP"),
        OTHER("OTHER"),
        DNSSEC("DNSSEC"),
        NONE("NONE"),
        BLOB("BLOB");

        companion object {
            operator fun get(key: String) =
                requireNotNull(LogEntryReplyTypeEntity.entries.find { it.key == key }) {
                    "No value found for ${LogEntryReplyTypeEntity::class.java.simpleName} with key: $key"
                }
        }
    }
}
