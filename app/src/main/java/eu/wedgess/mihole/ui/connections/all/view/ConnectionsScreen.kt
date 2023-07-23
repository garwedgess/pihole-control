package eu.wedgess.mihole.ui.connections.all.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import eu.wedgess.mihole.ui.connections.all.ConnectionsContract
import eu.wedgess.mihole.ui.connections.all.view.components.ConnectionsContent

@Composable
fun ConnectionsScreen(
    uiState: ConnectionsContract.UiState,
    onEvent: (ConnectionsContract.Event) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(ConnectionsContract.Event.AddConnection) },
                content = {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "")
                })
        }
    ) {
        ConnectionsContent(connections = uiState.connections, paddingValues = it, onEvent)
    }


}