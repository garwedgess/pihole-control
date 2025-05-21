package eu.wedgess.piholecontrol.presentation.connections.modify.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.usecases.auth.GenerateSessionIdUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.AddConnectionUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.FetchConnectionByIdUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.UpdateConnectionUseCase
import eu.wedgess.piholecontrol.presentation.base.EventDrivenViewModel
import eu.wedgess.piholecontrol.presentation.base.SideEffectViewModel
import eu.wedgess.piholecontrol.presentation.base.SideEffectViewModelImpl
import eu.wedgess.piholecontrol.presentation.base.UiStateViewModel
import eu.wedgess.piholecontrol.presentation.base.UiStateViewModelImpl
import eu.wedgess.piholecontrol.presentation.connections.modify.ModifyConnectionsContract
import eu.wedgess.piholecontrol.presentation.connections.modify.model.ConnectionInputError
import eu.wedgess.piholecontrol.presentation.connections.modify.model.ModifyConnectionDialogType
import eu.wedgess.piholecontrol.presentation.navigation.Screens
import eu.wedgess.piholecontrol.utils.UiText
import eu.wedgess.piholecontrol.utils.extensions.isDigitsOnly
import eu.wedgess.piholecontrol.utils.extensions.isValidHost
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import kotlin.reflect.KClass

@HiltViewModel
class ModifyConnectionViewModel @Inject constructor(
    private val fetchConnectionByIdUseCase: FetchConnectionByIdUseCase,
    private val addConnectionUseCase: AddConnectionUseCase,
    private val updateConnectionUseCase: UpdateConnectionUseCase,
    private val generateSessionIdUseCase: GenerateSessionIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    EventDrivenViewModel<ModifyConnectionsContract.Event>,
    SideEffectViewModel<ModifyConnectionsContract.Effect> by SideEffectViewModelImpl(),
    UiStateViewModel<ModifyConnectionsContract.UiState> by UiStateViewModelImpl(
        ModifyConnectionsContract.UiState.initial()
    ) {

    private val existingConnectionId: UUID? =
        savedStateHandle.toRoute<Screens.ModifyConnection>().connectionId?.run {
            UUID.fromString(this@run)
        }

    override fun onEvent(event: ModifyConnectionsContract.Event) {
        when (event) {
            ModifyConnectionsContract.Event.FetchCurrentConnection -> fetchConnection()
            ModifyConnectionsContract.Event.SaveConnection -> saveConnection()

            ModifyConnectionsContract.Event.OnDismissDialog -> updateUiState {
                copy(dialogType = ModifyConnectionDialogType.None)
            }

            is ModifyConnectionsContract.Event.OnApiPathChanged -> updateUiState {
                copy(apiPath = event.apiPath)
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

            is ModifyConnectionsContract.Event.OnToggleAdvancedSettingsChanged -> updateUiState {
                copy(showAdvancedSettings = event.showAdvancedSettings)
            }

            is ModifyConnectionsContract.Event.OnHostChanged -> onHostChanged(event.host)

            is ModifyConnectionsContract.Event.OnNameChanged -> onNameChanged(event.name)

            is ModifyConnectionsContract.Event.OnPortChanged -> onPortChanged(event.port)

            is ModifyConnectionsContract.Event.OnPasswordChanged ->
                onPasswordChanged(event.password)

            is ModifyConnectionsContract.Event.OnProtocolChanged -> {
                updateUiState {
                    copy(
                        protocol = event.protocol,
                        port = event.protocol.defaultPort.toString()
                    )
                }
            }

            is ModifyConnectionsContract.Event.OnPasswordFieldVisibilityChanged -> updateUiState {
                copy(passwordVisible = event.visible)
            }

            is ModifyConnectionsContract.Event.OnBasicPasswordFieldVisibilityChanged -> {
                updateUiState {
                    copy(basicAuthPasswordVisible = event.visible)
                }
            }
        }
    }

    private fun onPasswordChanged(password: String) {
        val isValidPassword = password.isNotBlank()
        val passwordError = if (!isValidPassword) {
            ConnectionInputError.Password(
                UiText.StringResource(R.string.connection_password_error_blank)
            )
        } else {
            null
        }

        updateUiState {
            copy(
                password = password,
                inputErrors = updateInputErrors(
                    currentErrors = inputErrors,
                    errorClass = ConnectionInputError.Password::class,
                    newError = passwordError
                )
            )
        }
    }

    /**
     * Updates input errors for a specific field
     *
     * @param currentErrors Current list of all input errors
     * @param errorClass The class of error to filter out
     * @param newError The new error to add if validation fails
     * @return Updated list of input errors
     */
    private fun updateInputErrors(
        currentErrors: List<ConnectionInputError>,
        errorClass: KClass<out ConnectionInputError>,
        newError: ConnectionInputError?
    ): List<ConnectionInputError> = currentErrors
        .filterNot { it::class == errorClass }
        .let { filtered -> newError?.let { filtered + it } ?: filtered }

    private fun onNameChanged(name: String) {
        val isValidName = name.isNotBlank()
        val nameError = if (!isValidName) {
            ConnectionInputError.Name(UiText.StringResource(R.string.connection_name_error_blank))
        } else {
            null
        }

        updateUiState {
            copy(
                name = name,
                inputErrors = updateInputErrors(
                    currentErrors = inputErrors,
                    errorClass = ConnectionInputError.Name::class,
                    newError = nameError
                )
            )
        }
    }

    private fun onHostChanged(host: String) {
        val isValidHost = host.isValidHost()
        val hostError = if (!isValidHost) {
            ConnectionInputError.Host(UiText.StringResource(R.string.connection_host_error_invalid))
        } else {
            null
        }

        updateUiState {
            copy(
                host = host,
                inputErrors = updateInputErrors(
                    currentErrors = inputErrors,
                    errorClass = ConnectionInputError.Host::class,
                    newError = hostError
                )
            )
        }
    }

    private fun onPortChanged(port: String) {
        val portNumber = port.toIntOrNull()
        val (validatedPort, portError) = when {
            port.isEmpty() ->
                port to
                    ConnectionInputError.Port(
                        UiText.StringResource(R.string.connection_port_error_blank)
                    )

            !port.isDigitsOnly() ->
                "" to
                    ConnectionInputError.Port(
                        UiText.StringResource(R.string.connection_port_error_invalid)
                    )

            portNumber !in 1..65535 ->
                "" to
                    ConnectionInputError.Port(
                        UiText.StringResource(R.string.connection_port_error_range)
                    )

            else -> port to null
        }

        updateUiState {
            copy(
                port = validatedPort,
                inputErrors = updateInputErrors(
                    currentErrors = inputErrors,
                    errorClass = ConnectionInputError.Port::class,
                    newError = portError
                )
            )
        }
    }

    private fun saveConnection() {
        viewModelScope.launch {
            if (existingConnectionId != null) {
                updateUiState {
                    copy(
                        dialogType = ModifyConnectionDialogType.LoadingDialog(
                            UiText.StringResource(
                                R.string.connection_loading_dialog_updating_message
                            )
                        )
                    )
                }
                val updatedConnection = uiState.value.toPiHoleConnectionEntity(
                    id = existingConnectionId
                )
                updateConnection(updatedConnection)
            } else {
                val newConnection = uiState.value.toPiHoleConnectionEntity()
                generateSessionAndInsert(newConnection)
            }
        }
    }

    private fun generateSessionAndInsert(newConnection: ConnectionEntity) {
        updateUiState {
            copy(
                dialogType = ModifyConnectionDialogType.LoadingDialog(
                    UiText.StringResource(
                        R.string.connection_loading_dialog_validating_password_message
                    )
                )
            )
        }
        viewModelScope.launch {
            generateSessionIdUseCase.invoke(newConnection)
                .onSuccess { response ->
                    insertConnection(newConnection.copy(sid = response.sid))
                }
                .onFailure {
                    val errorMessage = it.message ?: "unknown error"
                    updateUiState {
                        copy(
                            dialogType = ModifyConnectionDialogType.FailedToSaveConnection(
                                UiText.StringResourceWithArgs(
                                    R.string.connection_dialog_message_saving_failed,
                                    errorMessage
                                )
                            ),
                            inputErrors = updateInputErrors(
                                currentErrors = inputErrors,
                                errorClass = ConnectionInputError.Password::class,
                                newError = if (errorMessage.contains("password incorrect")) {
                                    ConnectionInputError.Password(
                                        UiText.DynamicString(errorMessage)
                                    )
                                } else {
                                    null
                                }
                            )
                        )
                    }
                }
        }
    }

    private suspend fun insertConnection(connectionEntity: ConnectionEntity) {
        updateUiState {
            copy(
                dialogType = ModifyConnectionDialogType.LoadingDialog(
                    UiText.StringResource(
                        R.string.connection_loading_dialog_saving_message
                    )
                )
            )
        }
        addConnectionUseCase(connectionEntity)
            .onFailure {
                updateUiState {
                    copy(
                        dialogType = ModifyConnectionDialogType.FailedToSaveConnection(
                            UiText.StringResourceWithArgs(
                                R.string.connection_dialog_message_saving_failed,
                                it.message ?: "unknown error"
                            )
                        )
                    )
                }
            }
            .onSuccess {
                updateUiState {
                    copy(dialogType = ModifyConnectionDialogType.None)
                }
                navigateTo(ModifyConnectionsContract.Effect.Navigation.Back)
            }
    }

    private suspend fun updateConnection(connectionEntity: ConnectionEntity) {
        updateConnectionUseCase(connectionEntity)
            .onSuccess {
                updateUiState {
                    copy(dialogType = ModifyConnectionDialogType.None)
                }
                navigateTo(ModifyConnectionsContract.Effect.Navigation.Back)
            }
            .onFailure {
                updateUiState {
                    copy(
                        dialogType = ModifyConnectionDialogType.FailedToSaveConnection(
                            UiText.StringResourceWithArgs(
                                R.string.connection_dialog_message_update_failed,
                                it.message ?: "unknown error"
                            )
                        )
                    )
                }
            }
    }

    private fun navigateTo(destination: ModifyConnectionsContract.Effect.Navigation) {
        viewModelScope.emitSideEffect(destination)
    }

    private fun fetchConnection() {
        existingConnectionId?.run {
            viewModelScope.launch {
                val connection = fetchConnectionByIdUseCase(this@run).getOrThrow()
                updateUiState {
                    with(connection) {
                        copy(
                            currentConnection = this,
                            name = this.name,
                            host = this.host,
                            port = this.port.toString(),
                            protocol = this.protocol,
                            password = this.password,
                            apiPath = this.apiPath,
                            authUsername = this.authUsername,
                            authPassword = this.authPassword,
                            authRealm = this.authRealm,
                            trustAllCerts = this.trustAllCerts
                        )
                    }
                }
            }
        }
    }
}
