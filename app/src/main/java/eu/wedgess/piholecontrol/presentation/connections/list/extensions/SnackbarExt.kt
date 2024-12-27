package eu.wedgess.piholecontrol.presentation.connections.list.extensions

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.connections.list.ConnectionsContract
import eu.wedgess.piholecontrol.utils.UiText

suspend fun ConnectionsContract.Effect.Snackbar.handle(
    snackbarHostState: SnackbarHostState,
    context: Context,
    onEvent: (ConnectionsContract.Event) -> Unit
) {
    suspend fun SnackbarHostState.showSnackbar(
        message: UiText,
        actionLabel: Int? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration = SnackbarDuration.Indefinite,
        onDismissed: (() -> Unit)? = null,
        onActionPerformed: (() -> Unit)? = null
    ) {
        val result = this.showSnackbar(
            message = message.asString(context),
            actionLabel = actionLabel?.let { context.getString(it) },
            withDismissAction = withDismissAction,
            duration = duration
        )
        when (result) {
            SnackbarResult.Dismissed -> onDismissed?.invoke()
            SnackbarResult.ActionPerformed -> onActionPerformed?.invoke()
        }
    }

    when (this) {
        is ConnectionsContract.Effect.Snackbar.DeleteConnectionFailed -> {
            snackbarHostState.showSnackbar(message)
        }

        is ConnectionsContract.Effect.Snackbar.DeleteConnection -> {
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = R.string.all_btn_undo,
                duration = SnackbarDuration.Long,
                onDismissed = {
                    onEvent(ConnectionsContract.Event.CompleteDeleteConnection(name))
                },
                onActionPerformed = {
                    onEvent(
                        ConnectionsContract.Event.UndoDeleteConnection(
                            id = id,
                            name = name
                        )
                    )
                }
            )
        }

        is ConnectionsContract.Effect.Snackbar.RestoreConnectionFailed -> {
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = R.string.all_btn_retry,
                withDismissAction = true,
                onActionPerformed = {
                    onEvent(
                        ConnectionsContract.Event.UndoDeleteConnection(
                            id = id,
                            name = name
                        )
                    )
                }
            )
        }

        is ConnectionsContract.Effect.Snackbar.SetActiveConnectionFailed -> {
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = R.string.all_btn_retry,
                withDismissAction = true,
                onActionPerformed = {
                    onEvent(
                        ConnectionsContract.Event.SetActive(
                            id = id,
                            name = name
                        )
                    )
                }
            )
        }
    }
}
