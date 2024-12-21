package eu.wedgess.piholecontrol.presentation.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import eu.wedgess.piholecontrol.presentation.compose.ThemePreviewWithBackground
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun AlertMessageDialog(
    titleText: String,
    messageText: String,
    confirmText: String,
    dismissText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(titleText)
        },
        text = {
            Text(messageText)
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissText)
            }
        }
    )
}

@ThemePreviewWithBackground
@Composable
private fun AlertMessageDialogPreview() {
    PiHoleControlTheme {
        Surface {
            AlertMessageDialog(
                titleText = "Some title",
                messageText = "Are you sure message?",
                confirmText = "Confirm",
                dismissText = "Cancel",
                onDismiss = {},
                onConfirm = {}
            )
        }
    }
}
