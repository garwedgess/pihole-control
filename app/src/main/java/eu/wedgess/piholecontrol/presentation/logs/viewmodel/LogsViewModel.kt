package eu.wedgess.piholecontrol.presentation.logs.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.model.LogEntryEntity
import eu.wedgess.piholecontrol.domain.usecases.filters.AddFilterRuleUseCase
import eu.wedgess.piholecontrol.domain.usecases.logs.FetchLogsUseCase
import eu.wedgess.piholecontrol.presentation.base.EventDrivenViewModel
import eu.wedgess.piholecontrol.presentation.base.SideEffectViewModel
import eu.wedgess.piholecontrol.presentation.base.SideEffectViewModelImpl
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.logs.LogsContract
import eu.wedgess.piholecontrol.presentation.logs.model.LogEntryStatus
import eu.wedgess.piholecontrol.presentation.logs.model.LogSorting
import eu.wedgess.piholecontrol.presentation.logs.model.LogsDialogType
import eu.wedgess.piholecontrol.presentation.logs.model.PickerType
import eu.wedgess.piholecontrol.utils.UiText
import eu.wedgess.piholecontrol.utils.extensions.toEpochSeconds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LogsViewModel @Inject constructor(
    private val fetchLogsUseCase: FetchLogsUseCase,
    private val addFilterRuleUseCase: AddFilterRuleUseCase
) : ViewModel(),
    EventDrivenViewModel<LogsContract.Event>,
    SideEffectViewModel<LogsContract.Effect> by SideEffectViewModelImpl() {

    private val _uiState = MutableStateFlow(LogsContract.UiState.initial())
    private val _bottomSheetUiState = MutableStateFlow(LogsContract.BottomSheetUiState.initial())
    val bottomSheetUiState = _bottomSheetUiState.asStateFlow()

    val uiResult = combine(
        fetchLogsUseCase(),
        _uiState,
        _bottomSheetUiState
    ) { logsResult, uiState, bsUiState ->
        logsResult.getOrElse {
            return@combine UIResult.Error(
                ResultType.Error.WithTitleAndSubTitleAndRetry(
                    title = UiText.DynamicString("Failed to fetch logs"),
                    subTitle = UiText.DynamicString(it.message ?: "Unknown error"),
                    onRetry = fetchLogsUseCase::refresh
                )
            )
        }.run {
            if (this@run.isEmpty()) {
                return@combine UIResult.Empty(
                    ResultType.Empty.WithTitle(UiText.DynamicString("No logs found"))
                )
            } else {
                return@combine UIResult.Loaded(
                    uiState.copy(
                        logs = this@run.sortBy(uiState.sorting)
                            .run {
                                val filteredByQuery = if (uiState.searchQuery.isNotBlank()) {
                                    filter { it.requestedDomain.contains(uiState.searchQuery) }
                                } else {
                                    this
                                }
                                if (
                                    bsUiState.filterFromTime != null ||
                                    bsUiState.filterToTime != null
                                ) {
                                    filteredByQuery.filter { log ->
                                        (bsUiState.filterFromTime == null ||
                                                log.timestamp >= bsUiState.filterFromTime) &&
                                                (bsUiState.filterToTime == null ||
                                                        log.timestamp <= bsUiState.filterToTime)
                                    }
                                } else {
                                    filteredByQuery
                                }
                            }
                    )
                )
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        UIResult.Loading(ResultType.Loading.WithTitle())
    )

    private fun List<LogEntryEntity>.sortBy(sortType: LogSorting) = when (sortType) {
        LogSorting.DATE_DESC -> sortedByDescending { it.timestamp }
        LogSorting.DATE_ASC -> sortedBy { it.timestamp }
        LogSorting.RESPONSE_TIME_ASC -> sortedBy { it.responseTime }
        LogSorting.RESPONSE_TIME_DESC -> sortedByDescending { it.responseTime }
    }

    override fun onEvent(event: LogsContract.Event) {
        when (event) {
            is LogsContract.Event.AddToAllowList -> onAddToAllowList(event.domain)
            is LogsContract.Event.AddToBlockList -> onAddToBlockList(event.domain)
            LogsContract.Event.OnDismissDialog -> _uiState.update {
                it.copy(dialogType = LogsDialogType.None)
            }

            LogsContract.Event.OnHideSortingMenu -> _uiState.update {
                it.copy(showSortingDropdownMenu = false)
            }

            is LogsContract.Event.OnLogLimitChanged -> _bottomSheetUiState.update {
                it.copy(logsLimit = event.limit)
            }.also {
                fetchLogsUseCase.setLogLimit(event.limit)
            }

            is LogsContract.Event.OnLogSelected -> _uiState.update {
                it.copy(dialogType = LogsDialogType.ShowDetailsDialog(event.log))
            }

            is LogsContract.Event.OnSearchQueryChanged -> _uiState.update {
                it.copy(searchQuery = event.query)
            }

            LogsContract.Event.OnShowSearchView -> _uiState.update {
                it.copy(showSearchView = true)
            }

            LogsContract.Event.OnShowSortingMenu -> _uiState.update {
                it.copy(showSortingDropdownMenu = true)
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
                it.copy(sorting = event.sorting, showSortingDropdownMenu = false)
            }

            LogsContract.Event.OnSortingDismissed -> _uiState.update {
                it.copy(showSortingDropdownMenu = false)
            }

            is LogsContract.Event.OnStatusChanged -> handleStatusFilterChanged(event.status)
            is LogsContract.Event.OnClearSearchQuery -> handleClearSearchQuery(event.query)
            LogsContract.Event.OnSearchClick -> _uiState.update {
                it.copy(showSearchView = false)
            }

            is LogsContract.Event.OnSearchExpandedChanged -> _uiState.update {
                it.copy(showSearchView = event.expanded)
            }
        }
    }

    private fun handleStatusFilterChanged(status: LogEntryStatus) {
        fetchLogsUseCase.setLogStatusFilter(status)
        _bottomSheetUiState.update { it.copy(selectedLogEntryStatus = status) }
    }

    private fun onAddToAllowList(domain: String) {
        viewModelScope.launch {
            addFilterRuleUseCase(domain, FilterRuleTypeEntity.ALLOW).onFailure {
                Timber.e(it, "Failed to add domain to allow list: $domain")
            }
        }
    }

    private fun onAddToBlockList(domain: String) {
        viewModelScope.launch {
            addFilterRuleUseCase(domain, FilterRuleTypeEntity.BLOCK).onFailure {
                Timber.e(it, "Failed to add domain to block list: $domain")
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
