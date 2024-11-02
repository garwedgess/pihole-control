package eu.wedgess.piholecontrol.ui.filters.tab.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.piholecontrol.di.FilterTabViewModelFactory
import eu.wedgess.piholecontrol.ui.compose.ResultType
import eu.wedgess.piholecontrol.ui.compose.UIResult
import eu.wedgess.piholecontrol.ui.filters.model.FilterScreenTabType
import eu.wedgess.piholecontrol.ui.filters.tab.controller.FilterTabController
import eu.wedgess.piholecontrol.utils.UiText
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
                    ResultType.Error.WithTitleAndSubTitle(
                        UiText.DynamicString("Failed to fetch filter rules"),
                        UiText.DynamicString(it.message ?: "Unknown error")
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