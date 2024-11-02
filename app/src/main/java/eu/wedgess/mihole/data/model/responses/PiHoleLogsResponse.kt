package eu.wedgess.mihole.data.model.responses

import eu.wedgess.mihole.ui.logs.model.LogEntryStatus
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleLogsResponse(
    val data: List<PiHoleLog> = listOf()
) {
    fun applyStatusFilter(status: LogEntryStatus) = when (status) {
        LogEntryStatus.ALL -> this
        LogEntryStatus.ALLOWED,
        LogEntryStatus.BLOCKED ->
            this.copy(data = data.filter { status.categories.contains(it.answerType.category) })
    }
}