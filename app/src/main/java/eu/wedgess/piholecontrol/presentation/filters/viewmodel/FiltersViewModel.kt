package eu.wedgess.piholecontrol.presentation.filters.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.piholecontrol.data.model.enums.PiHoleFilterRuleType
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.usecases.filters.AddFilterRuleUseCase
import eu.wedgess.piholecontrol.domain.usecases.filters.RemoveFilterRuleUseCase
import eu.wedgess.piholecontrol.presentation.base.EventDrivenViewModel
import eu.wedgess.piholecontrol.presentation.base.SideEffectViewModel
import eu.wedgess.piholecontrol.presentation.base.SideEffectViewModelImpl
import eu.wedgess.piholecontrol.presentation.base.UiStateViewModel
import eu.wedgess.piholecontrol.presentation.base.UiStateViewModelImpl
import eu.wedgess.piholecontrol.presentation.filters.FiltersContract
import eu.wedgess.piholecontrol.presentation.filters.model.FilterDialogType
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(
    private val addFilterRuleUseCase: AddFilterRuleUseCase,
    private val removeFilterRuleUseCase: RemoveFilterRuleUseCase
) : ViewModel(),
    EventDrivenViewModel<FiltersContract.Event>,
    SideEffectViewModel<FiltersContract.Effect> by SideEffectViewModelImpl(),
    UiStateViewModel<FiltersContract.UiState> by UiStateViewModelImpl(
        FiltersContract.UiState.initial()
    ) {

    private var currentPiHoleFilterRuleType: FilterRuleTypeEntity = FilterRuleTypeEntity.ALLOW

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
                copy(dialogType = FilterDialogType.AddFilterRule(type = currentPiHoleFilterRuleType))
            }

            is FiltersContract.Event.OnFilterTabChanged -> currentPiHoleFilterRuleType = event.type
        }
    }

    private fun handleAddFilterRule(rule: String, type: FilterRuleTypeEntity) {
        viewModelScope.launch {
            addFilterRuleUseCase(rule, type)
                .onFailure {
                    Timber.e(it, "Failed to insert filter rule $rule for type: $type")
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

    private fun handleRemoveFilterRule(rule: String, type: FilterRuleTypeEntity) {
        viewModelScope.launch {
            removeFilterRuleUseCase(rule, type)
                .onFailure {
                    Timber.e(it, "Failed to remove filter rule $rule for type: $type")
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
