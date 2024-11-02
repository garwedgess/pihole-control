package eu.wedgess.mihole.ui.connections.modify.view

import androidx.camera.core.ExperimentalGetImage
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.mlkit.vision.barcode.BarcodeScanner
import eu.wedgess.mihole.ui.connections.modify.view.components.dialog.ApiTokenScannerDialog
import eu.wedgess.mihole.ui.connections.modify.ModifyConnectionsContract
import eu.wedgess.mihole.ui.connections.modify.view.components.ModifyConnectionContent
import eu.wedgess.mihole.ui.connections.modify.view.components.dialog.ModifyConnectionDialogs
import eu.wedgess.mihole.ui.theme.MiHoleTheme

@ExperimentalGetImage
@Composable
fun ModifyConnectionScreen(
    uiState: ModifyConnectionsContract.UiState,
    onEvent: (ModifyConnectionsContract.Event) -> Unit
) {
    Scaffold(
        modifier = Modifier.padding(MiHoleTheme.dimens.padding.screenContent)
    ) { paddingValues ->
        ModifyConnectionContent(
            paddingValues = paddingValues,
            uiState = uiState,
            onEvent = onEvent
        )

        ModifyConnectionDialogs(uiState.dialogType, onEvent)
    }
}