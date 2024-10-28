package eu.wedgess.mihole.ui.logs.viewmodel

import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.mihole.R
import eu.wedgess.mihole.data.PiHoleRepository
import eu.wedgess.mihole.data.model.ResponseResult
import eu.wedgess.mihole.data.model.enums.FilterRuleType
import eu.wedgess.mihole.ui.base.RefreshableViewModel
import eu.wedgess.mihole.ui.base.UiResult
import eu.wedgess.mihole.ui.logs.LogsContract
import eu.wedgess.mihole.ui.logs.model.PickerType
import eu.wedgess.mihole.utils.UiText
import eu.wedgess.mihole.utils.extensions.handleError
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogsViewModel @Inject constructor(
    private val repository: PiHoleRepository
) : RefreshableViewModel(repository), LogsContract {

    private val _uiState: MutableStateFlow<LogsContract.UiState> =
        MutableStateFlow(LogsContract.UiState.initial())
    override val uiState: StateFlow<LogsContract.UiState> = _uiState.asStateFlow()

    private val _effect: Channel<LogsContract.Effect> = Channel(Channel.UNLIMITED)
    override val effect: Flow<LogsContract.Effect> = _effect.receiveAsFlow()

    override fun onEvent(event: LogsContract.Event) {
        when (event) {
            LogsContract.Event.FetchLogs -> fetchLogs()
            LogsContract.Event.OnShowSearchView -> showSearchView()
            LogsContract.Event.OnHideShowSearchView -> hideSearchView()
            LogsContract.Event.OnHideSortingBottomSheet -> _uiState.update { it.dismissSortingDropdown() }
            LogsContract.Event.OnShowFiltersBottomSheet -> showFiltersBottomSheet()
            LogsContract.Event.OnShowSortingBottomSheet -> _uiState.update { it.showSortingDropdown() }
            LogsContract.Event.OnSortingDismissed -> _uiState.update { it.dismissSortingDropdown() }
            LogsContract.Event.ListenForConnectionChanges -> listenForConnectionChange()
            is LogsContract.Event.OnSearchQueryChanged -> handleSearchQuery(event.query)
            is LogsContract.Event.OnLogLimitChanged -> _uiState.update {
                it.logsLimit(event.limit).copy(logs = UiResult.Loading)
            }
                .also { fetchLogs() }

            is LogsContract.Event.OnStatusChanged -> _uiState.update { it.logEntryStatus(event.status) }
            LogsContract.Event.OnDismissDatePicker -> _uiState.update { it.dismissDatePicker() }
            LogsContract.Event.OnDismissTimePicker -> _uiState.update { it.dismissTimePicker() }
            is LogsContract.Event.OnShowDatePicker -> _uiState.update { it.showDatePicker(event.type) }
            is LogsContract.Event.OnShowTimePicker -> _uiState.update { it.showTimePicker(event.type) }
            is LogsContract.Event.OnDateConfirmed -> _uiState.update {
                if (it.showDatePicker == PickerType.FromTime) {
                    it.setFromDate(from = event.date)
                } else {
                    it.setToDate(to = event.date)
                }
            }

            is LogsContract.Event.OnTimeConfirmed -> _uiState.update {
                if (it.showTimePicker == PickerType.FromTime) {
                    it.setFromTime(event.time)
                } else {
                    it.setToTime(event.time)
                }
            }

            LogsContract.Event.OnFromTimeCleared -> _uiState.update { it.fromTimeCleared() }
            LogsContract.Event.OnToTimeCleared -> _uiState.update { it.toTimeCleared() }
            is LogsContract.Event.OnSortTypeSelected -> _uiState.update { it.setSorting(event.sorting) }
            is LogsContract.Event.OnLogSelected -> _uiState.update { it.copy(selectedLog = event.log) }
            LogsContract.Event.OnLogDetailsDismissed -> _uiState.update { it.copy(selectedLog = null) }
            is LogsContract.Event.AddToAllowList -> addFilterRule(
                event.domain,
                FilterRuleType.ALLOW
            )

            is LogsContract.Event.AddToBlockList -> addFilterRule(
                event.domain,
                FilterRuleType.BLOCK
            )
        }
    }

    private fun addFilterRule(domain: String, filterRuleType: FilterRuleType) {
        viewModelScope.launch {
            repository.addFilterRules(domain, filterRuleType)
            _uiState.update { it.copy(selectedLog = null) }
            _effect.send(
                if (filterRuleType == FilterRuleType.ALLOW) {
                    LogsContract.Effect.Snackbar.DomainAddedToAllowList
                } else {
                    LogsContract.Effect.Snackbar.DomainAddedToBlockList
                }
            )
        }
    }

    private fun showFiltersBottomSheet() {
        viewModelScope.launch {
            _effect.send(LogsContract.Effect.ShowBottomSheet)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun handleSearchQuery(query: TextFieldValue) = viewModelScope.launch {
        _uiState.update { it.setSearchStateQuery(query = query) }
        snapshotFlow { query }
            .distinctUntilChanged()
            .filter { query: TextFieldValue ->
                query.text.isNotEmpty() && !_uiState.value.searchState.sameAsPreviousQuery()
            }
            .map { query: TextFieldValue ->
                _uiState.update { it.setSearchStateAsInProgress() }
                query
            }
            .debounce(0)
            .mapLatest { query: TextFieldValue ->
                delay(300)
                (_uiState.value.logs as UiResult.Success).data.filter {
                    it.requestedDomain.contains(
                        query.text,
                        ignoreCase = true
                    )
                }
            }
            .collect { results ->
                _uiState.update { it.setSearchResults(results) }
            }
    }

    private fun showSearchView() {
        _uiState.update { it.showSearchView() }
    }

    private fun hideSearchView() {
        _uiState.update { it.hideSearchView() }
    }

    private val fetchLogsErrorHandler = CoroutineExceptionHandler { _, throwable ->
        val errorMessage = throwable.message?.run {
            UiText.DynamicString(this)
        } ?: UiText.StringResource(R.string.all_error_msg_unknown)
        _uiState.update { it.logsError(errorMessage) }
    }

    private fun fetchLogs() = viewModelScope.launch(fetchLogsErrorHandler) {
        val limit = _uiState.value.logsLimit
        when (val response = repository.fetchLogs(limit)) {
            is ResponseResult.Success -> _uiState.update { it.logsFromServer(response.data.data) }

            is ResponseResult.Error -> fetchLogsErrorHandler.handleException(
                this@launch.coroutineContext,
                response.handleError()
            )
        }
    }

    override fun onRefresh() {
        fetchLogs()
    }
}