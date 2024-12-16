package eu.wedgess.piholecontrol.presentation.app.model

sealed interface AppDialogType {
    data object None : AppDialogType
    data object EnableAdBlocking : AppDialogType
    data object DisableAdBlocking : AppDialogType
}
