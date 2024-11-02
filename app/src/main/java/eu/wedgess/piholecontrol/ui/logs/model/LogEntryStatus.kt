package eu.wedgess.piholecontrol.ui.logs.model

import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.data.model.enums.LogsAnswerCategory
import eu.wedgess.piholecontrol.utils.UiText

enum class LogEntryStatus(val uiText: UiText, val categories: Set<LogsAnswerCategory>) {
    ALL(
        uiText = UiText.StringResource(R.string.log_label_all),
        setOf(
            LogsAnswerCategory.ALLOW,
            LogsAnswerCategory.CACHE,
            LogsAnswerCategory.BLOCK,
            LogsAnswerCategory.UNKNOWN
        )
    ),
    ALLOWED(
        uiText = UiText.StringResource(R.string.log_label_allowed),
        setOf(LogsAnswerCategory.ALLOW, LogsAnswerCategory.CACHE)
    ),
    BLOCKED(
        uiText = UiText.StringResource(R.string.log_label_blocked),
        setOf(LogsAnswerCategory.BLOCK, LogsAnswerCategory.UNKNOWN)
    )
}