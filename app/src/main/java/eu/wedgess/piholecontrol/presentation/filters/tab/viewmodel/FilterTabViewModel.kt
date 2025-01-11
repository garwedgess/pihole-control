package eu.wedgess.piholecontrol.presentation.filters.tab.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.di.FilterTabViewModelFactory
import eu.wedgess.piholecontrol.domain.usecases.filters.FetchFilterRulesUseCase
import eu.wedgess.piholecontrol.presentation.base.EventDrivenViewModel
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.filters.extensions.toInfo
import eu.wedgess.piholecontrol.presentation.filters.model.FilterScreenTabType
import eu.wedgess.piholecontrol.presentation.filters.tab.FilterTabContract
import eu.wedgess.piholecontrol.utils.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = FilterTabViewModelFactory::class)
class FilterTabViewModel @AssistedInject constructor(
    private val filterRulesUseCase: FetchFilterRulesUseCase,
    @Assisted val filterRuleType: FilterScreenTabType
) : ViewModel(),
    EventDrivenViewModel<FilterTabContract.Event> {

    private val searchQuery = MutableStateFlow("")

    val uiResult = filterRulesUseCase(filterRuleType.toFilterTypePair().first)
        .combine(searchQuery) { filterRules, query ->
            filterRules.fold(
                onFailure = {
                    handleErrorThrowable(
                        throwable = it,
                        onRetry = ::onRefreshData
                    )
                },
                onSuccess = { rules ->
                    val filteredRules =
                        rules
                            .filter { it.domain.contains(query.lowercase(), ignoreCase = true) }
                            .map { it.toInfo() }

                    if (filteredRules.isEmpty()) {
                        UIResult.Empty(
                            ResultType.Empty.WithTitle(UiText.DynamicString("No rules found"))
                        )
                    } else {
                        UIResult.Loaded(FilterTabContract.UiState(filteredRules))
                    }
                }
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            UIResult.Loading(ResultType.Loading.WithTitle())
        )

    override fun onEvent(event: FilterTabContract.Event) {
        when (event) {
            FilterTabContract.Event.OnRefresh -> onRefreshData()
            is FilterTabContract.Event.OnSearchQueryChanged -> setSearchQuery(event.query)
        }
    }

    private fun setSearchQuery(query: String?) {
        viewModelScope.launch { searchQuery.emit(query ?: "") }
    }

    private fun onRefreshData() = filterRulesUseCase.refreshRules()

    private fun handleErrorThrowable(throwable: Throwable?, onRetry: () -> Unit): UIResult.Error {
        return UIResult.Error(
            ResultType.Error.WithTitleAndSubTitleAndRetry(
                title = UiText.StringResource(R.string.filter_rules_fetch_error),
                subTitle = UiText.DynamicString(throwable?.message ?: "Unknown error"),
                onRetry = onRetry
            )
        )
    }
}
