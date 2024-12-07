package eu.wedgess.piholecontrol.presentation.logs.model

import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.LogAnswerCategoryEntity
import eu.wedgess.piholecontrol.utils.UiText

enum class LogEntryStatus(val uiText: UiText, val categories: Set<LogAnswerCategoryEntity>) {
    ALL(
        uiText = UiText.StringResource(R.string.log_label_all),
        setOf(
            LogAnswerCategoryEntity.ALLOW,
            LogAnswerCategoryEntity.CACHE,
            LogAnswerCategoryEntity.BLOCK,
            LogAnswerCategoryEntity.UNKNOWN
        )
    ),
    ALLOWED(
        uiText = UiText.StringResource(R.string.log_label_allowed),
        setOf(LogAnswerCategoryEntity.ALLOW, LogAnswerCategoryEntity.CACHE)
    ),
    BLOCKED(
        uiText = UiText.StringResource(R.string.log_label_blocked),
        setOf(LogAnswerCategoryEntity.BLOCK, LogAnswerCategoryEntity.UNKNOWN)
    )
}