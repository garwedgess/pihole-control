package eu.wedgess.piholecontrol.presentation.connections.modify

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.presentation.connections.modify.model.ConnectionInputError
import eu.wedgess.piholecontrol.presentation.connections.modify.model.ModifyConnectionDialogType
import io.ktor.http.URLProtocol
import java.util.UUID

interface ModifyConnectionsContract {

    data class UiState(
        val currentConnection: ConnectionEntity?,
        val name: String,
        val host: String,
        val port: String,
        val protocol: URLProtocol,
        val apiPath: String,
        val password: String,
        val authUsername: String,
        val authPassword: String,
        val authRealm: String,
        val trustAllCerts: Boolean,
        val dialogType: ModifyConnectionDialogType,
        val showAdvancedSettings: Boolean,
        val passwordVisible: Boolean,
        val basicAuthPasswordVisible: Boolean,
        val inputErrors: List<ConnectionInputError>
    ) {

        fun toPiHoleConnectionEntity(id: UUID? = null, sid: String? = null): ConnectionEntity =
            ConnectionEntity(
                id = id ?: UUID.randomUUID(),
                name = this.name,
                host = this.host,
                port = this.port.toInt(),
                protocol = this.protocol,
                sid = sid ?: "",
                password = this.password,
                apiPath = this.apiPath,
                authUsername = this.authUsername,
                authPassword = this.authPassword,
                authRealm = this.authRealm,
                trustAllCerts = this.trustAllCerts,
                isDeleted = false,
                isActive = false
            )

        companion object {

            fun initial(
                defaultInfo: ConnectionEntity = ConnectionEntity.default
            ): UiState {
                val genericUiState = UiState(
                    currentConnection = null,
                    dialogType = ModifyConnectionDialogType.None,
                    showAdvancedSettings = false,
                    name = defaultInfo.name,
                    host = defaultInfo.host,
                    port = defaultInfo.port.toString(),
                    protocol = defaultInfo.protocol,
                    apiPath = defaultInfo.apiPath,
                    password = "",
                    authUsername = defaultInfo.authUsername,
                    authPassword = defaultInfo.authPassword,
                    trustAllCerts = defaultInfo.trustAllCerts,
                    authRealm = defaultInfo.authRealm,
                    inputErrors = emptyList(),
                    passwordVisible = false,
                    basicAuthPasswordVisible = false
                )

                return genericUiState.copy(password = defaultInfo.password)
            }
        }
    }

    sealed interface Effect {
        sealed interface Navigation : Effect {
            data object Back : Navigation
        }
    }

    sealed interface Event {
        data object FetchCurrentConnection : Event
        data class OnNameChanged(val name: String) : Event
        data class OnHostChanged(val host: String) : Event
        data class OnPortChanged(val port: String) : Event
        data class OnProtocolChanged(val protocol: URLProtocol) : Event
        data class OnApiPathChanged(val apiPath: String) : Event
        data class OnPasswordChanged(val password: String) : Event
        data class OnPasswordFieldVisibilityChanged(val visible: Boolean) : Event
        data class OnBasicPasswordFieldVisibilityChanged(val visible: Boolean) : Event
        data class OnAuthUsernameChanged(val authUsername: String) : Event
        data class OnAuthPasswordChanged(val authPassword: String) : Event
        data class OnAuthRealmChanged(val authRealm: String) : Event
        data class OnTrustAllCertsChanged(val trustAllCerts: Boolean) : Event
        data class OnToggleAdvancedSettingsChanged(val showAdvancedSettings: Boolean) : Event
        data object OnDismissDialog : Event
        data object SaveConnection : Event
    }
}
