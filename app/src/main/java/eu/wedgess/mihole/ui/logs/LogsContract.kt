package eu.wedgess.mihole.ui.logs

import androidx.compose.ui.text.input.TextFieldValue
import eu.wedgess.mihole.R
import eu.wedgess.mihole.data.model.responses.PiHoleLog
import eu.wedgess.mihole.ui.base.UiResult
import eu.wedgess.mihole.ui.base.UnidirectionalViewModel
import eu.wedgess.mihole.ui.common.search.SearchState
import eu.wedgess.mihole.ui.logs.model.LogEntryStatus
import eu.wedgess.mihole.ui.logs.model.LogSorting
import eu.wedgess.mihole.ui.logs.model.PickerType
import eu.wedgess.mihole.utils.UiText
import eu.wedgess.mihole.utils.extensions.epochMillisToCurrentTimezoneEpochSeconds

interface LogsContract :
    UnidirectionalViewModel<LogsContract.UiState, LogsContract.Event, LogsContract.Effect> {

    data class UiState(
        val logs: UiResult<List<PiHoleLog>>,
        val logsLimit: Int,
        val selectedLogEntryStatus: LogEntryStatus,
        val sorting: LogSorting,
        val showSearchView: Boolean,
        val searchState: SearchState<PiHoleLog>,
        val showFiltersBottomSheet: Boolean,
        val showSortingDropdownMenu: Boolean,
        val filterFromTime: Long?,
        val filterToTime: Long?,
        val showDatePicker: PickerType?,
        val showTimePicker: PickerType?,
        val selectedLog: PiHoleLog?,
        private var allLogs: List<PiHoleLog>
    ) {

        fun logsLimit(logsLimit: Int): UiState =
            this.copy(logsLimit = logsLimit)

        fun logEntryStatus(status: LogEntryStatus): UiState =
            this.copy(selectedLogEntryStatus = status).filterLogs(allLogs)

        fun logsFromServer(logs: List<PiHoleLog>): UiState {
            this.allLogs = logs
            return this.filterLogs(logs)
        }

        fun showDatePicker(pickerType: PickerType): UiState =
            this.copy(showDatePicker = pickerType)

        fun dismissDatePicker(): UiState = this.copy(showDatePicker = null)

        fun showTimePicker(pickerType: PickerType): UiState =
            this.copy(showDatePicker = pickerType)

        fun dismissTimePicker(): UiState = this.copy(showTimePicker = null)

        fun logsError(errorMessage: UiText): UiState =
            this.copy(logs = UiResult.Error(errorMessage))

        private fun resetSearchState(): UiState =
            this.copy(searchState = SearchState())

        fun showSortingDropdown(): UiState =
            this.copy(showSortingDropdownMenu = true)

        fun dismissSortingDropdown(): UiState =
            this.copy(showSortingDropdownMenu = false)

        fun setSorting(sorting: LogSorting): UiState =
            this.copy(showSortingDropdownMenu = false, sorting = sorting).filterLogs(allLogs)

        fun setSearchResults(result: List<PiHoleLog>): UiState =
            this.copy(
                searchState = this.searchState.copy(
                    searchResults = result.statusFilter(this.selectedLogEntryStatus),
                    searching = false
                )
            )

        fun setSearchStateQuery(query: TextFieldValue): UiState =
            this.copy(searchState = this.searchState.copy(query = query))

        fun setSearchStateAsInProgress(): UiState =
            this.copy(searchState = this.searchState.copy(searching = true))

        fun showSearchView(): UiState =
            this.copy(showSearchView = true)

        fun hideSearchView(): UiState = this.copy(showSearchView = false).resetSearchState()

        fun setToDate(to: Long): UiState =
            this.copy(filterToTime = to, showTimePicker = showDatePicker, showDatePicker = null)

        fun setFromDate(from: Long): UiState =
            this.copy(filterFromTime = from, showTimePicker = showDatePicker, showDatePicker = null)

        fun setToTime(to: Long): UiState =
            this.copy(
                filterToTime = to.plus(this.filterToTime ?: 0L),
                showTimePicker = null,
                showDatePicker = null
            ).filterLogs(allLogs)

        fun setFromTime(from: Long): UiState =
            this.copy(
                filterFromTime = from.plus(this.filterFromTime ?: 0L),
                showTimePicker = null,
                showDatePicker = null
            ).filterLogs(allLogs)

        fun fromTimeCleared(): UiState =
            this.copy(filterFromTime = null).filterLogs(allLogs)

        fun toTimeCleared(): UiState =
            this.copy(filterToTime = null).filterLogs(allLogs)

        companion object {
            val availableLogLimits = listOf(500, 1000, 2500, 5000)
            fun initial() = UiState(
                logs = UiResult.Loading,
                logsLimit = availableLogLimits.first(),
                showSearchView = false,
                searchState = SearchState(),
                showFiltersBottomSheet = false,
                showSortingDropdownMenu = false,
                selectedLogEntryStatus = LogEntryStatus.ALL,
                filterFromTime = null,
                filterToTime = null,
                showTimePicker = null,
                showDatePicker = null,
                sorting = LogSorting.DATE_DESC,
                selectedLog = null,
                allLogs = emptyList()
            )

            fun List<PiHoleLog>.statusFilter(status: LogEntryStatus) =
                if (status == LogEntryStatus.ALL) this else this.filter { log ->
                    status.categories.contains(
                        log.answerType.category
                    )
                }

            fun List<PiHoleLog>.sortBy(sortType: LogSorting) =
                when (sortType) {
                    LogSorting.DATE_DESC -> sortedByDescending { it.timestamp }
                    LogSorting.DATE_ASC -> sortedBy { it.timestamp }
                    LogSorting.RESPONSE_TIME_ASC -> sortedBy { it.responseTime }
                    LogSorting.RESPONSE_TIME_DESC -> sortedByDescending { it.responseTime }
                }

            private fun List<PiHoleLog>.timeFilter(
                fromTimeMillis: Long? = null,
                toTimeMillis: Long? = null
            ) =
                if (fromTimeMillis == null) {
                    this
                } else {
                    val toTimeEpochSeconds = toTimeMillis ?: System.currentTimeMillis()
                        .epochMillisToCurrentTimezoneEpochSeconds()
                    this.filter { log ->
                        log.timestamp.times(1000L)
                            .epochMillisToCurrentTimezoneEpochSeconds() in fromTimeMillis..toTimeEpochSeconds
                    }
                }

            fun UiState.filterLogs(
                logs: List<PiHoleLog>
            ): UiState =
                this.copy(
                    logs = UiResult.Success(
                        logs.statusFilter(this.selectedLogEntryStatus)
                            .timeFilter(
                                fromTimeMillis = this.filterFromTime,
                                toTimeMillis = this.filterToTime
                            )
                            .sortBy(this.sorting)
                    )
                )
        }
    }

    sealed interface Effect {
        object ShowBottomSheet : Effect
        sealed class Snackbar(val message: UiText) : Effect {
            object DomainAddedToAllowList :
                Snackbar(message = UiText.StringResource(R.string.logs_domain_added_to_allow_list))

            object DomainAddedToBlockList :
                Snackbar(message = UiText.StringResource(R.string.logs_domain_added_to_block_list))
        }
    }

    sealed interface Event {
        object FetchLogs : Event
        object OnShowSearchView : Event
        object OnHideShowSearchView : Event
        object OnShowSortingBottomSheet : Event
        object OnHideSortingBottomSheet : Event
        object OnShowFiltersBottomSheet : Event
        object OnDismissDatePicker : Event
        object OnDismissTimePicker : Event
        object OnFromTimeCleared : Event
        object OnToTimeCleared : Event
        object OnSortingDismissed : Event
        object OnLogDetailsDismissed : Event
        object ListenForConnectionChanges : Event
        data class OnSearchQueryChanged(val query: TextFieldValue) : Event
        data class OnLogLimitChanged(val limit: Int) : Event
        data class OnStatusChanged(val status: LogEntryStatus) : Event
        data class OnDateConfirmed(val date: Long) : Event
        data class OnTimeConfirmed(val time: Long) : Event
        data class OnShowDatePicker(val type: PickerType) : Event
        data class OnShowTimePicker(val type: PickerType) : Event
        data class OnSortTypeSelected(val sorting: LogSorting) : Event
        data class OnLogSelected(val log: PiHoleLog) : Event
        data class AddToAllowList(val domain: String) : Event
        data class AddToBlockList(val domain: String) : Event
    }
}