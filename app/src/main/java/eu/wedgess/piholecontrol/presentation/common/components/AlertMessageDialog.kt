package eu.wedgess.piholecontrol.presentation.common.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import eu.wedgess.piholecontrol.presentation.compose.ThemePreviewWithBackground
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun AlertMessageDialog(
    titleText: String,
    messageText: String,
    dismissText: String,
    onDismiss: () -> Unit,
    confirmText: String? = null,
    onConfirm: (() -> Unit)? = null
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
            if (onConfirm != null && confirmText != null) {
                TextButton(onClick = onConfirm) {
                    Text(confirmText)
                }
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
private fun AlertMessageDialogPreview(
    @PreviewParameter(AlertMessageDialogPreviewParams::class) params: AlertMessageDialogParams
) {
    PiHoleControlTheme {
        Surface {
            AlertMessageDialog(
                titleText = params.titleText,
                messageText = params.messageText,
                confirmText = params.confirmText,
                dismissText = params.dismissText,
                onDismiss = params.onDismiss,
                onConfirm = params.onConfirm
            )
        }
    }
}

private data class AlertMessageDialogParams(
    val titleText: String = "Some title",
    val messageText: String = "Some random message",
    val confirmText: String? = null,
    val dismissText: String = "Cancel",
    val onDismiss: () -> Unit = {},
    val onConfirm: (() -> Unit)? = null
)

private class AlertMessageDialogPreviewParams : PreviewParameterProvider<AlertMessageDialogParams> {
    override val values: Sequence<AlertMessageDialogParams>
        get() = sequenceOf(
            AlertMessageDialogParams(),
            AlertMessageDialogParams(confirmText = "Ok", onConfirm = {})
        )
}
