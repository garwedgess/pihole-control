package eu.wedgess.piholecontrol.presentation.connections.list

import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.utils.UiText

interface ConnectionsContract {

    data class UiState(val connections: List<ConnectionEntity>) {
        companion object {
            fun initial() = UiState(
                connections = emptyList()
            )
        }
    }

    sealed interface Effect {
        sealed interface Snackbar : Effect {
            data class SetActiveConnectionFailed(
                val id: Long,
                val name: String,
                val message: UiText = UiText.StringResourceWithArgs(
                    id = R.string.snackbar_msg_failed_to_set_active_connection,
                    name
                )
            ) : Snackbar
            data class RestoreConnectionFailed(
                val id: Long,
                val name: String,
                val message: UiText = UiText.StringResourceWithArgs(
                    id = R.string.snackbar_msg_failed_connection_restore,
                    name
                )
            ) : Snackbar
            data class DeleteConnectionFailed(
                val name: String,
                val message: UiText = UiText.StringResourceWithArgs(
                    id = R.string.snackbar_msg_failed_to_delete_connection,
                    name
                )
            ) : Snackbar

            data class DeleteConnection(
                val id: Long,
                val name: String,
                val message: UiText = UiText.StringResourceWithArgs(
                    id = R.string.snackbar_msg_connection_deleted,
                    name
                )
            ) : Snackbar
        }

        sealed interface Navigation : Effect {
            data object Add : Navigation
            data class Edit(val id: Long) : Navigation
        }
    }

    sealed interface Event {
        data object AddConnection : Event
        data class EditConnection(val id: Long) : Event
        data class DeleteConnection(val id: Long, val name: String) : Event
        data class UndoDeleteConnection(val id: Long, val name: String) : Event
        data class CompleteDeleteConnection(val name: String) : Event
        data class SetActive(val id: Long, val name: String) : Event
    }
}
