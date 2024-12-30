package eu.wedgess.piholecontrol.presentation.filters.tab.model

import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.filters.extensions.toInfo
import eu.wedgess.piholecontrol.presentation.filters.tab.FilterTabContract
import eu.wedgess.piholecontrol.utils.UiText
import timber.log.Timber

data class FilterRulesResult(
    val rules: Result<List<FilterRuleEntity>>,
    val regexRules: Result<List<FilterRuleEntity>>,
) {
    fun toUiResult(query: String, onRetry: () -> Unit): UIResult<FilterTabContract.UiState> {
        return when {
            rules.isFailure && regexRules.isFailure -> handleErrorThrowable(
                throwable = rules.exceptionOrNull(),
                onRetry = onRetry
            )

            else -> {
                val rulesList = rules.onFailure {
                    Timber.e("Failed to rules: ${it.message}", it)
                }.getOrNull()
                val regexRulesList = regexRules.onFailure {
                    Timber.e("Failed to fetch regex rules: ${it.message}", it)
                }.getOrNull()

                val allRules = buildList {
                    rulesList?.run { addAll(this) }
                    regexRulesList?.run { addAll(this) }
                }.filter { it.domain.contains(query.lowercase(), ignoreCase = true) }
                    .map { it.toInfo() }

                if (allRules.isEmpty()) {
                    UIResult.Empty(
                        ResultType.Empty.WithTitle(UiText.DynamicString("No rules found"))
                    )
                } else {
                    UIResult.Loaded(FilterTabContract.UiState(allRules))
                }
            }
        }
    }

    companion object {
        fun handleErrorThrowable(throwable: Throwable?, onRetry: () -> Unit): UIResult.Error {
            return UIResult.Error(
                ResultType.Error.WithTitleAndSubTitleAndRetry(
                    title = UiText.StringResource(R.string.filter_rules_fetch_error),
                    subTitle = UiText.DynamicString(throwable?.message ?: "Unknown error"),
                    onRetry = onRetry
                )
            )
        }
    }
}
