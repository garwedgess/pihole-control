package eu.wedgess.piholecontrol.data.model.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class LogsAnswerType(val key: String, val category: LogsAnswerCategory) {
    @SerialName("1")
    GRAVITY_BLOCK(key = "GRAVITY_BLOCK", LogsAnswerCategory.BLOCK),

    @SerialName("2")
    UPSTREAM(key = "UPSTREAM", LogsAnswerCategory.ALLOW),

    @SerialName("3")
    LOCAL_CACHE(key = "LOCAL_CACHE", LogsAnswerCategory.CACHE),

    @SerialName("4")
    REGEX_BLOCK(key = "REGEX_BLOCK", LogsAnswerCategory.BLOCK),

    @SerialName("5")
    EXACT_BLOCK(key = "EXACT_BLOCK", LogsAnswerCategory.BLOCK),

    @SerialName("6")
    EXTERNAL_IP_BLOCK(key = "EXTERNAL_IP_BLOCK", LogsAnswerCategory.BLOCK),

    @SerialName("7")
    EXTERNAL_NULL_BLOCK(key = "EXTERNAL_NULL_BLOCK", LogsAnswerCategory.BLOCK),

    @SerialName("8")
    EXTERNAL_NXRA_BLOCK(key = "EXTERNAL_NXRA_BLOCK", LogsAnswerCategory.BLOCK),

    @SerialName("9")
    CNAME_GRAVITY_BLOCK(key = "CNAME_GRAVITY_BLOCK", LogsAnswerCategory.BLOCK),

    @SerialName("10")
    CNAME_REGEX_BLOCK(key = "CNAME_REGEX_BLOCK", LogsAnswerCategory.BLOCK),

    @SerialName("11")
    CNAME_EXACT_BLOCK(key = "CNAME_EXACT_BLOCK", LogsAnswerCategory.BLOCK),

    @SerialName("12")
    RETRIED(key = "RETRIED", LogsAnswerCategory.ALLOW),

    @SerialName("13")
    RETRIED_IGNORED(key = "RETRIED_IGNORED", LogsAnswerCategory.ALLOW),

    @SerialName("14")
    ALREADY_FORWARDED(key = "ALREADY_FORWARDED", LogsAnswerCategory.ALLOW),

    UNKNOWN(key = "UNKNOWN", LogsAnswerCategory.UNKNOWN)
}
