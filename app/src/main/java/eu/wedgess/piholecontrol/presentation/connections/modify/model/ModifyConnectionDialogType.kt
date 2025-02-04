package eu.wedgess.piholecontrol.presentation.connections.modify.model

import com.google.mlkit.vision.barcode.BarcodeScanner
import eu.wedgess.piholecontrol.utils.UiText

sealed interface ModifyConnectionDialogType {
    data object None : ModifyConnectionDialogType
    data class ApiTokenScanner(val barcodeScanner: BarcodeScanner) : ModifyConnectionDialogType
    data class FailedToSaveConnection(val message: UiText) : ModifyConnectionDialogType
    data class LoadingDialog(val message: UiText) : ModifyConnectionDialogType
}
