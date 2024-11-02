package eu.wedgess.mihole.ui.filters.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.mihole.data.model.enums.FilterRuleType
import eu.wedgess.mihole.ui.base.EventDrivenViewModel
import eu.wedgess.mihole.ui.base.SideEffectViewModel
import eu.wedgess.mihole.ui.base.SideEffectViewModelImpl
import eu.wedgess.mihole.ui.base.UiStateViewModel
import eu.wedgess.mihole.ui.base.UiStateViewModelImpl
import eu.wedgess.mihole.ui.filters.FiltersContract
import eu.wedgess.mihole.ui.filters.controller.FiltersController
import eu.wedgess.mihole.ui.filters.model.FilterDialogType
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(
    private val controller: FiltersController
) : ViewModel(),
    EventDrivenViewModel<FiltersContract.Event>,
    SideEffectViewModel<FiltersContract.Effect> by SideEffectViewModelImpl(),
    UiStateViewModel<FiltersContract.UiState> by UiStateViewModelImpl(
        FiltersContract.UiState.initial()
    ) {

    private var currentFilterRuleType: FilterRuleType = FilterRuleType.ALLOW

    override fun onEvent(event: FiltersContract.Event) {
        when (event) {
            is FiltersContract.Event.OnClearSearchQuery -> handleClearSearchQuery(event.query)

            FiltersContract.Event.OnSearchClick -> updateUiState { copy(showSearchView = false) }

            is FiltersContract.Event.OnSearchExpandedChanged -> updateUiState {
                copy(showSearchView = event.expanded)
            }

            is FiltersContract.Event.OnSearchQueryChanged -> updateUiState {
                copy(searchQuery = event.query)
            }

            FiltersContract.Event.OnShowSearchView -> updateUiState { copy(showSearchView = true) }

            is FiltersContract.Event.OnAddFilterRule -> handleAddFilterRule(
                rule = event.rule.domain,
                type = event.rule.type
            )

            is FiltersContract.Event.OnDeleteFilterRule -> handleRemoveFilterRule(
                rule = event.rule.domain,
                type = event.rule.type
            )

            FiltersContract.Event.OnDismissDialog -> updateUiState {
                copy(dialogType = FilterDialogType.None)
            }

            is FiltersContract.Event.OnFilterRuleItemClick -> updateUiState {
                copy(dialogType = FilterDialogType.ShowFilterRuleInfo(event.item))
            }

            FiltersContract.Event.AddFilterRuleClick -> updateUiState {
                copy(dialogType = FilterDialogType.AddFilterRule(type = currentFilterRuleType))
            }

            is FiltersContract.Event.OnFilterTabChanged -> currentFilterRuleType = event.type
        }
    }

    private fun handleAddFilterRule(rule: String, type: FilterRuleType) {
        viewModelScope.launch {
            controller.addFilterRule(rule, type)
                .onFailure {
                    Timber.e(
                        "Failed to insert filter rule $rule for type: $type",
                        it
                    )
                    emitSideEffect(FiltersContract.Effect.Toast.RuleAddFailed).also {
                        updateUiState { copy(dialogType = FilterDialogType.None) }
                    }
                }
                .onSuccess {
                    emitSideEffect(FiltersContract.Effect.Toast.RuleAdded).also {
                        updateUiState { copy(dialogType = FilterDialogType.None) }
                    }
                }
        }
    }

    private fun handleRemoveFilterRule(rule: String, type: FilterRuleType) {
        viewModelScope.launch {
            controller.removeFilterRule(rule, type)
                .onFailure {
                    Timber.e(
                        "Failed to remove filter rule $rule for type: $type",
                        it
                    )
                    emitSideEffect(FiltersContract.Effect.Toast.RuleRemovalFailed).also {
                        updateUiState { copy(dialogType = FilterDialogType.None) }
                    }
                }
                .onSuccess {
                    emitSideEffect(FiltersContract.Effect.Toast.RuleRemoved).also {
                        updateUiState { copy(dialogType = FilterDialogType.None) }
                    }
                }
        }
    }

    private fun handleClearSearchQuery(query: String) {
        if (query.isEmpty()) {
            updateUiState { copy(showSearchView = false) }
        } else {
            updateUiState { copy(searchQuery = "") }
        }
    }
}
