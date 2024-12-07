package eu.wedgess.piholecontrol.presentation.connections.modify.model

import com.google.mlkit.vision.barcode.BarcodeScanner

sealed interface ModifyConnectionDialogType {
    data object None : ModifyConnectionDialogType
    data class ApiTokenScanner(val barcodeScanner: BarcodeScanner) : ModifyConnectionDialogType

}