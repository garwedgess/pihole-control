package eu.wedgess.mihole.ui.logs.view.components.dialogs

import androidx.compose.runtime.Composable
import eu.wedgess.mihole.ui.logs.LogsContract
import eu.wedgess.mihole.ui.logs.model.LogsDialogType

@Composable
fun LogsDialogs(dialogType: LogsDialogType, onEvent: (LogsContract.Event) -> Unit) {
    when (dialogType) {
        LogsDialogType.None -> Unit
        is LogsDialogType.ShowDatePickerDialog -> LogsDatePickerDialog(
            onDismiss = { onEvent(LogsContract.Event.OnDismissDialog) },
            onConfirm = { onEvent(LogsContract.Event.OnDateConfirmed(dialogType.pickerType, it)) }
        )

        is LogsDialogType.ShowTimePickerDialog -> LogsTimePickerDialog(
            onDismiss = { onEvent(LogsContract.Event.OnDismissDialog) },
            onTimeConfirmed = { hours, minutes ->
                onEvent(
                    LogsContract.Event.OnTimeConfirmed(
                        dialogType.pickerType,
                        dialogType.currentDate.atTime(hours, minutes)
                    )
                )
            }
        )

        is LogsDialogType.ShowDetailsDialog -> LogDetailsDialog(
            piHoleLog = dialogType.details,
            addToAllowList = { onEvent(LogsContract.Event.AddToAllowList(it)) },
            addToBlockList = { onEvent(LogsContract.Event.AddToBlockList(it)) },
            onDismiss = { onEvent(LogsContract.Event.OnDismissDialog) }
        )
    }
}