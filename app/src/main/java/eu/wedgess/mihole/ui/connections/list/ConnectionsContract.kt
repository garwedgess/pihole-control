package eu.wedgess.mihole.ui.connections.list

import eu.wedgess.mihole.data.model.PiHoleInfo

interface ConnectionsContract {

    data class UiState(val connections: List<PiHoleInfo>) {
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
        data class DeleteConnection(val connection: PiHoleInfo) : Event
        data class SetActive(val connection: PiHoleInfo) : Event
    }
}