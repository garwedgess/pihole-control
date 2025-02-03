package eu.wedgess.piholecontrol.presentation.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun LoadingDialog(message: String, onDismiss: () -> Unit) {
    Dialog(
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surfaceContainerHigh
        ) {
            Column(
                modifier = Modifier.padding(PiHoleControlTheme.dimens.padding.dialogContent),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(PiHoleControlTheme.dimens.padding.itemContent)
            ) {
                CircularProgressIndicator()
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@ThemePreview
@Composable
private fun LoadingDialogPreview() {
    PiHoleControlTheme {
        LoadingDialog(
            message = "Loading content...",
            onDismiss = {}
        )
    }
}
