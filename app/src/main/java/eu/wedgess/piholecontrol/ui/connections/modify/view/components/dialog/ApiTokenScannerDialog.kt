package eu.wedgess.piholecontrol.ui.connections.modify.view.components.dialog

import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.google.mlkit.vision.barcode.BarcodeScanner
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.ui.connections.list.view.components.Scanner
import eu.wedgess.piholecontrol.ui.theme.PiHoleControlTheme

@ExperimentalGetImage
@Composable
fun ApiTokenScannerDialog(
    barcodeScanner: BarcodeScanner,
    onDismiss: () -> Unit,
    onApiTokenScanFailed: () -> Unit,
    onApiTokenScanned: (String) -> Unit
) {

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = PiHoleControlTheme.dimens.size.dialogTonalElevation,
            modifier = Modifier
                .padding(PiHoleControlTheme.dimens.padding.dialogContent)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            Column(
                modifier = Modifier.padding(PiHoleControlTheme.dimens.padding.itemContentXLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    Modifier.padding(
                        start = PiHoleControlTheme.dimens.padding.itemContentXLarge,
                        end = PiHoleControlTheme.dimens.padding.itemContentXLarge,
                        top = PiHoleControlTheme.dimens.padding.itemContentXLarge
                    )
                ) {
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
                        Text(
                            stringResource(R.string.scanner_dialog_title_scan_token),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
                Box(
                    Modifier.padding(
                        start = PiHoleControlTheme.dimens.padding.itemContentXLarge,
                        end = PiHoleControlTheme.dimens.padding.itemContentXLarge,
                        bottom = PiHoleControlTheme.dimens.padding.itemContentXLarge
                    )
                ) {
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
                        Text(
                            stringResource(R.string.scanner_dialog_hint_scan_token),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                BoxWithConstraints(modifier = Modifier.padding(vertical = PiHoleControlTheme.dimens.padding.dialogContent)) {
                    Scanner(
                        Modifier
                            .fillMaxWidth()
                            .height(maxWidth),
                        barcodeScanner = barcodeScanner,
                        onBarcodeScanSuccess = {
                            it.firstOrNull()?.rawValue?.run {
                                onApiTokenScanned(this)
                            } ?: onApiTokenScanFailed()
                        })
                }
            }
        }
    }
}