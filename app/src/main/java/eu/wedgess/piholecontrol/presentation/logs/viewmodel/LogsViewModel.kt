package eu.wedgess.piholecontrol.presentation.logs.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.model.PiHoleLogsEntity
import eu.wedgess.piholecontrol.domain.model.RefreshMode
import eu.wedgess.piholecontrol.domain.usecases.filters.AddFilterRuleUseCase
import eu.wedgess.piholecontrol.domain.usecases.logs.FetchLogFilterSuggestionsUseCase
import eu.wedgess.piholecontrol.domain.usecases.logs.FetchLogsUseCase
import eu.wedgess.piholecontrol.presentation.base.EventDrivenViewModel
import eu.wedgess.piholecontrol.presentation.base.SideEffectViewModel
import eu.wedgess.piholecontrol.presentation.base.SideEffectViewModelImpl
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.logs.LogsContract
import eu.wedgess.piholecontrol.presentation.logs.extensions.toInfo
import eu.wedgess.piholecontrol.presentation.logs.model.LogEntryStatus
import eu.wedgess.piholecontrol.presentation.logs.model.LogSorting
import eu.wedgess.piholecontrol.presentation.logs.model.LogsDialogType
import eu.wedgess.piholecontrol.presentation.logs.model.PickerType
import eu.wedgess.piholecontrol.utils.UiText
import eu.wedgess.piholecontrol.utils.extensions.toEpochSeconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LogsViewModel @Inject constructor(
    private val fetchLogsUseCase: FetchLogsUseCase,
    private val fetchLogsFilterSuggestionsUseCase: FetchLogFilterSuggestionsUseCase,
    private val addFilterRuleUseCase: AddFilterRuleUseCase
) : ViewModel(),
    EventDrivenViewModel<LogsContract.Event>,
    SideEffectViewModel<LogsContract.Effect> by SideEffectViewModelImpl() {

    private val _uiState = MutableStateFlow(LogsContract.UiState.initial())
    private val _bottomSheetUiState = MutableStateFlow(LogsContract.BottomSheetUiState.initial())
    val bottomSheetUiState = _bottomSheetUiState.asStateFlow()
    private val _searchUiState = MutableStateFlow(LogsContract.SearchUiState.initial())
    val searchUiState = _searchUiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiResult =
        _bottomSheetUiState
            .combine(searchUiState) { sheetState, searchState ->
                sheetState to searchState
            }
            .onStart { fetchLogFilterSuggestions() }
            .map { (sheetState, searchState) ->
                fetchLogsUseCase(
                    limit = sheetState.logsLimit,
                    status = sheetState.selectedLogEntryStatus,
                    query = searchState.searchQuery,
                    clientIp = sheetState.selectedClientIp,
                    clientName = sheetState.selectedClientName,
                    queryType = sheetState.selectedQueryType,
                    advancedStatus = sheetState.selectedAdvancedStatus,
                    from = sheetState.filterFromTime,
                    until = sheetState.filterToTime
                )
            }
            .flatMapLatest { logsFlow ->
                logsFlow.combine(_uiState) { result, uiState ->
                    result.fold(
                        onSuccess = { logs ->
                            if (logs.isEmpty()) {
                                UIResult.Empty(
                                    ResultType.Empty.WithTitle(
                                        UiText.StringResource(R.string.logs_msg_empty)
                                    )
                                )
                            } else {
                                UIResult.Loaded(
                                    uiState.copy(
                                        logs = logs.sortBy(uiState.sorting).map { it.toInfo() }
                                    )
                                )
                            }
                        },
                        onFailure = { error ->
                            UIResult.Error(
                                ResultType.Error.WithTitleAndSubTitleAndRetry(
                                    title = UiText.DynamicString("Failed to fetch logs"),
                                    subTitle = UiText.DynamicString(
                                        error.message ?: "Unknown error"
                                    ),
                                    onRetry = fetchLogsUseCase::refresh
                                )
                            )
                        }
                    )
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                UIResult.Loading(ResultType.Loading.WithTitle())
            )

    private fun fetchLogFilterSuggestions() {
        fetchLogsFilterSuggestionsUseCase()
            .onEach { suggestionsResult ->
                suggestionsResult.onSuccess { suggestions ->
                    _bottomSheetUiState.update { current ->
                        current.copy(
                            availableClientNames = suggestions.clientNames,
                            availableClientIps = suggestions.clientIpAddresses,
                            availableQueryTypes = suggestions.queryTypes,
                            availableStatuses = suggestions.statuses
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun List<PiHoleLogsEntity>.sortBy(sortType: LogSorting) = when (sortType) {
        LogSorting.DATE_DESC -> sortedByDescending { it.timestamp }
        LogSorting.DATE_ASC -> sortedBy { it.timestamp }
        LogSorting.RESPONSE_TIME_ASC -> sortedBy { it.replyTime }
        LogSorting.RESPONSE_TIME_DESC -> sortedByDescending { it.replyTime }
    }

    override fun onEvent(event: LogsContract.Event) {
        when (event) {
            is LogsContract.Event.AddToAllowList -> onAddToAllowList(event.domain)
            is LogsContract.Event.AddToBlockList -> onAddToBlockList(event.domain)
            LogsContract.Event.OnDismissDialog -> _uiState.update {
                it.copy(dialogType = LogsDialogType.None)
            }

            is LogsContract.Event.OnLogLimitChanged -> _bottomSheetUiState.update {
                it.copy(logsLimit = event.limit)
            }

            is LogsContract.Event.OnLogSelected -> _uiState.update {
                it.copy(dialogType = LogsDialogType.ShowDetailsDialog(event.log))
            }

            is LogsContract.Event.OnSearchQueryChanged -> _searchUiState.update {
                it.copy(searchQuery = event.query)
            }

            LogsContract.Event.OnShowSearchView -> _searchUiState.update {
                it.copy(showSearchView = true)
            }.also {
                fetchLogsUseCase.setRefreshMode(RefreshMode.Manual)
            }

            is LogsContract.Event.OnDateConfirmed -> {
                _uiState.update {
                    it.copy(
                        dialogType = LogsDialogType.ShowTimePickerDialog(
                            event.type,
                            event.date
                        )
                    )
                }
            }

            is LogsContract.Event.OnShowDatePicker -> {
                _uiState.update {
                    it.copy(dialogType = LogsDialogType.ShowDatePickerDialog(event.type))
                }
            }

            is LogsContract.Event.OnTimeConfirmed -> {
                _uiState.update { it.copy(dialogType = LogsDialogType.None) }
                _bottomSheetUiState.update {
                    if (event.type == PickerType.FromTime) {
                        it.copy(filterFromTime = event.time.toEpochSeconds())
                    } else {
                        it.copy(filterToTime = event.time.toEpochSeconds())
                    }
                }
            }

            LogsContract.Event.OnToTimeCleared -> _bottomSheetUiState.update {
                it.copy(filterToTime = null)
            }

            LogsContract.Event.OnFromTimeCleared -> _bottomSheetUiState.update {
                it.copy(filterFromTime = null)
            }

            is LogsContract.Event.OnSortTypeSelected -> _uiState.update {
                it.copy(sorting = event.sorting)
            }

            LogsContract.Event.OnClearFiltersClick -> _bottomSheetUiState.update {
                it.copy(
                    selectedLogEntryStatus = LogEntryStatus.ALL,
                    selectedClientIp = "ALL",
                    selectedClientName = "ALL",
                    selectedQueryType = "ALL",
                    selectedAdvancedStatus = "ALL",
                    filterToTime = null,
                    filterFromTime = null
                )
            }

            is LogsContract.Event.OnStatusChanged -> handleStatusFilterChanged(event.status)
            is LogsContract.Event.OnClearSearchQuery -> handleClearSearchQuery(event.query)

            is LogsContract.Event.OnSearchExpandedChanged -> _searchUiState.update {
                it.copy(showSearchView = event.expanded)
            }

            is LogsContract.Event.OnLiveLoggingChanged -> _uiState.update {
                it.copy(liveLogging = event.isLive)
            }.also {
                fetchLogsUseCase.setRefreshMode(
                    RefreshMode.Automatic(if (event.isLive) 1_000 else null)
                )
                fetchLogsUseCase.refresh()
            }

            is LogsContract.Event.OnToggleFiltersBottomSheet -> _bottomSheetUiState.update {
                it.copy(showFilterBottomSheet = event.show)
            }

            is LogsContract.Event.OnToggleAdvancedFilteringOptions -> _bottomSheetUiState.update {
                it.copy(
                    showAdvancedFiltering = event.show,
                    showBasicFiltering = if (event.show) false else it.showBasicFiltering
                )
            }

            is LogsContract.Event.OnToggleBasicFilteringOptions -> _bottomSheetUiState.update {
                it.copy(
                    showBasicFiltering = event.show,
                    showAdvancedFiltering = if (event.show) false else it.showAdvancedFiltering
                )
            }

            is LogsContract.Event.OnAdvancedStatusFilterChanged -> _bottomSheetUiState.update {
                it.copy(selectedAdvancedStatus = event.advancedStatus)
            }

            is LogsContract.Event.OnClientIpFilterChanged -> _bottomSheetUiState.update {
                it.copy(selectedClientIp = event.ip)
            }

            is LogsContract.Event.OnClientNameFilterChanged -> _bottomSheetUiState.update {
                it.copy(selectedClientName = event.name)
            }

            is LogsContract.Event.OnQueryTypeFilterChanged -> _bottomSheetUiState.update {
                it.copy(selectedQueryType = event.type)
            }
        }
    }

    private fun handleStatusFilterChanged(status: LogEntryStatus) {
        _bottomSheetUiState.update { it.copy(selectedLogEntryStatus = status) }
    }

    private fun onAddToAllowList(domain: String) {
        viewModelScope.launch {
            addFilterRuleUseCase(
                domain,
                groups = listOf(0),
                comment = null,
                FilterRuleTypeEntity.ALLOW
            ).onFailure {
                Timber.e(it, "Failed to add domain to allow list: $domain")
                emitSideEffect(
                    LogsContract.Effect.Snackbar.AddDomainToAllowListFailed(
                        domain = domain,
                        reason = UiText.StringResourceWithArgs(
                            R.string.logs_adding_to_allow_list_failed,
                            it.message ?: "Unknown"
                        )
                    )
                ).also {
                    _uiState.update { state -> state.copy(dialogType = LogsDialogType.None) }
                }
            }.onSuccess {
                emitSideEffect(LogsContract.Effect.Snackbar.DomainAddedToAllowList).also {
                    _uiState.update { it.copy(dialogType = LogsDialogType.None) }
                }
            }
        }
    }

    private fun onAddToBlockList(domain: String) {
        viewModelScope.launch {
            addFilterRuleUseCase(
                domain,
                groups = listOf(0),
                comment = null,
                FilterRuleTypeEntity.DENY
            ).onFailure {
                Timber.e(it, "Failed to add domain to block list: $domain")
                emitSideEffect(
                    LogsContract.Effect.Snackbar.AddDomainToDenyListFailed(
                        domain = domain,
                        reason = UiText.StringResourceWithArgs(
                            R.string.logs_adding_to_deny_list_failed,
                            it.message ?: "Unknown"
                        )
                    )
                ).also {
                    _uiState.update { state -> state.copy(dialogType = LogsDialogType.None) }
                }
            }.onSuccess {
                emitSideEffect(LogsContract.Effect.Snackbar.DomainAddedToBlockList).also {
                    _uiState.update { it.copy(dialogType = LogsDialogType.None) }
                }
            }
        }
    }

    private fun handleClearSearchQuery(query: String) {
        if (query.isEmpty()) {
            _searchUiState.update { it.copy(showSearchView = false) }
        } else {
            _searchUiState.update { it.copy(searchQuery = "") }
        }.also {
            fetchLogsUseCase.setRefreshMode(
                RefreshMode.Automatic(if (_uiState.value.liveLogging) 1_000 else null)
            )
        }
    }
}
