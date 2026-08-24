package eu.wedgess.piholecontrol.presentation.localdns.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.wedgess.piholecontrol.presentation.compose.Compose
import eu.wedgess.piholecontrol.presentation.compose.EmptyScreen
import eu.wedgess.piholecontrol.presentation.compose.ErrorScreen
import eu.wedgess.piholecontrol.presentation.compose.LoadingScreen
import eu.wedgess.piholecontrol.presentation.localdns.LocalDnsContract
import eu.wedgess.piholecontrol.presentation.localdns.view.components.LocalDnsDialogs
import eu.wedgess.piholecontrol.presentation.localdns.view.components.LocalDnsListContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalDnsScreen(
    uiState: LocalDnsContract.UiState,
    onEvent: (LocalDnsContract.Event) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(LocalDnsContract.Event.OnAddLocalDnsRecordClick) }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { padding ->
        PullToRefreshBox(
            modifier = Modifier.padding(padding),
            isRefreshing = uiState.isRefreshing,
            onRefresh = { onEvent(LocalDnsContract.Event.OnRefresh) }
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                uiState.filteredRecords.Compose(
                    onLoading = { LoadingScreen(modifier = Modifier.fillMaxSize(), it) },
                    onEmpty = { EmptyScreen(modifier = Modifier.fillMaxSize(), it) },
                    onError = { ErrorScreen(modifier = Modifier.fillMaxSize(), it) },
                    onLoaded = {
                        LocalDnsListContent(
                            records = it,
                            onEvent = onEvent
                        )
                    }
                )
            }
        }
    }

    LocalDnsDialogs(
        dialogType = uiState.dialogType,
        onIpAddressChange = {
            onEvent(LocalDnsContract.Event.OnLocalDnsRecordIpAddressChanged(it))
        },
        onDomainChange = {
            onEvent(LocalDnsContract.Event.OnLocalDnsRecordDomainChanged(it))
        },
        onAddClick = { onEvent(LocalDnsContract.Event.OnAddLocalDnsRecordConfirmed) },
        onUpdateClick = { onEvent(LocalDnsContract.Event.OnUpdateLocalDnsRecordConfirmed) },
        onEditClick = { onEvent(LocalDnsContract.Event.OnEditLocalDnsRecordClick(it)) },
        onDeleteClick = { onEvent(LocalDnsContract.Event.OnDeleteLocalDnsRecordClick(it)) },
        onDeleteConfirmClick = { onEvent(LocalDnsContract.Event.OnDeleteLocalDnsRecordConfirmed) },
        onDismissDialogClick = { onEvent(LocalDnsContract.Event.OnDismissDialog) }
    )
}
