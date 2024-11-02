package eu.wedgess.piholecontrol.ui.settings.view.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.window.Dialog
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.ui.app.view.components.dialog.components.TimePickerType
import eu.wedgess.piholecontrol.ui.app.view.components.dialog.components.TimeTextField
import eu.wedgess.piholecontrol.ui.common.previews.ThemePreview
import eu.wedgess.piholecontrol.ui.theme.PiHoleControlTheme
import java.util.concurrent.TimeUnit

@Composable
fun RefreshIntervalDialog(
    currentRefreshTime: Long,
    onRefreshIntervalConfirmed: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    var currentSeconds by remember {
        mutableStateOf(
            TimeUnit.MILLISECONDS.toSeconds(currentRefreshTime).toString().run {
                TextFieldValue(
                    text = this,
                    selection = TextRange(this.length)
                )
            }
        )
    }
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(shape = RoundedCornerShape(PiHoleControlTheme.dimens.size.cornerRadius)) {
            Column(
                Modifier.padding(PiHoleControlTheme.dimens.padding.dialogContent),
                verticalArrangement = Arrangement.spacedBy(PiHoleControlTheme.dimens.padding.itemContent)
            ) {
                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
                    Text(
                        stringResource(R.string.refresh_interval_dialog_title),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.refresh_interval_dialog_label_seconds),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Normal
                    )
                    TimeTextField(
                        type = TimePickerType.Seconds,
                        value = currentSeconds,
                        onValueChange = {
                            currentSeconds = it
                        }
                    )
                }
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
                        Text(text = stringResource(R.string.all_btn_cancel))
                    }
                    TextButton(onClick = {
                        onRefreshIntervalConfirmed(TimeUnit.SECONDS.toMillis(currentSeconds.text.toLong()))
                    }) {
                        Text(text = stringResource(R.string.all_btn_confirm))
                    }
                }
            }
        }
    }
}

@ThemePreview
@Composable
private fun RefreshIntervalDialogPreview() {
    PiHoleControlTheme {
        RefreshIntervalDialog(
            currentRefreshTime = 10_000,
            onDismiss = {},
            onRefreshIntervalConfirmed = {}
        )
    }
}