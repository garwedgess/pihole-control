package eu.wedgess.piholecontrol.presentation.logs.view.components.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import eu.wedgess.piholecontrol.domain.model.PiHoleLogsEntity
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.logs.LogsContract
import eu.wedgess.piholecontrol.presentation.logs.model.LogEntryInfo
import eu.wedgess.piholecontrol.presentation.logs.model.LogsDialogType
import eu.wedgess.piholecontrol.presentation.logs.model.PickerType
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun LogsDialogs(dialogType: LogsDialogType, onEvent: (LogsContract.Event) -> Unit) {
    when (dialogType) {
        LogsDialogType.None -> Unit
        is LogsDialogType.ShowDatePickerDialog -> LogsDatePickerDialog(
            onDismiss = {
                onEvent(
                    LogsContract.Event.OnDismissDialog
                )
            },
            onConfirm = { onEvent(LogsContract.Event.OnDateConfirmed(dialogType.pickerType, it)) }
        )

        is LogsDialogType.ShowTimePickerDialog -> LogsTimePickerDialog(
            onDismiss = {
                onEvent(
                    LogsContract.Event.OnDismissDialog
                )
            },
            onConfirmTime = { hours, minutes ->
                onEvent(
                    LogsContract.Event.OnTimeConfirmed(
                        dialogType.pickerType, dialogType.currentDate.atTime(hours, minutes)
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

@ThemePreview
@Composable
private fun LogsDialogsPreview(
    @PreviewParameter(LogsDialogsPreviewParameters::class) dialogType: LogsDialogType
) {
    PiHoleControlTheme {
        LogsDialogs(dialogType, onEvent = {})
    }
}

private class LogsDialogsPreviewParameters : PreviewParameterProvider<LogsDialogType> {
    override val values: Sequence<LogsDialogType>
        get() = sequenceOf(
            LogsDialogType.ShowDetailsDialog(
                details = LogEntryInfo.Version5(
                    timestamp = System.currentTimeMillis().div(1000L),
                    time = "10:12",
                    queryType = "IPv4",
                    domain = "www.google.com",
                    client = "My Android",
                    answerType = PiHoleLogsEntity.LogsAnswerTypeEntity.LOCAL_CACHE,
                    replyTime = 1.2
                )
            ),
            LogsDialogType.ShowDatePickerDialog(pickerType = PickerType.ToTime),
        )
}
