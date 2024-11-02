package eu.wedgess.mihole.ui.connections.modify.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.BarcodeScanner
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.mihole.R
import eu.wedgess.mihole.ui.base.EventDrivenViewModel
import eu.wedgess.mihole.ui.base.SideEffectViewModel
import eu.wedgess.mihole.ui.base.SideEffectViewModelImpl
import eu.wedgess.mihole.ui.base.UiStateViewModel
import eu.wedgess.mihole.ui.base.UiStateViewModelImpl
import eu.wedgess.mihole.ui.connections.modify.ModifyConnectionsContract
import eu.wedgess.mihole.ui.connections.modify.controller.ModifyConnectionController
import eu.wedgess.mihole.ui.navigation.KEY_ARG_ID
import eu.wedgess.mihole.utils.UiText
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ModifyConnectionViewModel @Inject constructor(
    private val controller: ModifyConnectionController,
    val barcodeScanner: BarcodeScanner,
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    EventDrivenViewModel<ModifyConnectionsContract.Event>,
    SideEffectViewModel<ModifyConnectionsContract.Effect> by SideEffectViewModelImpl(),
    UiStateViewModel<ModifyConnectionsContract.UiState> by UiStateViewModelImpl(
        ModifyConnectionsContract.UiState.initial()
    ) {

    private val existingConnectionId: Long? = savedStateHandle.get<String?>(KEY_ARG_ID)?.toLong()

    override fun onEvent(event: ModifyConnectionsContract.Event) {
        when (event) {
            ModifyConnectionsContract.Event.FetchCurrentConnection -> fetchConnection()
            ModifyConnectionsContract.Event.SaveConnection -> saveConnection()
            ModifyConnectionsContract.Event.OnOpenBarcodeScanner -> updateUiState {
                copy(showBarcodeScanner = true)
            }

            ModifyConnectionsContract.Event.OnDismissBarcodeScanner -> updateUiState {
                copy(showBarcodeScanner = false)
            }

            is ModifyConnectionsContract.Event.OnApiPathChanged -> updateUiState { copy(apiPath = event.apiPath) }
            is ModifyConnectionsContract.Event.OnApiTokenChanged -> updateUiState {
                copy(
                    apiToken = event.apiToken,
                    showBarcodeScanner = false
                )
            }

            is ModifyConnectionsContract.Event.OnAuthUsernameChanged -> updateUiState {
                copy(authUsername = event.authUsername)
            }

            is ModifyConnectionsContract.Event.OnAuthPasswordChanged -> updateUiState {
                copy(authPassword = event.authPassword)
            }

            is ModifyConnectionsContract.Event.OnAuthRealmChanged -> updateUiState {
                copy(authRealm = event.authRealm)
            }

            is ModifyConnectionsContract.Event.OnTrustAllCertsChanged -> updateUiState {
                copy(trustAllCerts = event.trustAllCerts)
            }

            is ModifyConnectionsContract.Event.OnShowAdvancedSettingsChanged -> updateUiState {
                copy(showAdvancedSettings = event.showAdvancedSettings)
            }

            is ModifyConnectionsContract.Event.OnHostChanged -> updateUiState { copy(host = event.host) }
            is ModifyConnectionsContract.Event.OnNameChanged -> updateUiState { copy(name = event.name) }
            is ModifyConnectionsContract.Event.OnPortChanged -> updateUiState { copy(port = event.port) }
            is ModifyConnectionsContract.Event.OnProtocolChanged -> {
                updateUiState {
                    copy(
                        protocol = event.protocol,
                        port = event.protocol.defaultPort
                    )
                }
            }
        }
    }

    private fun saveConnection() {
        viewModelScope.launch {
            if (existingConnectionId != null) {
                controller.updateConnection(uiState.value.toMiHoleInfo(id = existingConnectionId))
                    .onFailure { Timber.e("Updated connection failed ${it.message}", it) }
                    .onSuccess { navigateTo(ModifyConnectionsContract.Effect.Navigation.Back) }
            } else {
                controller.saveConnection(uiState.value.toMiHoleInfo())
                    .onFailure { Timber.e("Save connection failed ${it.message}", it) }
                    .onSuccess { navigateTo(ModifyConnectionsContract.Effect.Navigation.Back) }
            }
        }
    }

    private fun navigateTo(destination: ModifyConnectionsContract.Effect.Navigation) {
        viewModelScope.emitSideEffect(destination)
    }

    private fun fetchConnection() {
        existingConnectionId?.run {
            viewModelScope.launch {
                val connection = requireNotNull(controller.fetchConnection(this@run).getOrThrow())
                updateUiState { copy(currentConnection = connection) }
            }
        }
    }
}