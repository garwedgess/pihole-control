package eu.wedgess.piholecontrol.presentation.diagnosis.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.usecases.diagnosis.DismissDiagnosisMessagesUseCase
import eu.wedgess.piholecontrol.domain.usecases.diagnosis.FetchDiagnosisMessagesUseCase
import eu.wedgess.piholecontrol.presentation.base.EventDrivenViewModel
import eu.wedgess.piholecontrol.presentation.base.SideEffectViewModel
import eu.wedgess.piholecontrol.presentation.base.SideEffectViewModelImpl
import eu.wedgess.piholecontrol.presentation.common.model.SelectionMode
import eu.wedgess.piholecontrol.presentation.common.model.selectedItems
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.diagnosis.DiagnosisContract
import eu.wedgess.piholecontrol.presentation.diagnosis.extensions.toInfo
import eu.wedgess.piholecontrol.presentation.diagnosis.model.DiagnosisDialogType
import eu.wedgess.piholecontrol.presentation.diagnosis.model.DiagnosisMessageInfo
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
class DiagnosisViewModel @Inject constructor(
    private val fetchDiagnosisMessagesUseCase: FetchDiagnosisMessagesUseCase,
    private val dismissDiagnosisMessagesUseCase: DismissDiagnosisMessagesUseCase
) : ViewModel(),
    EventDrivenViewModel<DiagnosisContract.Event>,
    SideEffectViewModel<DiagnosisContract.Effect> by SideEffectViewModelImpl() {

    private val _uiState = MutableStateFlow(DiagnosisContract.UiState.initial())
    private val _messagesResult = MutableStateFlow<UIResult<List<DiagnosisMessageInfo>>>(
        UIResult.Loading(ResultType.Loading.WithTitle())
    )

    private val messagesResult = _messagesResult
        .onStart { fetchDiagnosisMessages() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UIResult.Loading(ResultType.Loading.WithTitle())
        )

    val uiState = combine(
        _uiState,
        messagesResult
    ) { uiState, messagesResult ->
        uiState.copy(messagesResult = messagesResult)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DiagnosisContract.UiState.initial()
        )

    override fun onEvent(event: DiagnosisContract.Event) {
        when (event) {
            is DiagnosisContract.Event.OnDiagnosisMessageClick -> updateUiState {
                if (selectionMode is SelectionMode.Active) {
                    copy(selectionMode = selectionMode.toggle(event.message.id))
                } else {
                    copy(dialogType = DiagnosisDialogType.ShowMessageDetails(event.message))
                }
            }
            is DiagnosisContract.Event.OnDiagnosisMessageLongClick -> updateUiState {
                copy(selectionMode = selectionMode.toggle(event.message.id))
            }
            is DiagnosisContract.Event.OnDismissDiagnosisMessageClick -> updateUiState {
                copy(dialogType = DiagnosisDialogType.ConfirmDismissMessage(event.message))
            }
            DiagnosisContract.Event.OnDismissDiagnosisMessageConfirmed ->
                dismissDiagnosisMessage()
            DiagnosisContract.Event.OnDismissSelectedDiagnosisMessagesClick -> updateUiState {
                val selectedCount = selectionMode.selectedItems().size
                if (selectedCount == 0) {
                    copy(selectionMode = SelectionMode.Inactive)
                } else {
                    copy(
                        dialogType = DiagnosisDialogType.ConfirmDismissSelectedMessages(
                            selectedCount
                        )
                    )
                }
            }
            DiagnosisContract.Event.OnDismissSelectedDiagnosisMessagesConfirmed ->
                dismissSelectedDiagnosisMessages()
            DiagnosisContract.Event.OnClearSelection -> updateUiState {
                copy(selectionMode = SelectionMode.Inactive)
            }
            DiagnosisContract.Event.OnDismissDialog -> updateUiState {
                copy(dialogType = DiagnosisDialogType.None)
            }
            DiagnosisContract.Event.OnRefresh -> fetchDiagnosisMessages(isRefreshing = true)
            DiagnosisContract.Event.OnRetry -> fetchDiagnosisMessages()
        }
    }

    private fun fetchDiagnosisMessages(isRefreshing: Boolean = false) {
        viewModelScope.launch {
            if (isRefreshing) {
                updateUiState { copy(isRefreshing = true) }
            } else {
                _messagesResult.update { UIResult.Loading(ResultType.Loading.WithTitle()) }
            }
            fetchDiagnosisMessagesUseCase()
                .onSuccess { messages ->
                    _messagesResult.update {
                        messages.map { it.toInfo() }.toMessagesResult()
                    }
                    updateUiState { copy(isRefreshing = false) }
                }
                .onFailure { error ->
                    Timber.e(error, "Failed to fetch diagnosis messages")
                    if (isRefreshing) {
                        emitSideEffect(DiagnosisContract.Effect.Toast.RefreshFailed)
                        updateUiState { copy(isRefreshing = false) }
                    } else {
                        _messagesResult.update {
                            UIResult.Error(
                                ResultType.Error.WithTitleAndSubTitleAndRetry(
                                    title = UiText.StringResource(
                                        R.string.diagnosis_fetch_error_title
                                    ),
                                    subTitle = UiText.StringResourceWithArgs(
                                        R.string.diagnosis_fetch_error,
                                        error.message.orEmpty()
                                    ),
                                    onRetry = { onEvent(DiagnosisContract.Event.OnRetry) }
                                )
                            )
                        }
                    }
                }
        }
    }

    private fun dismissDiagnosisMessage() {
        val dialog =
            _uiState.value.dialogType as? DiagnosisDialogType.ConfirmDismissMessage ?: return

        viewModelScope.launch {
            dismissDiagnosisMessagesUseCase(listOf(dialog.message.id))
                .onSuccess {
                    emitSideEffect(DiagnosisContract.Effect.Toast.MessageDismissed)
                    removeDiagnosisMessage(dialog.message)
                }
                .onFailure { error ->
                    Timber.e(error, "Failed to dismiss diagnosis message")
                    emitSideEffect(DiagnosisContract.Effect.Toast.MessageDismissFailed)
                    updateUiState { copy(dialogType = DiagnosisDialogType.None) }
                }
        }
    }

    private fun dismissSelectedDiagnosisMessages() {
        val selectedMessageIds = _uiState.value.selectionMode.selectedItems()
        if (selectedMessageIds.isEmpty()) {
            updateUiState {
                copy(
                    selectionMode = SelectionMode.Inactive,
                    dialogType = DiagnosisDialogType.None
                )
            }
            return
        }

        viewModelScope.launch {
            dismissDiagnosisMessagesUseCase(selectedMessageIds.toList())
                .onSuccess {
                    emitSideEffect(DiagnosisContract.Effect.Toast.MessageDismissed)
                    removeDiagnosisMessages(selectedMessageIds)
                }
                .onFailure { error ->
                    Timber.e(error, "Failed to dismiss selected diagnosis messages")
                    emitSideEffect(DiagnosisContract.Effect.Toast.MessageDismissFailed)
                    updateUiState { copy(dialogType = DiagnosisDialogType.None) }
                }
        }
    }

    private fun removeDiagnosisMessage(message: DiagnosisMessageInfo) {
        removeDiagnosisMessages(setOf(message.id))
    }

    private fun removeDiagnosisMessages(messageIds: Set<Int>) {
        val currentMessages = (messagesResult.value as? UIResult.Loaded)?.data.orEmpty()
        _messagesResult.update {
            currentMessages.filterNot { it.id in messageIds }.toMessagesResult()
        }
        updateUiState {
            copy(
                selectionMode = SelectionMode.Inactive,
                dialogType = DiagnosisDialogType.None
            )
        }
    }

    private fun updateUiState(block: DiagnosisContract.UiState.() -> DiagnosisContract.UiState) {
        _uiState.update { it.block() }
    }

    private fun SelectionMode<Int>.toggle(item: Int): SelectionMode<Int> {
        val selected = when (this) {
            is SelectionMode.Active -> selected.toMutableSet()
            SelectionMode.Inactive -> mutableSetOf()
        }
        if (!selected.add(item)) {
            selected.remove(item)
        }
        return if (selected.isEmpty()) SelectionMode.Inactive else SelectionMode.Active(selected)
    }

    private fun List<DiagnosisMessageInfo>.toMessagesResult(): UIResult<List<DiagnosisMessageInfo>> {
        return if (isEmpty()) {
            UIResult.Empty(
                ResultType.Empty.WithTitleAndSubTitle(
                    title = UiText.StringResource(R.string.diagnosis_empty_title),
                    subTitle = UiText.StringResource(R.string.diagnosis_empty_message)
                )
            )
        } else {
            UIResult.Loaded(this)
        }
    }
}
