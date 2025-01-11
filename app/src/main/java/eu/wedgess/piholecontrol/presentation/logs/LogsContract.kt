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
        val logsLimit: Int,
        val selectedLogEntryStatus: LogEntryStatus,
        val filterFromTime: Long?,
        val filterToTime: Long?,
    ) {
        companion object {
            internal val availableLogLimits = listOf(500, 1000, 2500, 5000)
            fun initial() = BottomSheetUiState(
                logsLimit = availableLogLimits.first(),
                selectedLogEntryStatus = LogEntryStatus.ALL,
                filterFromTime = null,
                filterToTime = null,
            )
        }
    }

    data class UiState(
        val logs: List<LogEntryInfo>,
        val sorting: LogSorting,
        val searchQuery: String,
        val showSearchView: Boolean,
        val showSortingDropdownMenu: Boolean,
        val dialogType: LogsDialogType
    ) {

        companion object {

            fun initial() = UiState(
                logs = emptyList(),
                searchQuery = "",
                showSearchView = false,
                showSortingDropdownMenu = false,
                sorting = LogSorting.DATE_DESC,
                dialogType = LogsDialogType.None
            )
        }
    }

    sealed interface Effect {
        sealed class Snackbar(val message: UiText) : Effect {
            data object DomainAddedToAllowList :
                Snackbar(message = UiText.StringResource(R.string.logs_domain_added_to_allow_list))

            data object DomainAddedToBlockList :
                Snackbar(message = UiText.StringResource(R.string.logs_domain_added_to_block_list))
        }
    }

    sealed interface Event {
        data object OnShowSearchView : Event
        data object OnSearchClick : Event
        data object OnShowSortingMenu : Event
        data object OnHideSortingMenu : Event
        data object OnDismissDialog : Event
        data object OnFromTimeCleared : Event
        data object OnToTimeCleared : Event
        data object OnSortingDismissed : Event
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
    }
}
