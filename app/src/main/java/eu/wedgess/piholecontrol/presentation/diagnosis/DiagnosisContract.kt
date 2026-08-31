package eu.wedgess.piholecontrol.presentation.diagnosis

import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.common.model.SelectionMode
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.diagnosis.model.DiagnosisDialogType
import eu.wedgess.piholecontrol.presentation.diagnosis.model.DiagnosisMessageInfo
import eu.wedgess.piholecontrol.utils.UiText

interface DiagnosisContract {

    data class UiState(
        val messagesResult: UIResult<List<DiagnosisMessageInfo>>,
        val isRefreshing: Boolean,
        val selectionMode: SelectionMode<Int>,
        val dialogType: DiagnosisDialogType
    ) {
        companion object {
            fun initial() = UiState(
                messagesResult = UIResult.Loading(ResultType.Loading.WithTitle()),
                isRefreshing = false,
                selectionMode = SelectionMode.Inactive,
                dialogType = DiagnosisDialogType.None
            )
        }
    }

    sealed interface Effect {
        sealed class Toast(val message: UiText) : Effect {
            data object MessageDismissed :
                Toast(UiText.StringResource(R.string.diagnosis_toast_message_dismissed))

            data object MessageDismissFailed :
                Toast(UiText.StringResource(R.string.diagnosis_toast_message_dismiss_failed))

            data object RefreshFailed :
                Toast(UiText.StringResource(R.string.diagnosis_toast_refresh_failed))
        }
    }

    sealed interface Event {
        data class OnDiagnosisMessageClick(val message: DiagnosisMessageInfo) : Event
        data class OnDiagnosisMessageLongClick(val message: DiagnosisMessageInfo) : Event
        data class OnDismissDiagnosisMessageClick(val message: DiagnosisMessageInfo) : Event
        data object OnDismissDiagnosisMessageConfirmed : Event
        data object OnDismissSelectedDiagnosisMessagesClick : Event
        data object OnDismissSelectedDiagnosisMessagesConfirmed : Event
        data object OnClearSelection : Event
        data object OnDismissDialog : Event
        data object OnRefresh : Event
        data object OnRetry : Event
    }
}
