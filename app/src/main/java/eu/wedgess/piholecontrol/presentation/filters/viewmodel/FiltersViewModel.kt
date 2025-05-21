package eu.wedgess.piholecontrol.presentation.filters.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.usecases.filters.AddFilterRuleUseCase
import eu.wedgess.piholecontrol.domain.usecases.filters.RemoveFilterRuleUseCase
import eu.wedgess.piholecontrol.domain.usecases.groups.FetchAllGroupsUseCase
import eu.wedgess.piholecontrol.presentation.base.EventDrivenViewModel
import eu.wedgess.piholecontrol.presentation.base.SideEffectViewModel
import eu.wedgess.piholecontrol.presentation.base.SideEffectViewModelImpl
import eu.wedgess.piholecontrol.presentation.base.UiStateViewModel
import eu.wedgess.piholecontrol.presentation.base.UiStateViewModelImpl
import eu.wedgess.piholecontrol.presentation.filters.FiltersContract
import eu.wedgess.piholecontrol.presentation.filters.model.FilterByOption
import eu.wedgess.piholecontrol.presentation.filters.model.FilterDialogType
import eu.wedgess.piholecontrol.presentation.navigation.tabs.FilterTab
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(
    private val addFilterRuleUseCase: AddFilterRuleUseCase,
    private val removeFilterRuleUseCase: RemoveFilterRuleUseCase,
    private val fetchAllGroupsUseCase: FetchAllGroupsUseCase
) : ViewModel(),
    EventDrivenViewModel<FiltersContract.Event>,
    SideEffectViewModel<FiltersContract.Effect> by SideEffectViewModelImpl(),
    UiStateViewModel<FiltersContract.UiState> by UiStateViewModelImpl(
        FiltersContract.UiState.initial()
    ) {

    private var currentTabType: FilterTab = FilterTab.AllowList

    override fun onEvent(event: FiltersContract.Event) {
        when (event) {
            is FiltersContract.Event.OnClearSearchQuery -> handleClearSearchQuery(event.query)

            is FiltersContract.Event.OnSearchExpandedChanged -> updateUiState {
                copy(showSearchView = event.expanded)
            }

            is FiltersContract.Event.OnSearchQueryChanged -> updateUiState {
                copy(searchQuery = event.query)
            }

            FiltersContract.Event.OnShowSearchView -> updateUiState { copy(showSearchView = true) }

            is FiltersContract.Event.OnAddFilterRule -> handleAddFilterRule(
                rule = event.rule.domain,
                groups = event.rule.groups,
                comment = event.rule.comment,
                type = event.rule.type
            )

            is FiltersContract.Event.OnDeleteFilterRuleConfirmed -> handleRemoveFilterRule(
                rule = event.rule.domain,
                type = event.rule.type
            )

            FiltersContract.Event.OnDismissDialog -> updateUiState {
                copy(dialogType = FilterDialogType.None)
            }

            is FiltersContract.Event.OnFilterRuleItemClick -> updateUiState {
                copy(dialogType = FilterDialogType.ShowFilterRuleInfo(event.item))
            }

            FiltersContract.Event.AddFilterRuleClick -> fetchGroupsAndShowDialog()

            is FiltersContract.Event.OnFilterTabChanged -> handleTabChange(event.type)

            is FiltersContract.Event.OnDeleteFilterRuleClick -> updateUiState {
                copy(dialogType = FilterDialogType.OnConfirmFilterDelete(filterRule = event.rule))
            }

            FiltersContract.Event.OnDismissFilterBy -> updateUiState {
                copy(showFilterByMenu = false)
            }

            FiltersContract.Event.OnShowFilterByMenu -> updateUiState {
                copy(showFilterByMenu = true)
            }

            is FiltersContract.Event.OnFilterByOptionClick -> handleFilterOptionClick(event.option)
        }
    }

    private fun fetchGroupsAndShowDialog() {
        viewModelScope.launch {
            val groups = fetchAllGroupsUseCase().getOrElse {
                Timber.e(it, "Failed to fetch groups")
                emptyList()
            }
            updateUiState {
                copy(
                    dialogType = FilterDialogType.AddFilterRule(
                        type = currentTabType.toFilterRuleType(),
                        groups = groups
                    )
                )
            }
        }
    }

    private fun handleFilterOptionClick(option: FilterByOption) {
        updateUiState {
            val newOptions = toggleFilterOption(
                previouslySelectedOptions = when (currentTabType) {
                    is FilterTab.AllowList -> allowSelectedFilterBy
                    is FilterTab.DenyList -> denySelectedFilterBy
                },
                option = option,
                availableOptions = filterByOptions
            )
            when (currentTabType) {
                is FilterTab.AllowList -> copy(
                    allowSelectedFilterBy = newOptions,
                    showFilterByMenu = false
                )

                is FilterTab.DenyList -> copy(
                    denySelectedFilterBy = newOptions,
                    showFilterByMenu = false
                )
            }
        }
    }

    private fun handleTabChange(tabType: FilterTab) {
        this.currentTabType = tabType
        val newOptions = FilterByOption.getByTab(tabType)

        updateUiState {
            copy(filterByOptions = newOptions, selectedTabType = tabType)
        }
    }

    private fun toggleFilterOption(
        previouslySelectedOptions: List<FilterByOption>,
        option: FilterByOption,
        availableOptions: List<FilterByOption>
    ): List<FilterByOption> {
        val newOptions = previouslySelectedOptions.toMutableList()
        val otherOption = availableOptions.firstOrNull { it != option }

        if (newOptions.contains(option)) {
            newOptions.remove(option)
        } else {
            newOptions.add(option)
        }

        return if (newOptions.isEmpty() && otherOption != null) {
            newOptions.add(otherOption)
            newOptions.toList()
        } else {
            newOptions.toList()
        }
    }

    private fun handleAddFilterRule(
        rule: String,
        groups: List<Int>,
        comment: String?,
        type: FilterRuleTypeEntity
    ) {
        viewModelScope.launch {
            addFilterRuleUseCase(rule, groups = groups, comment, type)
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
