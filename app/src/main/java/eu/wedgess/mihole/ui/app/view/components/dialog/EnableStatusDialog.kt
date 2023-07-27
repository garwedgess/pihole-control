package eu.wedgess.mihole.ui.app.view.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import eu.wedgess.mihole.ui.app.view.components.dialog.components.EnableStatusDialogContent
import eu.wedgess.mihole.ui.common.previews.ThemePreview
import eu.wedgess.mihole.ui.theme.MiHoleTheme

@Composable
fun EnableStatusDialog(
    onEnabledStatus: () -> Unit,
    onDismissDialog: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissDialog() }) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = MiHoleTheme.dimens.size.dialogTonalElevation,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            EnableStatusDialogContent(
                onEnable = { onEnabledStatus() },
                onDismiss = { onDismissDialog() }
            )
        }
    }
}

@ThemePreview
@Composable
private fun StatusDialogPreview() {
    MiHoleTheme {
        EnableStatusDialog(
            onEnabledStatus = {},
            onDismissDialog = {}
        )
    }
}