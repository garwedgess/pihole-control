package eu.wedgess.piholecontrol.ui.connections.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.data.toConnectionInfo
import eu.wedgess.piholecontrol.ui.base.EventDrivenViewModel
import eu.wedgess.piholecontrol.ui.base.SideEffectViewModel
import eu.wedgess.piholecontrol.ui.base.SideEffectViewModelImpl
import eu.wedgess.piholecontrol.ui.compose.ResultType
import eu.wedgess.piholecontrol.ui.compose.UIResult
import eu.wedgess.piholecontrol.ui.connections.list.ConnectionsContract
import eu.wedgess.piholecontrol.ui.connections.list.controller.ConnectionsController
import eu.wedgess.piholecontrol.utils.UiText
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ConnectionsViewModel @Inject constructor(
    private val controller: ConnectionsController
) : ViewModel(),
    EventDrivenViewModel<ConnectionsContract.Event>,
    SideEffectViewModel<ConnectionsContract.Effect> by SideEffectViewModelImpl() {

    val uiResult = controller.fetchAll()
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
                        UIResult.Loaded(ConnectionsContract.UiState(map { it.toConnectionInfo() }))
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
            is ConnectionsContract.Event.DeleteConnection -> TODO()
            is ConnectionsContract.Event.EditConnection -> navigateTo(
                ConnectionsContract.Effect.Navigation.Edit(
                    event.connectionId
                )
            )
        }
    }

    private fun navigateTo(destination: ConnectionsContract.Effect.Navigation) {
        viewModelScope.emitSideEffect(destination)
    }

    private fun setConnectionAsActive(connection: ConnectionInfo) {
        viewModelScope.launch {
            controller.setConnectionAsActive(connection.id)
                .onFailure {
                    Timber.e("Failed to set connection as active: $connection", it)
                }
                .onSuccess {

                }
        }
    }
}