package eu.wedgess.mihole.ui.filters.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.mihole.data.model.enums.FilterRuleType
import eu.wedgess.mihole.ui.filters.FiltersContract
import eu.wedgess.mihole.ui.filters.controller.FiltersController
import eu.wedgess.mihole.ui.filters.model.FilterDialogType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(
    private val controller: FiltersController
) : ViewModel(), FiltersContract {

    private val _uiState: MutableStateFlow<FiltersContract.UiState> =
        MutableStateFlow(FiltersContract.UiState.initial())
    override val uiState: StateFlow<FiltersContract.UiState> = _uiState.asStateFlow()

    private val _effect: Channel<FiltersContract.Effect> = Channel(Channel.UNLIMITED)
    override val effect: Flow<FiltersContract.Effect> = _effect.receiveAsFlow()

    private var currentFilterRuleType: FilterRuleType = FilterRuleType.ALLOW

    override fun onEvent(event: FiltersContract.Event) {
        when (event) {
            is FiltersContract.Event.OnClearSearchQuery -> handleClearSearchQuery(event.query)

            FiltersContract.Event.OnSearchClick -> _uiState.update {
                it.copy(showSearchView = false)
            }

            is FiltersContract.Event.OnSearchExpandedChanged -> _uiState.update {
                it.copy(showSearchView = event.expanded)
            }

            is FiltersContract.Event.OnSearchQueryChanged -> _uiState.update {
                it.copy(searchQuery = event.query)
            }

            FiltersContract.Event.OnShowSearchView -> _uiState.update {
                it.copy(showSearchView = true)
            }

            is FiltersContract.Event.OnAddFilterRule -> handleAddFilterRule(
                rule = event.rule.domain,
                type = event.rule.type
            )

            is FiltersContract.Event.OnDeleteFilterRule -> handleRemoveFilterRule(
                rule = event.rule.domain,
                type = event.rule.type
            )

            FiltersContract.Event.OnDismissDialog -> _uiState.update {
                it.copy(dialogType = FilterDialogType.None)
            }

            is FiltersContract.Event.OnFilterRuleItemClick -> _uiState.update {
                it.copy(dialogType = FilterDialogType.ShowFilterRuleInfo(event.item))
            }

            FiltersContract.Event.AddFilterRuleClick -> _uiState.update {
                it.copy(dialogType = FilterDialogType.AddFilterRule(type = currentFilterRuleType))
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
                    _effect.send(FiltersContract.Effect.Toast.RuleAddFailed).also {
                        _uiState.update { state -> state.copy(dialogType = FilterDialogType.None) }
                    }
                }
                .onSuccess {
                    _effect.send(FiltersContract.Effect.Toast.RuleAdded).also {
                        _uiState.update { state -> state.copy(dialogType = FilterDialogType.None) }
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
                    _effect.send(FiltersContract.Effect.Toast.RuleRemovalFailed).also {
                        _uiState.update { state -> state.copy(dialogType = FilterDialogType.None) }
                    }
                }
                .onSuccess {
                    _effect.send(FiltersContract.Effect.Toast.RuleRemoved).also {
                        _uiState.update { state -> state.copy(dialogType = FilterDialogType.None) }
                    }
                }
        }
    }

    private fun handleClearSearchQuery(query: String) {
        if (query.isEmpty()) {
            _uiState.update { it.copy(showSearchView = false) }
        } else {
            _uiState.update { it.copy(searchQuery = "") }
        }
    }
}
