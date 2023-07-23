package eu.wedgess.mihole.ui.connections.all

import eu.wedgess.mihole.data.model.MiHolesInfo
import eu.wedgess.mihole.ui.base.UiResult
import eu.wedgess.mihole.ui.base.UnidirectionalViewModel
import eu.wedgess.mihole.utils.UiText

interface ConnectionsContract :
    UnidirectionalViewModel<ConnectionsContract.UiState, ConnectionsContract.Event, ConnectionsContract.Effect> {

    data class UiState(
        val connections: UiResult<List<MiHolesInfo>>
    ) {

        fun connections(connections: List<MiHolesInfo>): UiState =
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
        data class DeleteConnection(val connection: MiHolesInfo) : Event
        data class SetActive(val connection: MiHolesInfo) : Event
    }
}