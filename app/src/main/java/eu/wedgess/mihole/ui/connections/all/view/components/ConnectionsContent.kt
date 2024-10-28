package eu.wedgess.mihole.ui.connections.all.view.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.wedgess.mihole.data.model.PiHoleInfo
import eu.wedgess.mihole.ui.base.UiResult
import eu.wedgess.mihole.ui.common.ErrorMessage
import eu.wedgess.mihole.ui.common.LoadingContent
import eu.wedgess.mihole.ui.connections.all.ConnectionsContract

@Composable
fun ConnectionsContent(
    connections: UiResult<List<PiHoleInfo>>,
    paddingValues: PaddingValues,
    onEvent: (ConnectionsContract.Event) -> Unit
) {
    when (connections) {
        is UiResult.Loading -> LoadingContent(
            message = "Loading connections",
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )

        is UiResult.Error -> ErrorMessage(
            errorMessage = connections.errorMessage.asString(),
            onRetry = {})

        is UiResult.Success -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                state = rememberLazyListState(),
                content = {
                    items(connections.data) {
                        ConnectionListItem(
                            miHolesInfo = it,
                            onEditClicked = { onEvent(ConnectionsContract.Event.EditConnection(it.id)) },
                            onDeleteClicked = { onEvent(ConnectionsContract.Event.DeleteConnection(it)) },
                            onSetActiveClicked = { onEvent(ConnectionsContract.Event.SetActive(it)) }
                        )
                    }
                }
            )
        }
    }
}