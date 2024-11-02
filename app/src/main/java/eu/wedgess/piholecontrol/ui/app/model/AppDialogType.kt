package eu.wedgess.piholecontrol.ui.app.model

sealed interface AppDialogType {
    data object None : AppDialogType
    data object EnableAdBlocking : AppDialogType
    data object DisableAdBlocking : AppDialogType
}