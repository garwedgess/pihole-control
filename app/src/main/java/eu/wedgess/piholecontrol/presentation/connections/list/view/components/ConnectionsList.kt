package eu.wedgess.piholecontrol.presentation.connections.list.view.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.presentation.common.SwipeToDeleteItem
import eu.wedgess.piholecontrol.presentation.connections.list.ConnectionsContract

@Composable
fun ConnectionsList(
    connections: List<ConnectionEntity>,
    onEvent: (ConnectionsContract.Event) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        state = rememberLazyListState(),
        content = {
            items(connections, key = { it.id }) {
                SwipeToDeleteItem(
                    modifier = Modifier.animateItem(),
                    onDelete = {
                        onEvent(
                            ConnectionsContract.Event.DeleteConnection(
                                id = it.id,
                                name = it.name
                            )
                        )
                    },
                    content = {
                        ConnectionListItem(
                            connectionInfo = it,
                            onEditClick = {
                                onEvent(ConnectionsContract.Event.EditConnection(it.id))
                            },
                            onDeleteClick = {
                                onEvent(
                                    ConnectionsContract.Event.DeleteConnection(
                                        id = it.id,
                                        name = it.name
                                    )
                                )
                            },
                            onSetActiveClick = {
                                onEvent(
                                    ConnectionsContract.Event.SetActive(
                                        id = it.id,
                                        name = it.name
                                    )
                                )
                            }
                        )
                    }
                )
            }
        }
    )
}
