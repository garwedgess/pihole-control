package eu.wedgess.piholecontrol.presentation.localdns.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.usecases.localdns.AddLocalDnsRecordUseCase
import eu.wedgess.piholecontrol.domain.usecases.localdns.DeleteLocalDnsRecordUseCase
import eu.wedgess.piholecontrol.domain.usecases.localdns.FetchLocalDnsRecordsUseCase
import eu.wedgess.piholecontrol.domain.usecases.localdns.UpdateLocalDnsRecordUseCase
import eu.wedgess.piholecontrol.presentation.base.EventDrivenViewModel
import eu.wedgess.piholecontrol.presentation.base.SideEffectViewModel
import eu.wedgess.piholecontrol.presentation.base.SideEffectViewModelImpl
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.localdns.LocalDnsContract
import eu.wedgess.piholecontrol.presentation.localdns.extensions.toDraft
import eu.wedgess.piholecontrol.presentation.localdns.extensions.toInfo
import eu.wedgess.piholecontrol.presentation.localdns.extensions.toUpdateEntity
import eu.wedgess.piholecontrol.presentation.localdns.model.LocalDnsDialogType
import eu.wedgess.piholecontrol.presentation.localdns.model.LocalDnsRecordDraft
import eu.wedgess.piholecontrol.presentation.localdns.model.LocalDnsRecordInfo
import eu.wedgess.piholecontrol.utils.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LocalDnsViewModel @Inject constructor(
    private val fetchLocalDnsRecordsUseCase: FetchLocalDnsRecordsUseCase,
    private val addLocalDnsRecordUseCase: AddLocalDnsRecordUseCase,
    private val updateLocalDnsRecordUseCase: UpdateLocalDnsRecordUseCase,
    private val deleteLocalDnsRecordUseCase: DeleteLocalDnsRecordUseCase
) : ViewModel(),
    EventDrivenViewModel<LocalDnsContract.Event>,
    SideEffectViewModel<LocalDnsContract.Effect> by SideEffectViewModelImpl() {

    private val _uiState = MutableStateFlow(LocalDnsContract.UiState.initial())
    private val _recordsResult = MutableStateFlow<UIResult<List<LocalDnsRecordInfo>>>(
        UIResult.Loading(ResultType.Loading.WithTitle())
    )

    private val recordsResult = _recordsResult
        .onStart { fetchLocalDnsRecords() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UIResult.Loading(ResultType.Loading.WithTitle())
        )

    val uiState = combine(
        _uiState,
        recordsResult
    ) { uiState, recordsResult ->
        uiState.copy(recordsResult = recordsResult)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LocalDnsContract.UiState.initial()
        )

    override fun onEvent(event: LocalDnsContract.Event) {
        when (event) {
            LocalDnsContract.Event.OnShowSearchView -> updateUiState { copy(showSearchView = true) }
            is LocalDnsContract.Event.OnSearchExpandedChanged -> updateUiState {
                copy(showSearchView = event.expanded)
            }
            is LocalDnsContract.Event.OnSearchQueryChanged -> updateUiState {
                copy(searchQuery = event.query)
            }
            is LocalDnsContract.Event.OnClearSearchQuery -> handleClearSearchQuery(event.query)
            LocalDnsContract.Event.OnAddLocalDnsRecordClick -> updateUiState {
                copy(dialogType = LocalDnsDialogType.AddLocalDnsRecord(LocalDnsRecordDraft()))
            }
            is LocalDnsContract.Event.OnLocalDnsRecordClick -> updateUiState {
                copy(dialogType = LocalDnsDialogType.ShowLocalDnsRecordInfo(event.record))
            }
            is LocalDnsContract.Event.OnEditLocalDnsRecordClick -> updateUiState {
                copy(
                    dialogType = LocalDnsDialogType.EditLocalDnsRecord(
                        originalValue = event.record.rawValue,
                        draft = event.record.toDraft()
                    )
                )
            }
            is LocalDnsContract.Event.OnDeleteLocalDnsRecordClick -> updateUiState {
                copy(dialogType = LocalDnsDialogType.ConfirmDeleteLocalDnsRecord(event.record))
            }
            LocalDnsContract.Event.OnAddLocalDnsRecordConfirmed -> addLocalDnsRecord()
            LocalDnsContract.Event.OnUpdateLocalDnsRecordConfirmed -> updateLocalDnsRecord()
            LocalDnsContract.Event.OnDeleteLocalDnsRecordConfirmed -> deleteLocalDnsRecord()
            is LocalDnsContract.Event.OnLocalDnsRecordIpAddressChanged ->
                updateLocalDnsRecordDraft(ipAddress = event.ipAddress)
            is LocalDnsContract.Event.OnLocalDnsRecordDomainChanged ->
                updateLocalDnsRecordDraft(domain = event.domain)
            LocalDnsContract.Event.OnRefresh -> fetchLocalDnsRecords(isRefreshing = true)
            LocalDnsContract.Event.OnDismissDialog -> updateUiState {
                copy(dialogType = LocalDnsDialogType.None)
            }
            LocalDnsContract.Event.OnRetry -> fetchLocalDnsRecords()
        }
    }

    private fun fetchLocalDnsRecords(isRefreshing: Boolean = false) {
        viewModelScope.launch {
            if (isRefreshing) {
                updateUiState { copy(isRefreshing = true) }
            } else {
                _recordsResult.update { UIResult.Loading(ResultType.Loading.WithTitle()) }
            }
            fetchLocalDnsRecordsUseCase()
                .onSuccess { records ->
                    _recordsResult.update { records.map { it.toInfo() }.toRecordsResult() }
                    updateUiState { copy(isRefreshing = false) }
                }
                .onFailure { error ->
                    Timber.e(error, "Failed to fetch local DNS records")
                    if (isRefreshing) {
                        emitSideEffect(LocalDnsContract.Effect.Toast.RefreshFailed)
                        updateUiState { copy(isRefreshing = false) }
                    } else {
                        _recordsResult.update {
                            UIResult.Error(
                                ResultType.Error.WithTitleAndSubTitleAndRetry(
                                    title = UiText.StringResource(
                                        R.string.local_dns_fetch_error_title
                                    ),
                                    subTitle = UiText.StringResourceWithArgs(
                                        R.string.local_dns_fetch_error,
                                        error.message.orEmpty()
                                    ),
                                    onRetry = { onEvent(LocalDnsContract.Event.OnRetry) }
                                )
                            )
                        }
                    }
                }
        }
    }

    private fun addLocalDnsRecord() {
        val dialog = _uiState.value.dialogType as? LocalDnsDialogType.AddLocalDnsRecord ?: return
        if (!dialog.draft.canConfirm) return

        viewModelScope.launch {
            addLocalDnsRecordUseCase(
                ipAddress = dialog.draft.ipAddress,
                domain = dialog.draft.domain
            ).onSuccess {
                emitSideEffect(LocalDnsContract.Effect.Toast.RecordAdded)
                updateLocalDnsRecords {
                    (it + dialog.draft.toInfo()).sortedBy { record -> record.domain.lowercase() }
                }
            }.onFailure { error ->
                Timber.e(error, "Failed to add local DNS record")
                emitSideEffect(LocalDnsContract.Effect.Toast.RecordAddFailed)
                updateUiState { copy(dialogType = LocalDnsDialogType.None) }
            }
        }
    }

    private fun updateLocalDnsRecord() {
        val dialog = _uiState.value.dialogType as? LocalDnsDialogType.EditLocalDnsRecord ?: return
        if (!dialog.draft.canConfirm) return

        viewModelScope.launch {
            updateLocalDnsRecordUseCase(dialog.draft.toUpdateEntity(dialog.originalValue))
                .onSuccess {
                    emitSideEffect(LocalDnsContract.Effect.Toast.RecordUpdated)
                    updateLocalDnsRecords { records ->
                        records.map { record ->
                            if (record.rawValue == dialog.originalValue) {
                                dialog.draft.toInfo()
                            } else {
                                record
                            }
                        }.sortedBy { record -> record.domain.lowercase() }
                    }
                }
                .onFailure { error ->
                    Timber.e(error, "Failed to update local DNS record")
                    emitSideEffect(LocalDnsContract.Effect.Toast.RecordUpdateFailed)
                    updateUiState { copy(dialogType = LocalDnsDialogType.None) }
                }
        }
    }

    private fun deleteLocalDnsRecord() {
        val dialog =
            _uiState.value.dialogType as? LocalDnsDialogType.ConfirmDeleteLocalDnsRecord ?: return

        viewModelScope.launch {
            deleteLocalDnsRecordUseCase(dialog.record.rawValue)
                .onSuccess {
                    emitSideEffect(LocalDnsContract.Effect.Toast.RecordDeleted)
                    updateLocalDnsRecords { records ->
                        records.filterNot { it.rawValue == dialog.record.rawValue }
                    }
                }
                .onFailure { error ->
                    Timber.e(error, "Failed to delete local DNS record")
                    emitSideEffect(LocalDnsContract.Effect.Toast.RecordDeleteFailed)
                    updateUiState { copy(dialogType = LocalDnsDialogType.None) }
                }
        }
    }

    private fun handleClearSearchQuery(query: String) {
        if (query.isEmpty()) {
            updateUiState { copy(showSearchView = false) }
        } else {
            updateUiState { copy(searchQuery = "") }
        }
    }

    private fun updateLocalDnsRecordDraft(
        ipAddress: String? = null,
        domain: String? = null
    ) {
        updateUiState {
            copy(
                dialogType = when (val dialog = dialogType) {
                    is LocalDnsDialogType.AddLocalDnsRecord -> dialog.copy(
                        draft = dialog.draft.copy(
                            ipAddress = ipAddress ?: dialog.draft.ipAddress,
                            domain = domain ?: dialog.draft.domain
                        )
                    )
                    is LocalDnsDialogType.EditLocalDnsRecord -> dialog.copy(
                        draft = dialog.draft.copy(
                            ipAddress = ipAddress ?: dialog.draft.ipAddress,
                            domain = domain ?: dialog.draft.domain
                        )
                    )
                    else -> dialog
                }
            )
        }
    }

    private fun updateLocalDnsRecords(
        updateRecords: (List<LocalDnsRecordInfo>) -> List<LocalDnsRecordInfo>
    ) {
        val currentRecords = (recordsResult.value as? UIResult.Loaded)?.data.orEmpty()
        _recordsResult.update {
            updateRecords(currentRecords).toRecordsResult()
        }
        updateUiState { copy(dialogType = LocalDnsDialogType.None) }
    }

    private fun updateUiState(block: LocalDnsContract.UiState.() -> LocalDnsContract.UiState) {
        _uiState.update { it.block() }
    }

    private fun List<LocalDnsRecordInfo>.toRecordsResult(): UIResult<List<LocalDnsRecordInfo>> {
        return if (isEmpty()) {
            UIResult.Empty(
                ResultType.Empty.WithTitle(
                    UiText.StringResource(R.string.local_dns_empty_title)
                )
            )
        } else {
            UIResult.Loaded(this)
        }
    }
}
