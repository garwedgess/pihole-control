package eu.wedgess.piholecontrol.presentation.connections.list

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

interface ConnectionsContract {

    data class UiState(val connections: List<ConnectionEntity>) {
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
        data class DeleteConnection(val connection: ConnectionEntity) : Event
        data class SetActive(val connection: ConnectionEntity) : Event
    }
}