package eu.wedgess.piholecontrol.ui.app.view.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import eu.wedgess.piholecontrol.ui.app.view.components.dialog.components.DisableStatusDialogContent
import eu.wedgess.piholecontrol.ui.common.previews.ThemePreview
import eu.wedgess.piholecontrol.ui.theme.PiHoleControlTheme

@Composable
fun DisableStatusDialog(
    onDisableStatus: (duration: Long) -> Unit,
    onDismissDialog: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissDialog() }) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = PiHoleControlTheme.dimens.size.dialogTonalElevation,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            DisableStatusDialogContent(
                onDisableStatus = { onDisableStatus(it) },
                onDismiss = { onDismissDialog() }
            )
        }
    }
}

@ThemePreview
@Composable
private fun DisableStatusDialogPreview() {
    PiHoleControlTheme {
        DisableStatusDialog(
            onDisableStatus = {},
            onDismissDialog = {}
        )
    }
}