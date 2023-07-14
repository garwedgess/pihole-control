package eu.wedgess.mihole.ui.logs.model

import eu.wedgess.mihole.R
import eu.wedgess.mihole.data.model.enums.LogsAnswerCategory
import eu.wedgess.mihole.utils.UiText

enum class LogEntryStatus(val uiText: UiText, val categories: Set<LogsAnswerCategory>) {
    ALL(
        uiText = UiText.StringResource(R.string.log_screen_label_all),
        setOf(
            LogsAnswerCategory.ALLOW,
            LogsAnswerCategory.CACHE,
            LogsAnswerCategory.BLOCK,
            LogsAnswerCategory.UNKNOWN
        )
    ),
    ALLOWED(
        uiText = UiText.StringResource(R.string.log_screen_label_allowed),
        setOf(LogsAnswerCategory.ALLOW, LogsAnswerCategory.CACHE)
    ),
    BLOCKED(
        uiText = UiText.StringResource(R.string.log_screen_label_blocked),
        setOf(LogsAnswerCategory.BLOCK, LogsAnswerCategory.UNKNOWN)
    )
}