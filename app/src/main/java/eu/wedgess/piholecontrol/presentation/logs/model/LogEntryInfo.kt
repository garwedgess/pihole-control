package eu.wedgess.piholecontrol.presentation.logs.model

import androidx.compose.runtime.Composable
import eu.wedgess.piholecontrol.domain.model.LogAnswerCategoryEntity
import eu.wedgess.piholecontrol.domain.model.PiHoleLogsEntity
import eu.wedgess.piholecontrol.presentation.logs.extensions.toColor
import eu.wedgess.piholecontrol.presentation.logs.extensions.toIcon
import eu.wedgess.piholecontrol.presentation.logs.extensions.toStringValue

sealed class LogEntryInfo {

    abstract val timestamp: Long
    abstract val client: String
    abstract val domain: String
    abstract val time: String
    abstract val replyTime: Double

    data class Version5(
        override val timestamp: Long,
        override val client: String,
        override val domain: String,
        override val time: String,
        override val replyTime: Double,
        val queryType: String,
        val answerType: PiHoleLogsEntity.LogsAnswerTypeEntity,
    ) : LogEntryInfo()

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
    ) : LogEntryInfo()

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
