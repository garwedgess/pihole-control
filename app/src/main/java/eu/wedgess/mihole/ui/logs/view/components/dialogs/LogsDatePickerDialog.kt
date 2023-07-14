package eu.wedgess.mihole.ui.logs.view.components.dialogs

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import eu.wedgess.mihole.utils.extensions.epochMillisToCurrentTimezoneEpochSeconds
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsDatePickerDialog(onDismiss: () -> Unit, onConfirm: (date: Long) -> Unit) {
    val datePickerState = rememberDatePickerState()
    val confirmEnabled = remember {
        derivedStateOf { datePickerState.selectedDateMillis != null }
    }

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                enabled = confirmEnabled.value,
                onClick = {
                    onConfirm(requireNotNull(datePickerState.selectedDateMillis?.epochMillisToCurrentTimezoneEpochSeconds()))
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("CANCEL")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            dateValidator = {
                it in (System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2))..System.currentTimeMillis()
            }
        )
    }
}