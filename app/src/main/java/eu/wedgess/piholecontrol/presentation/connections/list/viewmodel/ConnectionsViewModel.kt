package eu.wedgess.piholecontrol.presentation.connections.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.usecases.connections.DeleteConnectionMarkedForDeletionUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.FetchAllConnectionsUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.MarkConnectionForDeletionUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.SetConnectionAsActiveUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.UnMarkConnectionForDeletionUseCase
import eu.wedgess.piholecontrol.presentation.base.EventDrivenViewModel
import eu.wedgess.piholecontrol.presentation.base.SideEffectViewModel
import eu.wedgess.piholecontrol.presentation.base.SideEffectViewModelImpl
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.connections.list.ConnectionsContract
import eu.wedgess.piholecontrol.utils.UiText
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ConnectionsViewModel @Inject constructor(
    fetchAllConnectionsUseCase: FetchAllConnectionsUseCase,
    private val setConnectionAsActiveUseCase: SetConnectionAsActiveUseCase,
    private val deleteConnectionMarkedForDeletionUseCase: DeleteConnectionMarkedForDeletionUseCase,
    private val markConnectionForDeletionUseCase: MarkConnectionForDeletionUseCase,
    private val unMarkConnectionForDeletionUseCase: UnMarkConnectionForDeletionUseCase
) : ViewModel(),
    EventDrivenViewModel<ConnectionsContract.Event>,
    SideEffectViewModel<ConnectionsContract.Effect> by SideEffectViewModelImpl() {

    val uiResult = fetchAllConnectionsUseCase()
        .map { result ->
            result.getOrElse {
                return@map UIResult.Error(
                    ResultType.Error.WithTitleAndSubTitle(
                        UiText.StringResource(id = R.string.connections_error_title),
                        it.message?.run { UiText.DynamicString(this@run) } ?: UiText.StringResource(
                            R.string.all_error_msg_unknown
                        )
                    )
                )
            }.run {
                return@map if (isEmpty()) {
                    UIResult.Empty(
                        ResultType.Empty.WithTitleAndSubTitle(
                            UiText.StringResource(R.string.connections_empty_title),
                            UiText.StringResource(R.string.connections_empty_message)
                        )
                    )
                } else {
                    UIResult.Loaded(ConnectionsContract.UiState(this.filterNot { it.isDeleted }))
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            UIResult.Loading(ResultType.Loading.WithTitle())
        )

    override fun onEvent(event: ConnectionsContract.Event) {
        when (event) {
            is ConnectionsContract.Event.SetActive -> setConnectionAsActive(event.id, event.name)
            ConnectionsContract.Event.AddConnection -> navigateTo(
                ConnectionsContract.Effect.Navigation.Add
            )

            is ConnectionsContract.Event.DeleteConnection -> deleteConnection(event.id, event.name)
            is ConnectionsContract.Event.EditConnection -> navigateTo(
                ConnectionsContract.Effect.Navigation.Edit(id = event.id)
            )

            is ConnectionsContract.Event.CompleteDeleteConnection -> onDeleteConnectionConfirmed(
                name = event.name
            )

            is ConnectionsContract.Event.UndoDeleteConnection -> undoDeleteConnection(
                id = event.id,
                name = event.name
            )
        }
    }

    private fun undoDeleteConnection(id: UUID, name: String) {
        viewModelScope.launch {
            unMarkConnectionForDeletionUseCase(id).onFailure {
                Timber.e(it, "Failed to unmark connection $id : $name for deletion")
                emitSideEffect(
                    ConnectionsContract.Effect.Snackbar.RestoreConnectionFailed(
                        id = id,
                        name = name
                    )
                )
            }
        }
    }

    private fun onDeleteConnectionConfirmed(name: String) {
        viewModelScope.launch {
            deleteConnectionMarkedForDeletionUseCase().onFailure {
                Timber.e(it, "Failed to delete connection $name")
                emitSideEffect(
                    ConnectionsContract.Effect.Snackbar.DeleteConnectionFailed(
                        name = name
                    )
                )
            }
        }
    }

    private fun deleteConnection(id: UUID, name: String) {
        viewModelScope.launch {
            markConnectionForDeletionUseCase(id)
                .onFailure {
                    Timber.e(it, "Failed to mark connection $id : $name for deletion")
                    emitSideEffect(
                        ConnectionsContract.Effect.Snackbar.DeleteConnectionFailed(name = name)
                    )
                }.onSuccess {
                    emitSideEffect(
                        ConnectionsContract.Effect.Snackbar.DeleteConnection(
                            id = id,
                            name = name
                        )
                    )
                }
        }
    }

    private fun navigateTo(destination: ConnectionsContract.Effect.Navigation) {
        viewModelScope.emitSideEffect(destination)
    }

    private fun setConnectionAsActive(id: UUID, name: String) {
        viewModelScope.launch {
            setConnectionAsActiveUseCase(id)
                .onFailure {
                    Timber.e(it, "Failed to set connection as active: $id : $name")
                    emitSideEffect(
                        ConnectionsContract.Effect.Snackbar.SetActiveConnectionFailed(
                            id = id,
                            name = name
                        )
                    )
                }
        }
    }
}
