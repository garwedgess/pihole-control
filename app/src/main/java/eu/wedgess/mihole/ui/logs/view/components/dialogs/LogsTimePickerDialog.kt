package eu.wedgess.mihole.ui.logs.view.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import eu.wedgess.mihole.R
import eu.wedgess.mihole.ui.theme.MiHoleTheme
import org.threeten.bp.OffsetDateTime
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsTimePickerDialog(
    onConfirm: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val state = rememberTimePickerState()

    TimePickerDialog(
        onCancel = { onDismiss() },
        onConfirm = {
            val timeMillis = TimeUnit.HOURS.toSeconds(state.hour.toLong())
                .plus(TimeUnit.MINUTES.toSeconds(state.minute.toLong()))
                .minus(OffsetDateTime.now().offset.totalSeconds)
            onConfirm(timeMillis)
        },
    ) {
        TimePicker(state = state)
    }
}

@Composable
private fun TimePickerDialog(
    title: String = stringResource(R.string.logs_dialog_time_picker_title_select_time),
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) = Dialog(
    onDismissRequest = onCancel,
    properties = DialogProperties(
        usePlatformDefaultWidth = false
    ),
) {
    Surface(
        shape = MaterialTheme.shapes.extraLarge,
        tonalElevation = MiHoleTheme.dimens.size.dialogTonalElevation,
        modifier = Modifier
            .width(IntrinsicSize.Min)
            .height(IntrinsicSize.Min)
            .background(
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.surface
            ),
    ) {
        toggle()
        Column(
            modifier = Modifier.padding(MiHoleTheme.dimens.padding.itemContentXLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = MiHoleTheme.dimens.padding.screenContent),
                text = title,
                style = MaterialTheme.typography.labelMedium
            )
            content()
            Row(
                modifier = Modifier
                    .height(MiHoleTheme.dimens.size.timeDialogButtonRowHeight)
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(MiHoleTheme.dimens.weight.full))
                TextButton(
                    onClick = onCancel
                ) { Text("Cancel") }
                TextButton(
                    onClick = onConfirm
                ) { Text("OK") }
            }
        }
    }
}