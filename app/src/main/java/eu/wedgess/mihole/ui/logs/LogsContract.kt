package eu.wedgess.mihole.ui.logs

import eu.wedgess.mihole.R
import eu.wedgess.mihole.data.model.responses.PiHoleLog
import eu.wedgess.mihole.ui.logs.model.LogEntryStatus
import eu.wedgess.mihole.ui.logs.model.LogSorting
import eu.wedgess.mihole.ui.logs.model.LogsDialogType
import eu.wedgess.mihole.ui.logs.model.PickerType
import eu.wedgess.mihole.utils.UiText
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

interface LogsContract {

    data class UiState(
        val logs: List<PiHoleLog>,
        val logsLimit: Int,
        val selectedLogEntryStatus: LogEntryStatus,
        val sorting: LogSorting,
        val searchQuery: String,
        val showSearchView: Boolean,
        val showSortingDropdownMenu: Boolean,
        val filterFromTime: Long?,
        val filterToTime: Long?,
        val dialogType: LogsDialogType
    ) {

        companion object {
            val availableLogLimits = listOf(500, 1000, 2500, 5000)
            fun initial() = UiState(
                logs = emptyList(),
                logsLimit = availableLogLimits.first(),
                searchQuery = "",
                showSearchView = false,
                showSortingDropdownMenu = false,
                selectedLogEntryStatus = LogEntryStatus.ALL,
                filterFromTime = null,
                filterToTime = null,
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
        data class OnLogSelected(val log: PiHoleLog) : Event
        data class AddToAllowList(val domain: String) : Event
        data class AddToBlockList(val domain: String) : Event
        data class OnClearSearchQuery(val query: String) : Event
        data class OnSearchExpandedChanged(val expanded: Boolean) : Event
        data class OnSearchQueryChanged(val query: String) : Event
    }
}