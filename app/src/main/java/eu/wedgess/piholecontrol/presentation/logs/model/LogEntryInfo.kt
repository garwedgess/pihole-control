package eu.wedgess.piholecontrol.presentation.logs.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import eu.wedgess.piholecontrol.domain.model.LogAnswerCategoryEntity
import eu.wedgess.piholecontrol.domain.model.PiHoleLogsEntity
import eu.wedgess.piholecontrol.presentation.logs.extensions.toColor
import eu.wedgess.piholecontrol.presentation.logs.extensions.toIcon
import eu.wedgess.piholecontrol.presentation.logs.extensions.toStringValue
import eu.wedgess.piholecontrol.utils.UiText
import java.util.Locale

data class LogEntryInfo(
    val timestamp: Long,
    val client: String,
    val domain: String,
    val time: String,
    val replyTime: Double,
    val queryType: PiHoleLogsEntity.LogEntryQueryTypeEntity,
    val id: Int,
    val status: PiHoleLogsEntity.LogEntryStatusEntity,
    val dnssec: PiHoleLogsEntity.LogEntryDnssecEntity,
    val replyType: PiHoleLogsEntity.LogEntryReplyTypeEntity,
    val listId: Int?,
    val edeCode: Int,
    val edeText: String?,
    val cname: String?
) {

    val formattedReplyTime: UiText
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

    @Composable
    fun icon(): ImageVector = this.status.toIcon()

    @Composable
    fun color(): Color = this.status.toColor()

    @Composable
    fun stringValue(): String = this.status.toStringValue()

    val queryTypeString: String = this.queryType.key

    val statusString: String = this.status.category.name

    val isBlocked: Boolean = this.status.category == LogAnswerCategoryEntity.BLOCK

    val isAllowed: Boolean = this.status.category == LogAnswerCategoryEntity.ALLOW ||
            this.status.category == LogAnswerCategoryEntity.CACHE
}
