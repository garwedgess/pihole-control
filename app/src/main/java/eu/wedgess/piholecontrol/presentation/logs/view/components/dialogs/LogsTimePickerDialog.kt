package eu.wedgess.piholecontrol.presentation.logs.view.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jakewharton.threetenabp.AndroidThreeTen
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import org.threeten.bp.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsTimePickerDialog(
    onConfirmTime: (Int, Int) -> Unit,
    onDismiss: () -> Unit,
    initialMinutes: Int = LocalDateTime.now().minute,
    initialHour: Int = LocalDateTime.now().hour,
) {
    val state = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinutes,
        is24Hour = true
    )
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    PiHoleControlTheme.dimens.padding.itemContent
                )
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colorScheme.onSurface
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                bottom = PiHoleControlTheme.dimens.padding.screenContent
                            ),
                        text = stringResource(R.string.logs_dialog_time_picker_title_select_time),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                TimePicker(state = state)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = PiHoleControlTheme.dimens.padding.dialogContent),
                    horizontalArrangement = Arrangement.spacedBy(
                        PiHoleControlTheme.dimens.padding.itemContent,
                        Alignment.End
                    )
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(text = stringResource(R.string.all_btn_cancel))
                    }

                    TextButton(
                        onClick = {
                            onConfirmTime(state.hour, state.minute)
                        }
                    ) {
                        Text(text = stringResource(R.string.all_btn_confirm))
                    }
                }
            }
        }
    }
}

@ThemePreview
@Composable
private fun LogsTimePickerDialogPreview() {
    AndroidThreeTen.init(LocalContext.current)
    PiHoleControlTheme {
        Surface {
            LogsTimePickerDialog(
                onDismiss = {},
                onConfirmTime = { _, _ -> }
            )
        }
    }
}
