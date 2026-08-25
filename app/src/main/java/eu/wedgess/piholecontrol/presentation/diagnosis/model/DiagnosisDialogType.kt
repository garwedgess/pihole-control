package eu.wedgess.piholecontrol.presentation.diagnosis.model

sealed interface DiagnosisDialogType {
    data object None : DiagnosisDialogType
    data class ShowMessageDetails(val message: DiagnosisMessageInfo) : DiagnosisDialogType
    data class ConfirmDismissMessage(val message: DiagnosisMessageInfo) : DiagnosisDialogType
}
