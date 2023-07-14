package eu.wedgess.mihole.ui.logs.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.wedgess.mihole.ui.base.UiResult
import eu.wedgess.mihole.ui.logs.LogsContract
import eu.wedgess.mihole.ui.logs.model.LogEntryStatus
import eu.wedgess.mihole.ui.logs.model.PickerType

@Composable
fun LogFiltersBottomSheet(
    uiState: LogsContract.UiState,
    onEvent: (LogsContract.Event) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                text = "Total Results: ${(uiState.logs as? UiResult.Success)?.data?.size ?: 0}",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge
            )
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            Text(
                text = "Number of Queries", modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            )
            LogsLimitRadioButtonGroup(
                modifier = Modifier.fillMaxWidth(),
                itemsList = LogsContract.UiState.availableLogLimits,
                selectedItem = uiState.logsLimit,
                onLogLimitSelected = { onEvent(LogsContract.Event.OnLogLimitChanged(it)) }
            )
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            Text(
                text = "Status", modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            )
            LogsEntryRadioButtonGroup(
                modifier = Modifier.fillMaxWidth(),
                itemsList = LogEntryStatus.values(),
                selectedItem = uiState.selectedLogEntryStatus,
                onLogEntryStatusSelected = { onEvent(LogsContract.Event.OnStatusChanged(it)) }
            )
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            Text(
                text = "Time", modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            )
            TimePickerLayout(
                fromTime = uiState.filterFromTime,
                toTime = uiState.filterToTime,
                onFromTimeClicked = {
                    onEvent(LogsContract.Event.OnShowDatePicker(PickerType.FromTime))
                },
                onToTimeClicked = {
                    onEvent(LogsContract.Event.OnShowDatePicker(PickerType.ToTime))
                },
                onFromTimeCleared = {
                    onEvent(LogsContract.Event.OnFromTimeCleared)
                },
                onToTimeCleared = {
                    onEvent(LogsContract.Event.OnToTimeCleared)
                }
            )
        }
    }
}