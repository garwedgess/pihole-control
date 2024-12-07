package eu.wedgess.piholecontrol.presentation.connections.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.usecases.connections.DeleteConnectionUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.FetchAllConnectionsUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.SetConnectionAsActiveUseCase
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
import javax.inject.Inject

@HiltViewModel
class ConnectionsViewModel @Inject constructor(
    fetchAllConnectionsUseCase: FetchAllConnectionsUseCase,
    private val setConnectionAsActiveUseCase: SetConnectionAsActiveUseCase,
    private val deleteConnectionUseCase: DeleteConnectionUseCase
) : ViewModel(),
    EventDrivenViewModel<ConnectionsContract.Event>,
    SideEffectViewModel<ConnectionsContract.Effect> by SideEffectViewModelImpl() {

    val uiResult = fetchAllConnectionsUseCase()
        .map { result ->
            result.getOrElse {
                return@map UIResult.Error(ResultType.Error.WithTitle(UiText.DynamicString("Failed to fetch connections")))
            }.run {
                return@map if (isEmpty()) {
                        UIResult.Empty(ResultType.Empty.WithTitleAndSubTitle(
                            UiText.DynamicString("No connections"),
                            UiText.DynamicString("Add a connection to get started")
                        ))
                    } else {
                        UIResult.Loaded(ConnectionsContract.UiState(this))
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
            is ConnectionsContract.Event.SetActive -> setConnectionAsActive(event.connection)
            ConnectionsContract.Event.AddConnection -> navigateTo(ConnectionsContract.Effect.Navigation.Add)
            is ConnectionsContract.Event.DeleteConnection -> deleteConnectionById(event.connection.id)
            is ConnectionsContract.Event.EditConnection -> navigateTo(
                ConnectionsContract.Effect.Navigation.Edit(
                    event.connectionId
                )
            )
        }
    }

    private fun deleteConnectionById(id: Long) {
        viewModelScope.launch {
            deleteConnectionUseCase(id).onFailure {
                Timber.e(it, "Failed to delete connection by id: $id")
            }.onSuccess {

            }
        }
    }

    private fun navigateTo(destination: ConnectionsContract.Effect.Navigation) {
        viewModelScope.emitSideEffect(destination)
    }

    private fun setConnectionAsActive(connection: ConnectionEntity) {
        viewModelScope.launch {
            setConnectionAsActiveUseCase(connection.id)
                .onFailure {
                    Timber.e("Failed to set connection as active: $connection", it)
                }
                .onSuccess {

                }
        }
    }
}