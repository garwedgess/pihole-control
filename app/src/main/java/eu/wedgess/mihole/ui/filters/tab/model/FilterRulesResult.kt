package eu.wedgess.mihole.ui.filters.tab.model

import eu.wedgess.mihole.data.model.responses.PiHoleFilterRules
import eu.wedgess.mihole.ui.compose.ResultType
import eu.wedgess.mihole.ui.compose.UIResult
import eu.wedgess.mihole.ui.filters.model.UiState
import eu.wedgess.mihole.utils.UiText
import timber.log.Timber

data class FilterRulesResult(
    val rules: Result<PiHoleFilterRules>,
    val regexRules: Result<PiHoleFilterRules>,
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
                    rulesList?.rulesList?.apply { addAll(this) }
                    regexRulesList?.rulesList?.apply { addAll(this) }
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
