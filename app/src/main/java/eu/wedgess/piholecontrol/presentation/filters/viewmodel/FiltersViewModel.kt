package eu.wedgess.piholecontrol.presentation.filters.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.model.GroupEntity
import eu.wedgess.piholecontrol.domain.usecases.filters.AddFilterRuleUseCase
import eu.wedgess.piholecontrol.domain.usecases.filters.RemoveFilterRuleUseCase
import eu.wedgess.piholecontrol.domain.usecases.filters.UpdateFilterRuleUseCase
import eu.wedgess.piholecontrol.domain.usecases.groups.FetchAllGroupsUseCase
import eu.wedgess.piholecontrol.presentation.base.EventDrivenViewModel
import eu.wedgess.piholecontrol.presentation.base.SideEffectViewModel
import eu.wedgess.piholecontrol.presentation.base.SideEffectViewModelImpl
import eu.wedgess.piholecontrol.presentation.base.UiStateViewModel
import eu.wedgess.piholecontrol.presentation.base.UiStateViewModelImpl
import eu.wedgess.piholecontrol.presentation.common.model.SelectionMode
import eu.wedgess.piholecontrol.presentation.common.model.selectedItems
import eu.wedgess.piholecontrol.presentation.filters.FiltersContract
import eu.wedgess.piholecontrol.presentation.filters.extensions.toBaseFilterRuleType
import eu.wedgess.piholecontrol.presentation.filters.extensions.toFilterRuleUpdateEntity
import eu.wedgess.piholecontrol.presentation.filters.extensions.withRegexFilterRuleType
import eu.wedgess.piholecontrol.presentation.filters.model.FilterByOption
import eu.wedgess.piholecontrol.presentation.filters.model.FilterDialogType
import eu.wedgess.piholecontrol.presentation.filters.model.FilterRuleDraft
import eu.wedgess.piholecontrol.presentation.filters.model.FilterRuleIdentity
import eu.wedgess.piholecontrol.presentation.filters.model.ModifyFilterRule
import eu.wedgess.piholecontrol.presentation.filters.tab.model.FilterRuleInfo
import eu.wedgess.piholecontrol.presentation.navigation.tabs.FilterTab
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(
    private val addFilterRuleUseCase: AddFilterRuleUseCase,
    private val removeFilterRuleUseCase: RemoveFilterRuleUseCase,
    private val updateFilterRuleUseCase: UpdateFilterRuleUseCase,
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

            FiltersContract.Event.OnAddFilterRuleConfirmed -> handleAddFilterRuleConfirmed()

            is FiltersContract.Event.OnDeleteFilterRuleConfirmed -> handleRemoveFilterRule(
                rule = event.rule.domain,
                type = event.rule.type
            )

            FiltersContract.Event.OnDeleteSelectedFilterRulesClick -> updateUiState {
                val selectedCount = selectionMode.selectedItems().size
                if (selectedCount == 0) {
                    copy(selectionMode = SelectionMode.Inactive)
                } else {
                    copy(
                        dialogType = FilterDialogType.OnConfirmSelectedFiltersDelete(
                            selectedCount
                        )
                    )
                }
            }

            FiltersContract.Event.OnDeleteSelectedFilterRulesConfirmed -> handleRemoveSelectedRules()

            FiltersContract.Event.OnClearSelection -> updateUiState {
                copy(selectionMode = SelectionMode.Inactive)
            }

            FiltersContract.Event.OnUpdateFilterRuleConfirmed -> handleUpdateFilterRuleConfirmed()

            FiltersContract.Event.OnDismissDialog -> updateUiState {
                copy(dialogType = FilterDialogType.None)
            }

            is FiltersContract.Event.OnFilterRuleItemClick -> updateUiState {
                if (selectionMode is SelectionMode.Active) {
                    copy(selectionMode = selectionMode.toggle(event.item.identity()))
                } else {
                    copy(dialogType = FilterDialogType.ShowFilterRuleInfo(event.item))
                }
            }

            is FiltersContract.Event.OnFilterRuleItemLongClick -> updateUiState {
                copy(selectionMode = selectionMode.toggle(event.item.identity()))
            }

            FiltersContract.Event.AddFilterRuleClick -> fetchGroupsAndShowDialog()

            is FiltersContract.Event.OnFilterTabChanged -> handleTabChange(event.type)

            is FiltersContract.Event.OnDeleteFilterRuleClick -> updateUiState {
                copy(dialogType = FilterDialogType.OnConfirmFilterDelete(filterRule = event.rule))
            }

            is FiltersContract.Event.OnEditFilterRuleClick -> fetchGroupsAndShowEditDialog(event.rule)

            FiltersContract.Event.OnDismissFilterBy -> updateUiState {
                copy(showFilterByMenu = false)
            }

            FiltersContract.Event.OnShowFilterByMenu -> updateUiState {
                copy(showFilterByMenu = true)
            }

            is FiltersContract.Event.OnFilterByOptionClick -> handleFilterOptionClick(event.option)

            is FiltersContract.Event.OnFilterRuleCommentChanged ->
                updateFilterRuleDraft(comment = event.comment)

            is FiltersContract.Event.OnFilterRuleDomainChanged ->
                updateFilterRuleDraft(domain = event.domain)

            is FiltersContract.Event.OnFilterRuleEnabledChanged ->
                updateFilterRuleDraft(enabled = event.enabled)

            is FiltersContract.Event.OnFilterRuleGroupsChanged ->
                updateFilterRuleDraft(groups = event.groups)

            is FiltersContract.Event.OnFilterRuleRegexChanged ->
                updateFilterRuleDraft(isRegex = event.isRegex)
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
                        groups = groups,
                        draft = FilterRuleDraft(
                            selectedGroups = setOfNotNull(groups.firstOrNull())
                        )
                    )
                )
            }
        }
    }

    private fun fetchGroupsAndShowEditDialog(filterRule: FilterRuleInfo) {
        viewModelScope.launch {
            val groups = fetchAllGroupsUseCase().getOrElse {
                Timber.e(it, "Failed to fetch groups")
                emptyList()
            }
            updateUiState {
                copy(
                    dialogType = FilterDialogType.EditFilterRule(
                        original = FilterRuleIdentity(
                            domain = filterRule.domain,
                            type = filterRule.type
                        ),
                        draft = filterRule,
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

    private fun handleRemoveSelectedRules() {
        val selectedRules = uiState.value.selectionMode.selectedItems()
        if (selectedRules.isEmpty()) {
            updateUiState {
                copy(
                    selectionMode = SelectionMode.Inactive,
                    dialogType = FilterDialogType.None
                )
            }
            return
        }

        viewModelScope.launch {
            val failedRules = selectedRules.filter { rule ->
                removeFilterRuleUseCase(rule.domain, rule.type).isFailure
            }
            if (failedRules.isEmpty()) {
                emitSideEffect(FiltersContract.Effect.Toast.SelectedRulesRemoved).also {
                    updateUiState {
                        copy(
                            selectionMode = SelectionMode.Inactive,
                            dialogType = FilterDialogType.None
                        )
                    }
                }
            } else {
                Timber.e("Failed to remove ${failedRules.size} selected filter rules")
                emitSideEffect(FiltersContract.Effect.Toast.RuleRemovalFailed).also {
                    updateUiState { copy(dialogType = FilterDialogType.None) }
                }
            }
        }
    }

    private fun handleAddFilterRuleConfirmed() {
        val dialog = uiState.value.dialogType as? FilterDialogType.AddFilterRule ?: return
        val ruleType = dialog.type
            .toBaseFilterRuleType()
            .withRegexFilterRuleType(dialog.draft.isRegex)
        handleAddFilterRule(
            rule = dialog.draft.domain,
            groups = dialog.draft.selectedGroups.map { it.id },
            comment = dialog.draft.comment,
            type = ruleType
        )
    }

    private fun handleUpdateFilterRule(rule: ModifyFilterRule.Update) {
        viewModelScope.launch {
            updateFilterRuleUseCase(
                update = rule.toFilterRuleUpdateEntity()
            )
                .onFailure {
                    Timber.e(it, "Failed to update filter rule ${rule.originalDomain} for type: ${rule.originalType}")
                    emitSideEffect(FiltersContract.Effect.Toast.RuleUpdateFailed).also {
                        updateUiState { copy(dialogType = FilterDialogType.None) }
                    }
                }
                .onSuccess {
                    emitSideEffect(FiltersContract.Effect.Toast.RuleUpdated).also {
                        updateUiState { copy(dialogType = FilterDialogType.None) }
                    }
                }
        }
    }

    private fun handleUpdateFilterRuleConfirmed() {
        val dialog = uiState.value.dialogType as? FilterDialogType.EditFilterRule ?: return
        handleUpdateFilterRule(
            ModifyFilterRule.Update(
                originalDomain = dialog.original.domain,
                domain = dialog.draft.domain,
                groups = dialog.draft.groups,
                comment = dialog.draft.comment,
                enabled = dialog.draft.enabled,
                originalType = dialog.original.type,
                type = dialog.draft.type
            )
        )
    }

    private fun handleClearSearchQuery(query: String) {
        if (query.isEmpty()) {
            updateUiState { copy(showSearchView = false) }
        } else {
            updateUiState { copy(searchQuery = "") }
        }
    }

    private fun updateFilterRuleDraft(
        domain: String? = null,
        groups: Set<GroupEntity>? = null,
        comment: String? = null,
        enabled: Boolean? = null,
        isRegex: Boolean? = null
    ) {
        updateUiState {
            copy(
                dialogType = when (val dialog = dialogType) {
                    is FilterDialogType.AddFilterRule -> dialog.copy(
                        draft = dialog.draft.copy(
                            domain = domain ?: dialog.draft.domain,
                            selectedGroups = groups ?: dialog.draft.selectedGroups,
                            comment = comment ?: dialog.draft.comment,
                            isRegex = isRegex ?: dialog.draft.isRegex
                        )
                    )

                    is FilterDialogType.EditFilterRule -> dialog.copy(
                        draft = dialog.draft.copy(
                            domain = domain ?: dialog.draft.domain,
                            groups = groups?.map { it.id } ?: dialog.draft.groups,
                            comment = comment ?: dialog.draft.comment,
                            enabled = enabled ?: dialog.draft.enabled,
                            type = isRegex?.let {
                                dialog.draft.type
                                    .toBaseFilterRuleType()
                                    .withRegexFilterRuleType(it)
                            } ?: dialog.draft.type
                        )
                    )

                    else -> dialog
                }
            )
        }
    }

    private fun FilterRuleInfo.identity() = FilterRuleIdentity(
        domain = domain,
        type = type
    )

    private fun SelectionMode<FilterRuleIdentity>.toggle(
        item: FilterRuleIdentity
    ): SelectionMode<FilterRuleIdentity> {
        val selected = when (this) {
            is SelectionMode.Active -> this.selected.toMutableSet()
            SelectionMode.Inactive -> mutableSetOf()
        }
        if (!selected.add(item)) {
            selected.remove(item)
        }
        return if (selected.isEmpty()) SelectionMode.Inactive else SelectionMode.Active(selected)
    }
}
