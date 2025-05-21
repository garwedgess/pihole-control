package eu.wedgess.piholecontrol.presentation.connections.modify.model

import eu.wedgess.piholecontrol.utils.UiText

sealed interface ModifyConnectionDialogType {
    data object None : ModifyConnectionDialogType
    data class FailedToSaveConnection(val message: UiText) : ModifyConnectionDialogType
    data class LoadingDialog(val message: UiText) : ModifyConnectionDialogType
}
