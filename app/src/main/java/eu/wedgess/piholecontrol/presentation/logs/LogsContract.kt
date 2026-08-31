package eu.wedgess.piholecontrol.presentation.logs

import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.logs.model.LogEntryInfo
import eu.wedgess.piholecontrol.presentation.logs.model.LogEntryStatus
import eu.wedgess.piholecontrol.presentation.logs.model.LogSorting
import eu.wedgess.piholecontrol.presentation.logs.model.LogsDialogType
import eu.wedgess.piholecontrol.presentation.logs.model.PickerType
import eu.wedgess.piholecontrol.utils.UiText
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

interface LogsContract {

    data class BottomSheetUiState(
        val showFilterBottomSheet: Boolean,
        val logsLimit: Int,
        val selectedLogEntryStatus: LogEntryStatus,
        val filterFromTime: Long?,
        val filterToTime: Long?,
        val availableClientIps: List<String>,
        val selectedClientIp: String,
        val availableClientNames: List<String>,
        val selectedClientName: String,
        val availableQueryTypes: List<String>,
        val selectedQueryType: String,
        val availableStatuses: List<String>,
        val selectedAdvancedStatus: String,
        val showBasicFiltering: Boolean,
        val showAdvancedFiltering: Boolean
    ) {
        companion object {
            internal val availableLogLimits = listOf(100, 500, 1000, 2500)
            fun initial() = BottomSheetUiState(
                showFilterBottomSheet = false,
                logsLimit = availableLogLimits.first(),
                selectedLogEntryStatus = LogEntryStatus.ALL,
                filterFromTime = null,
                filterToTime = null,
                availableClientIps = emptyList(),
                availableClientNames = emptyList(),
                availableQueryTypes = emptyList(),
                availableStatuses = emptyList(),
                selectedClientIp = "ALL",
                selectedClientName = "ALL",
                selectedQueryType = "ALL",
                selectedAdvancedStatus = "ALL",
                showBasicFiltering = true,
                showAdvancedFiltering = false
            )
        }
    }

    data class SearchUiState(
        val searchQuery: String,
        val showSearchView: Boolean
    ) {
        companion object {
            fun initial() = SearchUiState(
                searchQuery = "",
                showSearchView = false
            )
        }
    }

    data class UiState(
        val logs: List<LogEntryInfo>,
        val sorting: LogSorting,
        val liveLogging: Boolean,
        val dialogType: LogsDialogType
    ) {

        companion object {

            fun initial() = UiState(
                logs = emptyList(),
                sorting = LogSorting.DATE_DESC,
                dialogType = LogsDialogType.None,
                liveLogging = false
            )
        }
    }

    sealed interface Effect {
        sealed class Snackbar(val message: UiText) : Effect {
            data object DomainAddedToAllowList :
                Snackbar(message = UiText.StringResource(R.string.logs_domain_added_to_allow_list))

            data class AddDomainToAllowListFailed(val domain: String, val reason: UiText) :
                Snackbar(message = reason)

            data object DomainAddedToBlockList :
                Snackbar(message = UiText.StringResource(R.string.logs_domain_added_to_deny_list))

            data class AddDomainToDenyListFailed(val domain: String, val reason: UiText) :
                Snackbar(message = reason)
        }
    }

    sealed interface Event {
        data object OnShowSearchView : Event
        data object OnDismissDialog : Event
        data object OnFromTimeCleared : Event
        data object OnToTimeCleared : Event
        data object OnClearFiltersClick : Event
        data class OnLogLimitChanged(val limit: Int) : Event
        data class OnStatusChanged(val status: LogEntryStatus) : Event
        data class OnDateConfirmed(val type: PickerType, val date: LocalDate) : Event
        data class OnTimeConfirmed(val type: PickerType, val time: LocalDateTime) : Event
        data class OnShowDatePicker(val type: PickerType) : Event
        data class OnSortTypeSelected(val sorting: LogSorting) : Event
        data class OnLogSelected(val log: LogEntryInfo) : Event
        data class AddToAllowList(val domain: String) : Event
        data class AddToBlockList(val domain: String) : Event
        data class OnClearSearchQuery(val query: String) : Event
        data class OnSearchExpandedChanged(val expanded: Boolean) : Event
        data class OnSearchQueryChanged(val query: String) : Event
        data class OnLiveLoggingChanged(val isLive: Boolean) : Event
        data class OnToggleFiltersBottomSheet(val show: Boolean) : Event
        data class OnToggleBasicFilteringOptions(val show: Boolean) : Event
        data class OnToggleAdvancedFilteringOptions(val show: Boolean) : Event
        data class OnClientIpFilterChanged(val ip: String) : Event
        data class OnClientNameFilterChanged(val name: String) : Event
        data class OnQueryTypeFilterChanged(val type: String) : Event
        data class OnAdvancedStatusFilterChanged(val advancedStatus: String) : Event
    }
}
