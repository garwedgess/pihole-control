package eu.wedgess.piholecontrol.presentation.app.view.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun EnableStatusDialog(
    onEnabledStatus: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surfaceContainerHigh
        ) {
            EnableStatusDialogContent(
                onEnable = onEnabledStatus,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
private fun EnableStatusDialogContent(
    onEnable: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier.padding(PiHoleControlTheme.dimens.padding.dialogContent),
        verticalArrangement = Arrangement.spacedBy(PiHoleControlTheme.dimens.padding.itemContent)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = PiHoleControlTheme.dimens.padding.screenContent),
            text = "Enable",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(text = "Enable ad-blocking?")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = PiHoleControlTheme.dimens.padding.dialogContent),
            horizontalArrangement = Arrangement.spacedBy(
                PiHoleControlTheme.dimens.padding.itemContent,
                Alignment.End
            )
        ) {
            TextButton(onClick = { onDismiss() }) {
                Text(text = "Cancel")
            }
            TextButton(onClick = {
                onEnable()
            }) {
                Text(text = "Confirm")
            }
        }
    }
}

@ThemePreview
@Composable
private fun StatusDialogPreview() {
    PiHoleControlTheme {
        EnableStatusDialog(
            onEnabledStatus = {},
            onDismiss = {}
        )
    }
}
