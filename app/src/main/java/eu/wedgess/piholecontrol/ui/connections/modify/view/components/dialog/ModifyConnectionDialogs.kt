package eu.wedgess.piholecontrol.ui.connections.modify.view.components.dialog

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.runtime.Composable
import eu.wedgess.piholecontrol.ui.connections.modify.ModifyConnectionsContract
import eu.wedgess.piholecontrol.ui.connections.modify.model.ModifyConnectionDialogType

@OptIn(ExperimentalGetImage::class)
@Composable
fun ModifyConnectionDialogs(
    dialogType: ModifyConnectionDialogType,
    onEvent: (ModifyConnectionsContract.Event) -> Unit
) {
    when (dialogType) {
        ModifyConnectionDialogType.None -> Unit
        is ModifyConnectionDialogType.ApiTokenScanner -> ApiTokenScannerDialog(
            barcodeScanner = dialogType.barcodeScanner,
            onDismiss = { onEvent(ModifyConnectionsContract.Event.OnDismissDialog) },
            onApiTokenScanFailed = { },
            onApiTokenScanned = { onEvent(ModifyConnectionsContract.Event.OnApiTokenChanged(it)) }
        )
    }

}