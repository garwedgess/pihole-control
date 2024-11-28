package eu.wedgess.piholecontrol.presentation.filters.tab.model

import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.filters.model.UiState
import eu.wedgess.piholecontrol.utils.UiText
import timber.log.Timber

data class FilterRulesResult(
    val rules: Result<List<FilterRuleEntity>>,
    val regexRules: Result<List<FilterRuleEntity>>,
) {
    fun toUiResult(query: String): UIResult<UiState> {
        return when {
            rules.isFailure && regexRules.isFailure -> {
                UIResult.Error(
                    ResultType.Error.WithTitle(
                        UiText.DynamicString("Failed to fetch filter rules")
                    )
                )
            }

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

                if (allRules.isEmpty()) {
                    UIResult.Empty(ResultType.Empty.WithTitle(UiText.DynamicString("No rules found")))
                } else {
                    UIResult.Loaded(UiState(allRules))
                }
            }
        }
    }
}
