package eu.wedgess.mihole.ui.connections.all.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.mihole.R
import eu.wedgess.mihole.data.PiHoleRepository
import eu.wedgess.mihole.data.model.MiHolesInfo
import eu.wedgess.mihole.ui.connections.all.ConnectionsContract
import eu.wedgess.mihole.utils.UiText
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectionsViewModel @Inject constructor(
    private val repository: PiHoleRepository
) : ViewModel(), ConnectionsContract {

    private val _uiState: MutableStateFlow<ConnectionsContract.UiState> =
        MutableStateFlow(ConnectionsContract.UiState.initial())
    override val uiState: StateFlow<ConnectionsContract.UiState> = _uiState.asStateFlow()

    private val _effect: Channel<ConnectionsContract.Effect> = Channel(Channel.UNLIMITED)
    override val effect: Flow<ConnectionsContract.Effect> = _effect.receiveAsFlow()

    override fun onEvent(event: ConnectionsContract.Event) {
        when (event) {
            ConnectionsContract.Event.FetchConnections -> fetchConnections()
            is ConnectionsContract.Event.SetActive -> setConnectionAsActive(event.connection)
            ConnectionsContract.Event.AddConnection -> navigateTo(ConnectionsContract.Effect.Navigation.Add)
            is ConnectionsContract.Event.DeleteConnection -> TODO()
            is ConnectionsContract.Event.EditConnection -> navigateTo(ConnectionsContract.Effect.Navigation.Edit(event.connectionId))
        }
    }

    private fun navigateTo(destination: ConnectionsContract.Effect.Navigation) {
        viewModelScope.launch { _effect.send(destination) }
    }

    private val connectionErrorHandler = CoroutineExceptionHandler { _, throwable ->
        val errorMessage = throwable.message?.run {
            UiText.DynamicString(this)
        } ?: UiText.StringResource(R.string.all_error_msg_unknown)
        _uiState.update { it.connectionsError(errorMessage) }
    }

    private fun setConnectionAsActive(connection: MiHolesInfo) {
        viewModelScope.launch {
            repository.setConnectionAsActive(connection).getOrThrow()
            fetchConnections()
        }
    }

    private fun fetchConnections() {
        viewModelScope.launch(connectionErrorHandler) {
            val connections = repository.fetchAll().getOrThrow()
            _uiState.update {
                it.connections(connections.takeIf { connections.isNotEmpty() } ?: listOf(
                    MiHolesInfo.default
                ))
            }
        }
    }
}