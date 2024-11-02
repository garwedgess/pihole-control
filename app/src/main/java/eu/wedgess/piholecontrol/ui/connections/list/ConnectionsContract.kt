package eu.wedgess.piholecontrol.ui.connections.list

import eu.wedgess.piholecontrol.data.model.ConnectionInfo

interface ConnectionsContract {

    data class UiState(val connections: List<ConnectionInfo>) {
        companion object {
            fun initial() = UiState(
                connections = emptyList()
            )
        }
    }

    sealed interface Effect {
        sealed interface Navigation : Effect {
            data object Add : Navigation
            data class Edit(val id: Long) : Navigation
        }

    }

    sealed interface Event {
        data object AddConnection : Event
        data class EditConnection(val connectionId: Long) : Event
        data class DeleteConnection(val connection: ConnectionInfo) : Event
        data class SetActive(val connection: ConnectionInfo) : Event
    }
}