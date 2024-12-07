package eu.wedgess.piholecontrol.data.model.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class LogsAnswerType(val category: LogsAnswerCategory) {
    @SerialName("1")
    GRAVITY_BLOCK(LogsAnswerCategory.BLOCK),

    @SerialName("2")
    UPSTREAM(LogsAnswerCategory.ALLOW),

    @SerialName("3")
    LOCAL_CACHE(LogsAnswerCategory.CACHE),

    @SerialName("4")
    REGEX_BLOCK(LogsAnswerCategory.BLOCK),

    @SerialName("5")
    EXACT_BLOCK(LogsAnswerCategory.BLOCK),

    @SerialName("6")
    EXTERNAL_IP_BLOCK(LogsAnswerCategory.BLOCK),

    @SerialName("7")
    EXTERNAL_NULL_BLOCK(LogsAnswerCategory.BLOCK),

    @SerialName("8")
    EXTERNAL_NXRA_BLOCK(LogsAnswerCategory.BLOCK),

    @SerialName("9")
    CNAME_GRAVITY_BLOCK(LogsAnswerCategory.BLOCK),

    @SerialName("10")
    CNAME_REGEX_BLOCK(LogsAnswerCategory.BLOCK),

    @SerialName("11")
    CNAME_EXACT_BLOCK(LogsAnswerCategory.BLOCK),

    @SerialName("12")
    RETRIED(LogsAnswerCategory.ALLOW),

    @SerialName("13")
    RETRIED_IGNORED(LogsAnswerCategory.ALLOW),

    @SerialName("14")
    ALREADY_FORWARDED(LogsAnswerCategory.ALLOW),

    UNKNOWN(LogsAnswerCategory.UNKNOWN)
}