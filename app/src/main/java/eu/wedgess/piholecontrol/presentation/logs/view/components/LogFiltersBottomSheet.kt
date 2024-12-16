package eu.wedgess.piholecontrol.presentation.logs.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.wedgess.piholecontrol.R
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
            .height(PiHoleControlTheme.dimens.size.logsBottomSheetHeight(LocalConfiguration.current.screenHeightDp.dp))
            .padding(PiHoleControlTheme.dimens.padding.itemContent)
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            Text(
                text = stringResource(R.string.logs_filter_sheet_title_number_of_queries),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = PiHoleControlTheme.dimens.padding.itemContent,
                        end = PiHoleControlTheme.dimens.padding.itemContent,
                        bottom = PiHoleControlTheme.dimens.padding.itemContent
                    )
            )
            LogsLimitRadioButtonGroup(
                modifier = Modifier.fillMaxWidth(),
                itemsList = LogsContract.BottomSheetUiState.availableLogLimits,
                selectedItem = uiState.logsLimit,
                onLogLimitClick = { onEvent(LogsContract.Event.OnLogLimitChanged(it)) }
            )
            HorizontalDivider(
                modifier = Modifier.padding(
                    vertical = PiHoleControlTheme.dimens.padding.itemContentLarge
                )
            )
            Text(
                text = stringResource(R.string.logs_filter_sheet_title_status),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = PiHoleControlTheme.dimens.padding.itemContent,
                        end = PiHoleControlTheme.dimens.padding.itemContent,
                        bottom = PiHoleControlTheme.dimens.padding.itemContent
                    )
            )
            LogsEntryRadioButtonGroup(
                modifier = Modifier.fillMaxWidth(),
                itemsList = LogEntryStatus.entries.toTypedArray(),
                selectedItem = uiState.selectedLogEntryStatus,
                onLogEntryStatusClick = { onEvent(LogsContract.Event.OnStatusChanged(it)) }
            )
            HorizontalDivider(
                modifier = Modifier.padding(
                    vertical = PiHoleControlTheme.dimens.padding.itemContentLarge
                )
            )
            Text(
                text = stringResource(R.string.logs_filter_sheet_title_time),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = PiHoleControlTheme.dimens.padding.itemContent,
                        end = PiHoleControlTheme.dimens.padding.itemContent,
                        bottom = PiHoleControlTheme.dimens.padding.itemContent
                    )
            )
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
