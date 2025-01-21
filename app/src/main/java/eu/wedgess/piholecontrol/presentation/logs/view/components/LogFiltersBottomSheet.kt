package eu.wedgess.piholecontrol.presentation.logs.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.logs.LogsContract
import eu.wedgess.piholecontrol.presentation.logs.model.LogEntryStatus
import eu.wedgess.piholecontrol.presentation.logs.model.PickerType
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun LogFiltersBottomSheet(
    uiState: LogsContract.BottomSheetUiState,
    onEvent: (LogsContract.Event) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .height(
                PiHoleControlTheme.dimens.size.logsBottomSheetHeight(
                    LocalConfiguration.current.screenHeightDp.dp
                )
            )
            .padding(PiHoleControlTheme.dimens.padding.itemContent),
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(
                PiHoleControlTheme.dimens.padding.itemContent
            ),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Live Logging")
                Checkbox(
                    checked = uiState.liveLogging,
                    onCheckedChange = { onEvent(LogsContract.Event.OnLiveLoggingChanged(it)) }
                )
            }
            Text(text = stringResource(R.string.logs_filter_sheet_title_number_of_queries))
            LogsLimitRadioButtonGroup(
                modifier = Modifier.fillMaxWidth(),
                itemsList = LogsContract.BottomSheetUiState.availableLogLimits,
                selectedItem = uiState.logsLimit,
                onLogLimitClick = { onEvent(LogsContract.Event.OnLogLimitChanged(it)) }
            )
            HorizontalDivider()
            Text(text = stringResource(R.string.logs_filter_sheet_title_status))
            LogsEntryRadioButtonGroup(
                modifier = Modifier.fillMaxWidth(),
                itemsList = LogEntryStatus.entries.toTypedArray(),
                selectedItem = uiState.selectedLogEntryStatus,
                onLogEntryStatusClick = { onEvent(LogsContract.Event.OnStatusChanged(it)) }
            )
            HorizontalDivider()
            Text(text = stringResource(R.string.logs_filter_sheet_title_time))
            TimePickerLayout(
                fromTime = uiState.filterFromTime,
                toTime = uiState.filterToTime,
                onFromTimeClick = {
                    onEvent(LogsContract.Event.OnShowDatePicker(PickerType.FromTime))
                },
                onToTimeClick = {
                    onEvent(LogsContract.Event.OnShowDatePicker(PickerType.ToTime))
                },
                onClearFromTime = {
                    onEvent(LogsContract.Event.OnFromTimeCleared)
                },
                onClearToTime = {
                    onEvent(LogsContract.Event.OnToTimeCleared)
                }
            )
        }
    }
}

@ThemePreview
@Composable
private fun LogFiltersBottomSheetPreview() {
    PiHoleControlTheme {
        LogFiltersBottomSheet(LogsContract.BottomSheetUiState.initial(), onEvent = {})
    }
}
