package eu.wedgess.piholecontrol.presentation.diagnosis.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.wedgess.piholecontrol.presentation.compose.Compose
import eu.wedgess.piholecontrol.presentation.compose.EmptyScreen
import eu.wedgess.piholecontrol.presentation.compose.ErrorScreen
import eu.wedgess.piholecontrol.presentation.compose.LoadingScreen
import eu.wedgess.piholecontrol.presentation.diagnosis.DiagnosisContract
import eu.wedgess.piholecontrol.presentation.diagnosis.view.components.DiagnosisDialogs
import eu.wedgess.piholecontrol.presentation.diagnosis.view.components.DiagnosisListContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosisScreen(
    uiState: DiagnosisContract.UiState,
    onEvent: (DiagnosisContract.Event) -> Unit
) {
    Scaffold { padding ->
        PullToRefreshBox(
            modifier = Modifier.padding(padding),
            isRefreshing = uiState.isRefreshing,
            onRefresh = { onEvent(DiagnosisContract.Event.OnRefresh) }
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                uiState.messagesResult.Compose(
                    onLoading = { LoadingScreen(modifier = Modifier.fillMaxSize(), it) },
                    onEmpty = { EmptyScreen(modifier = Modifier.fillMaxSize(), it) },
                    onError = { ErrorScreen(modifier = Modifier.fillMaxSize(), it) },
                    onLoaded = {
                        DiagnosisListContent(
                            messages = it,
                            selectionMode = uiState.selectionMode,
                            onEvent = onEvent
                        )
                    }
                )
            }
        }
    }

    DiagnosisDialogs(
        dialogType = uiState.dialogType,
        onDismissMessageClick = {
            onEvent(DiagnosisContract.Event.OnDismissDiagnosisMessageClick(it))
        },
        onDismissMessageConfirmClick = {
            onEvent(DiagnosisContract.Event.OnDismissDiagnosisMessageConfirmed)
        },
        onDismissSelectedMessagesConfirmClick = {
            onEvent(DiagnosisContract.Event.OnDismissSelectedDiagnosisMessagesConfirmed)
        },
        onDismissDialogClick = {
            onEvent(DiagnosisContract.Event.OnDismissDialog)
        }
    )
}
