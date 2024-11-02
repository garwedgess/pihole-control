package eu.wedgess.piholecontrol.ui.logs.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.piholecontrol.data.model.enums.FilterRuleType
import eu.wedgess.piholecontrol.data.model.responses.PiHoleLog
import eu.wedgess.piholecontrol.ui.base.EventDrivenViewModel
import eu.wedgess.piholecontrol.ui.base.SideEffectViewModel
import eu.wedgess.piholecontrol.ui.base.SideEffectViewModelImpl
import eu.wedgess.piholecontrol.ui.compose.ResultType
import eu.wedgess.piholecontrol.ui.compose.UIResult
import eu.wedgess.piholecontrol.ui.logs.LogsContract
import eu.wedgess.piholecontrol.ui.logs.LogsController
import eu.wedgess.piholecontrol.ui.logs.model.LogEntryStatus
import eu.wedgess.piholecontrol.ui.logs.model.LogSorting
import eu.wedgess.piholecontrol.ui.logs.model.LogsDialogType
import eu.wedgess.piholecontrol.ui.logs.model.PickerType
import eu.wedgess.piholecontrol.utils.UiText
import eu.wedgess.piholecontrol.utils.extensions.toEpochMillis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LogsViewModel @Inject constructor(
    private val controller: LogsController,
) : ViewModel(),
    EventDrivenViewModel<LogsContract.Event>,
    SideEffectViewModel<LogsContract.Effect> by SideEffectViewModelImpl() {

    private val _uiState = MutableStateFlow(LogsContract.UiState.initial())

    val uiResult = controller.logs().combine(_uiState) { logsResult, uiState ->
        logsResult.getOrElse {
            return@combine UIResult.Error(
                ResultType.Error.WithTitleAndSubTitle(
                    UiText.DynamicString("Failed to fetch logs"),
                    UiText.DynamicString(it.message ?: "Unknown error")
                )
            )
        }.run {
            if (this@run.data.isEmpty()) {
                return@combine UIResult.Empty(
                    ResultType.Empty.WithTitle(UiText.DynamicString("No logs found"))
                )
            } else {
                return@combine UIResult.Loaded(
                    uiState.copy(
                        logs = this@run.data.sortBy(uiState.sorting)
                            .run {
                                val filteredByQuery = if (uiState.searchQuery.isNotBlank()) {
                                    filter { it.requestedDomain.contains(uiState.searchQuery) }
                                } else {
                                    this
                                }
                                if (uiState.filterFromTime != null || uiState.filterToTime != null) {
                                    filteredByQuery.filter { log ->
                                        (uiState.filterFromTime == null || log.timestamp >= uiState.filterFromTime) &&
                                                (uiState.filterToTime == null || log.timestamp <= uiState.filterToTime)
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

    fun List<PiHoleLog>.sortBy(sortType: LogSorting) = when (sortType) {
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

            is LogsContract.Event.OnLogLimitChanged -> _uiState.update {
                it.copy(logsLimit = event.limit)
            }.also {
                controller.setLimit(event.limit)
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
                _uiState.update {
                    if (event.type == PickerType.FromTime) {
                        it.copy(
                            filterFromTime = event.time.toEpochMillis(),
                            dialogType = LogsDialogType.None
                        )
                    } else {
                        it.copy(
                            filterToTime = event.time.toEpochMillis(),
                            dialogType = LogsDialogType.None
                        )
                    }
                }
            }

            LogsContract.Event.OnToTimeCleared -> _uiState.update {
                it.copy(filterToTime = null)
            }

            LogsContract.Event.OnFromTimeCleared -> _uiState.update {
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
        controller.setStatusFilter(status)
        _uiState.update { it.copy(selectedLogEntryStatus = status) }
    }

    private fun onAddToAllowList(domain: String) {
        viewModelScope.launch {
            controller.addFilterRule(domain, FilterRuleType.ALLOW).onFailure {
                Timber.e("Failed to add domain to allow list: $domain", it)
            }.onSuccess {

            }
        }
    }

    private fun onAddToBlockList(domain: String) {
        viewModelScope.launch {
            controller.addFilterRule(domain, FilterRuleType.BLOCK).onFailure {
                Timber.e("Failed to add domain to block list: $domain", it)
            }.onSuccess {

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