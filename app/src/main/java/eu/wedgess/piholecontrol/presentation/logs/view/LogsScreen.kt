package eu.wedgess.piholecontrol.presentation.logs.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import eu.wedgess.piholecontrol.presentation.compose.Compose
import eu.wedgess.piholecontrol.presentation.compose.EmptyScreen
import eu.wedgess.piholecontrol.presentation.compose.ErrorScreen
import eu.wedgess.piholecontrol.presentation.compose.LoadingScreen
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.logs.LogsContract
import eu.wedgess.piholecontrol.presentation.logs.view.components.LogFiltersBottomSheet
import eu.wedgess.piholecontrol.presentation.logs.view.components.LogsListContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen(
    uiResult: UIResult<LogsContract.UiState>,
    bottomSheetUiState: LogsContract.BottomSheetUiState,
    scaffoldState: BottomSheetScaffoldState,
    snackbarHostState: SnackbarHostState,
    onEvent: (LogsContract.Event) -> Unit
) {
    BottomSheetScaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        scaffoldState = scaffoldState,
        sheetPeekHeight = BottomSheetDefaults.SheetPeekHeight,
        sheetContent = {
            LogFiltersBottomSheet(bottomSheetUiState, onEvent)
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                uiResult.Compose(
                    onLoading = { LoadingScreen(modifier = Modifier.fillMaxSize(), it) },
                    onLoaded = {
                        LogsListContent(
                            logsList = it.logs,
                            dialogType = it.dialogType,
                            onEvent = onEvent
                        )
                    },
                    onEmpty = { EmptyScreen(modifier = Modifier.fillMaxSize(), it) },
                    onError = { ErrorScreen(modifier = Modifier.fillMaxSize(), it) }
                )
            }
        }
    )
}
