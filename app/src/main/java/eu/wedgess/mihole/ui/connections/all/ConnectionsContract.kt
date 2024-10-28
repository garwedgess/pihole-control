package eu.wedgess.mihole.ui.connections.all

import eu.wedgess.mihole.data.model.PiHoleInfo
import eu.wedgess.mihole.ui.base.UiResult
import eu.wedgess.mihole.ui.base.UnidirectionalViewModel
import eu.wedgess.mihole.utils.UiText

interface ConnectionsContract :
    UnidirectionalViewModel<ConnectionsContract.UiState, ConnectionsContract.Event, ConnectionsContract.Effect> {

    data class UiState(
        val connections: UiResult<List<PiHoleInfo>>
    ) {

        fun connections(connections: List<PiHoleInfo>): UiState =
            this.copy(connections = UiResult.Success(connections))

        fun connectionsError(errorMessage: UiText): UiState =
            this.copy(connections = UiResult.Error(errorMessage))

        companion object {
            fun initial() = UiState(
                connections = UiResult.Loading
            )
        }
    }

    sealed interface Effect {
        sealed interface Navigation : Effect {
            object Add : Navigation
            data class Edit(val id: Long) : Navigation
        }

    }

    sealed interface Event {
        object FetchConnections : Event
        object AddConnection : Event
        data class EditConnection(val connectionId: Long) : Event
        data class DeleteConnection(val connection: PiHoleInfo) : Event
        data class SetActive(val connection: PiHoleInfo) : Event
    }
}