package eu.wedgess.mihole.ui.connections.modify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.BarcodeScanner
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.mihole.R
import eu.wedgess.mihole.data.PiHoleRepository
import eu.wedgess.mihole.ui.base.UiResult
import eu.wedgess.mihole.ui.connections.modify.ModifyConnectionsContract
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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ModifyConnectionViewModel @Inject constructor(
    private val repository: PiHoleRepository,
    val barcodeScanner: BarcodeScanner
) : ViewModel(), ModifyConnectionsContract {

    private val _uiState: MutableStateFlow<ModifyConnectionsContract.UiState> =
        MutableStateFlow(ModifyConnectionsContract.UiState.initial())
    override val uiState: StateFlow<ModifyConnectionsContract.UiState> = _uiState.asStateFlow()

    private val _effect: Channel<ModifyConnectionsContract.Effect> = Channel(Channel.UNLIMITED)
    override val effect: Flow<ModifyConnectionsContract.Effect> = _effect.receiveAsFlow()

    override fun onEvent(event: ModifyConnectionsContract.Event) {
        when (event) {
            is ModifyConnectionsContract.Event.FetchCurrentConnection -> fetchConnection(event.id)
            ModifyConnectionsContract.Event.SaveConnection -> saveConnection()
            ModifyConnectionsContract.Event.OnOpenBarcodeScanner -> _uiState.update { it.copy(showBarcodeScanner = true) }
            ModifyConnectionsContract.Event.OnDismissBarcodeScanner -> _uiState.update { it.copy(showBarcodeScanner = false) }
            is ModifyConnectionsContract.Event.OnApiPathChanged -> _uiState.update { it.copy(apiPath = event.apiPath) }
            is ModifyConnectionsContract.Event.OnApiTokenChanged -> _uiState.update { it.copy(apiToken = event.apiToken, showBarcodeScanner = false) }
            is ModifyConnectionsContract.Event.OnAuthUsernameChanged -> _uiState.update { it.copy(authUsername = event.authUsername) }
            is ModifyConnectionsContract.Event.OnAuthPasswordChanged -> _uiState.update { it.copy(authPassword = event.authPassword) }
            is ModifyConnectionsContract.Event.OnAuthRealmChanged -> _uiState.update { it.copy(authRealm = event.authRealm) }
            is ModifyConnectionsContract.Event.OnTrustAllCertsChanged -> _uiState.update { it.copy(trustAllCerts = event.trustAllCerts) }
            is ModifyConnectionsContract.Event.OnShowAdvancedSettingsChanged -> _uiState.update { it.copy(showAdvancedSettings = event.showAdvancedSettings) }
            is ModifyConnectionsContract.Event.OnHostChanged -> _uiState.update { it.copy(host = event.host) }
            is ModifyConnectionsContract.Event.OnNameChanged -> _uiState.update { it.copy(name = event.name) }
            is ModifyConnectionsContract.Event.OnPortChanged -> _uiState.update { it.copy(port = event.port) }
            is ModifyConnectionsContract.Event.OnProtocolChanged -> {
                _uiState.update { it.copy(protocol = event.protocol, port = event.protocol.defaultPort) }
            }
        }
    }

    private fun saveConnection() {
        viewModelScope.launch {
            repository.insertMiHole(_uiState.value.toMiHoleInfo(id = (_uiState.value.currentConnection as? UiResult.Success)?.data?.id))
                .onFailure { Timber.e("Dave connection failed ${it.message}", it) }
                .onSuccess { navigateTo(ModifyConnectionsContract.Effect.Navigation.Back) }
        }
    }

    private fun navigateTo(destination: ModifyConnectionsContract.Effect.Navigation) {
        viewModelScope.launch { _effect.send(destination) }
    }

    private val connectionErrorHandler = CoroutineExceptionHandler { _, throwable ->
        val errorMessage = throwable.message?.run {
            UiText.DynamicString(this)
        } ?: UiText.StringResource(R.string.all_error_msg_unknown)
        _uiState.update { it.connectionError(errorMessage) }
    }

    private fun fetchConnection(id: Long) {
        viewModelScope.launch(connectionErrorHandler) {
            val connection = repository.fetchById(id).getOrThrow()
            _uiState.update {
                it.connection(connection)
            }
        }
    }
}