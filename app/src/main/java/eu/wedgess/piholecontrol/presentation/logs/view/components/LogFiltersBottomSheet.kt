package eu.wedgess.piholecontrol.presentation.logs.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(
                PiHoleControlTheme.dimens.padding.itemContentXSmall
            )
        ) {
            FilterSection(
                title = stringResource(R.string.logs_filter_title_basic_filter),
                expanded = uiState.showBasicFiltering,
                onExpandedStateChanged = {
                    onEvent(LogsContract.Event.OnToggleBasicFilteringOptions(it))
                }
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        PiHoleControlTheme.dimens.padding.itemContent
                    )
                ) {
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
            FilterSection(
                title = stringResource(R.string.logs_filter_title_advanced_filter),
                expanded = uiState.showAdvancedFiltering,
                onExpandedStateChanged = {
                    onEvent(LogsContract.Event.OnToggleAdvancedFilteringOptions(it))
                }
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        PiHoleControlTheme.dimens.padding.itemContent
                    )
                ) {
                    AnimatedVisibility(visible = uiState.availableClientIps.isNotEmpty()) {
                        DropdownTextField(
                            selectedValue = uiState.selectedClientIp,
                            options = uiState.availableClientIps,
                            label = stringResource(R.string.logs_advanced_filter_label_client_ip),
                            onValueChange = {
                                onEvent(LogsContract.Event.OnClientIpFilterChanged(it))
                            }
                        )
                    }

                    AnimatedVisibility(visible = uiState.availableClientNames.isNotEmpty()) {
                        DropdownTextField(
                            selectedValue = uiState.selectedClientName,
                            options = uiState.availableClientNames,
                            label = stringResource(R.string.logs_advanced_filter_label_client_name),
                            onValueChange = {
                                onEvent(LogsContract.Event.OnClientNameFilterChanged(it))
                            }
                        )
                    }

                    AnimatedVisibility(visible = uiState.availableQueryTypes.isNotEmpty()) {
                        DropdownTextField(
                            selectedValue = uiState.selectedQueryType,
                            options = uiState.availableQueryTypes,
                            label = stringResource(R.string.logs_advanced_filter_label_query_type),
                            onValueChange = {
                                onEvent(LogsContract.Event.OnQueryTypeFilterChanged(it))
                            }
                        )
                    }
                    AnimatedVisibility(visible = uiState.availableStatuses.isNotEmpty()) {
                        DropdownTextField(
                            selectedValue = uiState.selectedAdvancedStatus,
                            options = uiState.availableStatuses,
                            label = stringResource(R.string.logs_advanced_filter_label_advanced_status),
                            onValueChange = {
                                onEvent(LogsContract.Event.OnAdvancedStatusFilterChanged(it))
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { onEvent(LogsContract.Event.OnClearFiltersClick) }) {
                    Text(text = stringResource(R.string.filters_clear_btn))
                }
            }
        }
    }
}

@ThemePreview
@Composable
private fun LogFiltersBottomSheetPreview() {
    PiHoleControlTheme {
        LogFiltersBottomSheet(
            LogsContract.BottomSheetUiState.initial()
                .copy(
                    availableClientIps = listOf("192.168.50.2")
                ),
            onEvent = {}
        )
    }
}
