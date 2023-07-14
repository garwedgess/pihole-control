package eu.wedgess.mihole.ui.logs.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import eu.wedgess.mihole.ui.base.UiResult
import eu.wedgess.mihole.ui.common.ErrorMessage
import eu.wedgess.mihole.ui.common.LoadingContent
import eu.wedgess.mihole.ui.logs.LogsContract
import eu.wedgess.mihole.ui.logs.view.components.LogFiltersBottomSheet
import eu.wedgess.mihole.ui.logs.view.components.LogsListContent
import eu.wedgess.mihole.ui.logs.view.components.dialogs.LogsDatePickerDialog
import eu.wedgess.mihole.ui.logs.view.components.dialogs.LogsTimePickerDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen(
    uiState: LogsContract.UiState,
    scaffoldState: BottomSheetScaffoldState,
    onEvent: (LogsContract.Event) -> Unit
) {

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = BottomSheetDefaults.SheetPeekHeight,
        sheetContent = {
            LogFiltersBottomSheet(uiState, onEvent)
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (uiState.logs) {
                    is UiResult.Loading -> LoadingContent(message = "Loading filters")
                    is UiResult.Error -> ErrorMessage(
                        errorMessage = uiState.logs.errorMessage,
                        onRetry = {})

                    is UiResult.Success -> {
                        LogsListContent(
                            logsList = uiState.logs.data,
                            uiState.searchState,
                            onEvent
                        )
                    }
                }
            }
            AnimatedVisibility(visible = uiState.showDatePicker != null) {
                LogsDatePickerDialog(
                    onDismiss = { onEvent(LogsContract.Event.OnDismissDatePicker) },
                    onConfirm = { dateMillis ->
                        onEvent(LogsContract.Event.OnDateConfirmed(dateMillis))
                    }
                )
            }
            AnimatedVisibility(visible = uiState.showTimePicker != null) {
                LogsTimePickerDialog(
                    onDismiss = { onEvent(LogsContract.Event.OnDismissDatePicker) },
                    onConfirm = { timeMillis ->
                        onEvent(LogsContract.Event.OnTimeConfirmed(timeMillis))
                    }
                )
            }
        }
    )
}