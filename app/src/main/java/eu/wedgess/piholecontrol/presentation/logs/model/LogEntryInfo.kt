package eu.wedgess.piholecontrol.presentation.logs.model

import androidx.compose.runtime.Composable
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.LogAnswerCategoryEntity
import eu.wedgess.piholecontrol.domain.model.PiHoleLogsEntity
import eu.wedgess.piholecontrol.presentation.logs.extensions.toColor
import eu.wedgess.piholecontrol.presentation.logs.extensions.toIcon
import eu.wedgess.piholecontrol.presentation.logs.extensions.toStringValue
import eu.wedgess.piholecontrol.utils.UiText
import java.util.Locale

sealed class LogEntryInfo {

    abstract val timestamp: Long
    abstract val client: String
    abstract val domain: String
    abstract val time: String
    abstract val replyTime: Double
    abstract val formattedReplyTime: UiText

    data class Version5(
        override val timestamp: Long,
        override val client: String,
        override val domain: String,
        override val time: String,
        override val replyTime: Double,
        val queryType: String,
        val answerType: PiHoleLogsEntity.LogsAnswerTypeEntity,
    ) : LogEntryInfo() {
        override val formattedReplyTime: UiText
            get() =
                UiText.StringResourceWithArgs(
                    R.string.log_version_5_response_time_postfix,
                    String.format(Locale.UK, "%.1f", replyTime * 0.1)
                )
    }

    data class Version6(
        override val timestamp: Long,
        override val client: String,
        override val domain: String,
        override val time: String,
        override val replyTime: Double,
        val id: Int,
        val type: PiHoleLogsEntity.LogEntryTypeEntity,
        val status: PiHoleLogsEntity.LogEntryStatusEntity,
        val dnssec: PiHoleLogsEntity.LogEntryDnssecEntity,
        val replyType: PiHoleLogsEntity.LogEntryReplyTypeEntity,
        val listId: Int?,
        val edeCode: Int,
        val edeText: String?,
        val cname: String?
    ) : LogEntryInfo() {
        override val formattedReplyTime: UiText
            get() =
                UiText.DynamicString(convertToSeconds(replyTime))

        private fun convertToSeconds(value: Double): String {
            if (value == 0.0) return "0.0 s"
            val valueInMs = value * 1000

            return when {
                valueInMs < 0.095 -> {
                    val microseconds = value * 1000000
                    String.format(Locale.UK, "%.1f µs", microseconds)
                }

                valueInMs < 1000 -> {
                    String.format(Locale.UK, "%.1f ms", valueInMs)
                }

                else -> {
                    String.format(Locale.UK, "%.1f s", value)
                }
            }
        }
    }

    @Composable
    fun icon() = when (this) {
        is Version5 -> this.answerType.toIcon()
        is Version6 -> this.status.toIcon()
    }

    @Composable
    fun color() = when (this) {
        is Version5 -> this.answerType.toColor()
        is Version6 -> this.status.toColor()
    }

    @Composable
    fun stringValue() = when (this) {
        is Version5 -> this.answerType.toStringValue()
        is Version6 -> this.status.toStringValue()
    }

    val queryTypeString: String
        get() = when (this) {
            is Version5 -> this.queryType
            is Version6 -> this.type.key
        }

    val statusString: String
        get() = when (this) {
            is Version5 -> this.answerType.category.name
            is Version6 -> this.status.category.name
        }

    val isBlocked: Boolean
        get() = when (this) {
            is Version5 -> this.answerType.category == LogAnswerCategoryEntity.BLOCK
            is Version6 -> this.status.category == LogAnswerCategoryEntity.BLOCK
        }

    val isAllowed: Boolean
        get() = when (this) {
            is Version5 -> {
                this.answerType.category == LogAnswerCategoryEntity.ALLOW ||
                        this.answerType.category == LogAnswerCategoryEntity.CACHE
            }

            is Version6 -> {
                this.status.category == LogAnswerCategoryEntity.ALLOW ||
                        this.status.category == LogAnswerCategoryEntity.CACHE
            }
        }
}
