package eu.wedgess.mihole.ui.filters.tab.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.mihole.di.FilterTabViewModelFactory
import eu.wedgess.mihole.ui.compose.ResultType
import eu.wedgess.mihole.ui.compose.UIResult
import eu.wedgess.mihole.ui.filters.model.FilterScreenTabType
import eu.wedgess.mihole.ui.filters.tab.controller.FilterTabController
import eu.wedgess.mihole.utils.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = FilterTabViewModelFactory::class)
class FilterTabViewModel @AssistedInject constructor(
    private val controller: FilterTabController,
    @Assisted val filterRuleType: FilterScreenTabType
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    val uiResult = controller.filterRulesResult(filterRuleType)
        .combine(searchQuery) { filterRules, query ->
            filterRules.getOrElse {
                return@combine UIResult.Error(
                    ResultType.Error.WithTitle(
                        UiText.DynamicString(
                            "Failed to fetch rules"
                        )
                    )
                )
            }.run {
                return@combine this@run.toUiResult(query)
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            UIResult.Loading(ResultType.Loading.WithTitle())
        )

    fun setSearchQuery(query: String?) {
        viewModelScope.launch { searchQuery.emit(query ?: "") }
    }

    fun onRefreshData() = controller.triggerRefresh()
}