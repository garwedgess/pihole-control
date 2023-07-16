package eu.wedgess.mihole.ui.logs.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.wedgess.mihole.R
import eu.wedgess.mihole.ui.logs.LogsContract
import eu.wedgess.mihole.ui.logs.model.LogEntryStatus
import eu.wedgess.mihole.ui.logs.model.PickerType
import eu.wedgess.mihole.ui.theme.MiHoleTheme

@Composable
fun LogFiltersBottomSheet(
    uiState: LogsContract.UiState,
    onEvent: (LogsContract.Event) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(MiHoleTheme.dimens.size.logsBottomSheetHeight(LocalConfiguration.current.screenHeightDp.dp))
            .padding(MiHoleTheme.dimens.padding.itemContent)
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            Text(
                text = stringResource(R.string.logs_filter_sheet_title_number_of_queries),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = MiHoleTheme.dimens.padding.itemContent,
                        end = MiHoleTheme.dimens.padding.itemContent,
                        bottom = MiHoleTheme.dimens.padding.itemContent
                    )
            )
            LogsLimitRadioButtonGroup(
                modifier = Modifier.fillMaxWidth(),
                itemsList = LogsContract.UiState.availableLogLimits,
                selectedItem = uiState.logsLimit,
                onLogLimitSelected = { onEvent(LogsContract.Event.OnLogLimitChanged(it)) }
            )
            Divider(modifier = Modifier.padding(vertical = MiHoleTheme.dimens.padding.itemContentLarge))
            Text(
                text = stringResource(R.string.logs_filter_sheet_title_status), modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = MiHoleTheme.dimens.padding.itemContent,
                        end = MiHoleTheme.dimens.padding.itemContent,
                        bottom = MiHoleTheme.dimens.padding.itemContent
                    )
            )
            LogsEntryRadioButtonGroup(
                modifier = Modifier.fillMaxWidth(),
                itemsList = LogEntryStatus.values(),
                selectedItem = uiState.selectedLogEntryStatus,
                onLogEntryStatusSelected = { onEvent(LogsContract.Event.OnStatusChanged(it)) }
            )
            Divider(modifier = Modifier.padding(vertical = MiHoleTheme.dimens.padding.itemContentLarge))
            Text(
                text = stringResource(R.string.logs_filter_sheet_title_time), modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = MiHoleTheme.dimens.padding.itemContent,
                        end = MiHoleTheme.dimens.padding.itemContent,
                        bottom = MiHoleTheme.dimens.padding.itemContent
                    )
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