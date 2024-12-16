package eu.wedgess.piholecontrol.presentation.connections.list.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.wedgess.piholecontrol.presentation.compose.Compose
import eu.wedgess.piholecontrol.presentation.compose.EmptyScreen
import eu.wedgess.piholecontrol.presentation.compose.ErrorScreen
import eu.wedgess.piholecontrol.presentation.compose.LoadingScreen
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.connections.list.ConnectionsContract
import eu.wedgess.piholecontrol.presentation.connections.list.view.components.ConnectionsList

@Composable
fun ConnectionsScreen(
    uiResult: UIResult<ConnectionsContract.UiState>,
    onEvent: (ConnectionsContract.Event) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(ConnectionsContract.Event.AddConnection) },
                content = {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "")
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            uiResult.Compose(
                onLoading = { LoadingScreen(modifier = Modifier.fillMaxSize(), it) },
                onEmpty = { EmptyScreen(modifier = Modifier.fillMaxSize(), it) },
                onError = { ErrorScreen(modifier = Modifier.fillMaxSize(), it) },
                onLoaded = { ConnectionsList(connections = it.connections, onEvent) }
            )
        }
    }
}
