package eu.wedgess.piholecontrol.ui.app.view.components.dialog.components

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
import eu.wedgess.piholecontrol.ui.common.previews.ThemePreview
import eu.wedgess.piholecontrol.ui.theme.PiHoleControlTheme

@Composable
fun EnableStatusDialogContent(
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
                .padding(bottom = PiHoleControlTheme.dimens.padding.itemContentLarge),
            text = "Enable",
            style = MaterialTheme.typography.titleLarge
        )
        Text(text = "Enable ad-blocking?")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = PiHoleControlTheme.dimens.padding.itemContentXLarge),
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
private fun EnableStatusDialogContentPreview() {
    PiHoleControlTheme {
        Surface {
            EnableStatusDialogContent(onEnable = {}, onDismiss = {})
        }
    }
}