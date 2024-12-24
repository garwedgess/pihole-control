package eu.wedgess.piholecontrol.presentation.filters.tab.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.di.FilterTabViewModelFactory
import eu.wedgess.piholecontrol.domain.model.FilterRulesResultEntity
import eu.wedgess.piholecontrol.domain.usecases.filters.FetchFilterRulesUseCase
import eu.wedgess.piholecontrol.presentation.base.EventDrivenViewModel
import eu.wedgess.piholecontrol.presentation.base.SideEffectViewModel
import eu.wedgess.piholecontrol.presentation.base.SideEffectViewModelImpl
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.filters.extensions.toFilterRulesResult
import eu.wedgess.piholecontrol.presentation.filters.model.FilterScreenTabType
import eu.wedgess.piholecontrol.presentation.filters.tab.FilterTabContract
import eu.wedgess.piholecontrol.presentation.filters.tab.model.FilterRulesResult
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
    SideEffectViewModel<FilterTabContract.Effect> by SideEffectViewModelImpl(),
    EventDrivenViewModel<FilterTabContract.Event> {

    private val searchQuery = MutableStateFlow("")
    private var showErrorMessage: Boolean = true

    val uiResult = filterRulesUseCase(filterRuleType.toFilterTypePair().first)
        .combine(searchQuery) { filterRules, query ->
            filterRules.getOrElse {
                return@combine FilterRulesResult.handleErrorThrowable(it)
            }.run {
                return@combine this@run.toFilterRulesResult().toUiResult(query).also {
                    if (it !is UIResult.Error && showErrorMessage) {
                        handleCombinedErrors(this)
                    }
                }
            }
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

    private fun handleCombinedErrors(rulesEntity: FilterRulesResultEntity) {
        val failures = mutableListOf<UiText>()
        rulesEntity.rules.onFailure {
            failures.add(
                UiText.StringResourceWithArgs(
                    R.string.filter_rules_error,
                    it.message ?: ""
                )
            )
        }
        rulesEntity.regexRules.onFailure {
            failures.add(
                UiText.StringResourceWithArgs(
                    R.string.filter_regex_rules_error,
                    it.message ?: ""
                )
            )
        }
        if (failures.isNotEmpty()) {
            showErrorMessage = false
            viewModelScope.emitSideEffect(
                FilterTabContract.Effect.ShowErrorSnackbar(failures)
            )
        }
    }
}
