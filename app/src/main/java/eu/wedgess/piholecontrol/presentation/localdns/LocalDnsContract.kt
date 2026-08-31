package eu.wedgess.piholecontrol.presentation.localdns

import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.common.model.SelectionMode
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.localdns.model.LocalDnsDialogType
import eu.wedgess.piholecontrol.presentation.localdns.model.LocalDnsRecordInfo
import eu.wedgess.piholecontrol.utils.UiText

interface LocalDnsContract {

    data class UiState(
        val recordsResult: UIResult<List<LocalDnsRecordInfo>>,
        val searchQuery: String,
        val showSearchView: Boolean,
        val isRefreshing: Boolean,
        val selectionMode: SelectionMode<String>,
        val dialogType: LocalDnsDialogType
    ) {
        val filteredRecords: UIResult<List<LocalDnsRecordInfo>>
            get() = when (recordsResult) {
                is UIResult.Loaded -> {
                    val filteredRecords = recordsResult.data.filter { record ->
                        record.domain.contains(searchQuery, ignoreCase = true) ||
                            record.ipAddress.contains(searchQuery, ignoreCase = true)
                    }
                    if (searchQuery.isNotBlank() && filteredRecords.isEmpty()) {
                        UIResult.Empty(
                            ResultType.Empty.WithTitleAndSubTitle(
                                title = UiText.StringResource(
                                    R.string.local_dns_empty_search_title
                                ),
                                subTitle = UiText.StringResourceWithArgs(
                                    R.string.all_msg_no_results_found_for,
                                    searchQuery
                                )
                            )
                        )
                    } else {
                        UIResult.Loaded(filteredRecords)
                    }
                }
                else -> recordsResult
            }

        companion object {
            fun initial() = UiState(
                recordsResult = UIResult.Loading(ResultType.Loading.WithTitle()),
                searchQuery = "",
                showSearchView = false,
                isRefreshing = false,
                selectionMode = SelectionMode.Inactive,
                dialogType = LocalDnsDialogType.None
            )
        }
    }

    sealed interface Effect {
        sealed class Toast(val message: UiText) : Effect {
            data object RecordAdded :
                Toast(UiText.StringResource(R.string.local_dns_toast_record_added))

            data object RecordAddFailed :
                Toast(UiText.StringResource(R.string.local_dns_toast_record_add_failed))

            data object RecordUpdated :
                Toast(UiText.StringResource(R.string.local_dns_toast_record_updated))

            data object RecordUpdateFailed :
                Toast(UiText.StringResource(R.string.local_dns_toast_record_update_failed))

            data object RecordDeleted :
                Toast(UiText.StringResource(R.string.local_dns_toast_record_deleted))

            data object SelectedRecordsDeleted :
                Toast(UiText.StringResource(R.string.local_dns_toast_selected_records_deleted))

            data object RecordDeleteFailed :
                Toast(UiText.StringResource(R.string.local_dns_toast_record_delete_failed))

            data object RefreshFailed :
                Toast(UiText.StringResource(R.string.local_dns_toast_refresh_failed))
        }
    }

    sealed interface Event {
        data object OnShowSearchView : Event
        data class OnSearchExpandedChanged(val expanded: Boolean) : Event
        data class OnSearchQueryChanged(val query: String) : Event
        data class OnClearSearchQuery(val query: String) : Event
        data object OnAddLocalDnsRecordClick : Event
        data class OnLocalDnsRecordClick(val record: LocalDnsRecordInfo) : Event
        data class OnEditLocalDnsRecordClick(val record: LocalDnsRecordInfo) : Event
        data class OnDeleteLocalDnsRecordClick(val record: LocalDnsRecordInfo) : Event
        data object OnAddLocalDnsRecordConfirmed : Event
        data object OnUpdateLocalDnsRecordConfirmed : Event
        data object OnDeleteLocalDnsRecordConfirmed : Event
        data object OnDeleteSelectedLocalDnsRecordsClick : Event
        data object OnDeleteSelectedLocalDnsRecordsConfirmed : Event
        data object OnClearSelection : Event
        data class OnLocalDnsRecordLongClick(val record: LocalDnsRecordInfo) : Event
        data class OnLocalDnsRecordIpAddressChanged(val ipAddress: String) : Event
        data class OnLocalDnsRecordDomainChanged(val domain: String) : Event
        data object OnRefresh : Event
        data object OnDismissDialog : Event
        data object OnRetry : Event
    }
}
