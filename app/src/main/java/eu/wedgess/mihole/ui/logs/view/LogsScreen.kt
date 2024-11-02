package eu.wedgess.mihole.ui.logs.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import eu.wedgess.mihole.ui.compose.Compose
import eu.wedgess.mihole.ui.compose.EmptyScreen
import eu.wedgess.mihole.ui.compose.ErrorScreen
import eu.wedgess.mihole.ui.compose.LoadingScreen
import eu.wedgess.mihole.ui.compose.UIResult
import eu.wedgess.mihole.ui.logs.LogsContract
import eu.wedgess.mihole.ui.logs.view.components.LogFiltersBottomSheet
import eu.wedgess.mihole.ui.logs.view.components.LogsListContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen(
    uiResult: UIResult<LogsContract.UiState>,
    scaffoldState: BottomSheetScaffoldState,
    snackbarHostState: SnackbarHostState,
    onEvent: (LogsContract.Event) -> Unit
) {

    BottomSheetScaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        scaffoldState = scaffoldState,
        sheetPeekHeight = BottomSheetDefaults.SheetPeekHeight,
        sheetContent = {
            (uiResult as? UIResult.Loaded)?.run {
                LogFiltersBottomSheet(this@run.data, onEvent)
            }
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
                    onLoaded = { LogsListContent(logsList = it.logs, dialogType = it.dialogType, onEvent = onEvent) },
                    onEmpty = { EmptyScreen(modifier = Modifier.fillMaxSize(), it) },
                    onError = { ErrorScreen(modifier = Modifier.fillMaxSize(), it) }
                )
            }
        }
    )
}