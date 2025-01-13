package eu.wedgess.piholecontrol.presentation.connections.modify

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.presentation.connections.modify.model.ConnectionInputError
import eu.wedgess.piholecontrol.presentation.connections.modify.model.ModifyConnectionDialogType
import eu.wedgess.piholecontrol.presentation.connections.modify.model.PiHoleApiVersion
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
        val apiToken: String,
        val password: String,
        val apiVersion: PiHoleApiVersion,
        val authUsername: String,
        val authPassword: String,
        val authRealm: String,
        val trustAllCerts: Boolean,
        val dialogType: ModifyConnectionDialogType,
        val showAdvancedSettings: Boolean,
        val apiKeyPasswordVisible: Boolean,
        val basicAuthPasswordVisible: Boolean,
        val inputErrors: List<ConnectionInputError>
    ) {

        fun toPiHoleConnectionEntity(id: UUID? = null): ConnectionEntity =
            when (apiVersion) {
                PiHoleApiVersion.Version5 -> ConnectionEntity.Version5(
                    id = id ?: UUID.randomUUID(),
                    name = this.name,
                    host = this.host,
                    port = this.port.toInt(),
                    protocol = this.protocol,
                    apiPath = this.apiPath,
                    token = this.apiToken,
                    authUsername = this.authUsername,
                    authPassword = this.authPassword,
                    authRealm = this.authRealm,
                    trustAllCerts = this.trustAllCerts,
                    isDeleted = false,
                    isActive = false
                )

                PiHoleApiVersion.Version6 -> ConnectionEntity.Version6(
                    id = UUID.randomUUID(),
                    name = this.name,
                    host = this.host,
                    port = this.port.toInt(),
                    protocol = this.protocol,
                    sid = "",
                    password = this.password,
                    authUsername = this.authUsername,
                    authPassword = this.authPassword,
                    authRealm = this.authRealm,
                    trustAllCerts = this.trustAllCerts,
                    isDeleted = false,
                    isActive = false
                )
            }

        companion object {

            fun initial(
                defaultInfo: ConnectionEntity = ConnectionEntity.Version5.default
            ): UiState {
                val genericUiState = UiState(
                    currentConnection = null,
                    dialogType = ModifyConnectionDialogType.None,
                    showAdvancedSettings = false,
                    name = defaultInfo.name,
                    host = defaultInfo.host,
                    port = defaultInfo.port.toString(),
                    protocol = defaultInfo.protocol,
                    apiPath = "",
                    apiToken = "",
                    password = "",
                    apiVersion = PiHoleApiVersion.Version5,
                    authUsername = defaultInfo.authUsername,
                    authPassword = defaultInfo.authPassword,
                    trustAllCerts = defaultInfo.trustAllCerts,
                    authRealm = defaultInfo.authRealm,
                    inputErrors = emptyList(),
                    apiKeyPasswordVisible = false,
                    basicAuthPasswordVisible = false
                )

                return when (defaultInfo) {
                    is ConnectionEntity.Version5 -> {
                        genericUiState.copy(
                            apiPath = defaultInfo.apiPath,
                            apiToken = defaultInfo.token,
                            apiVersion = PiHoleApiVersion.Version5
                        )
                    }

                    is ConnectionEntity.Version6 -> {
                        genericUiState.copy(
                            password = defaultInfo.password,
                            apiVersion = PiHoleApiVersion.Version6,
                        )
                    }
                }
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
        data class OnApiTokenChanged(val apiToken: String) : Event
        data class OnPasswordChanged(val password: String) : Event
        data class OnPasswordFieldVisibilityChanged(val visible: Boolean) : Event
        data class OnBasicPasswordFieldVisibilityChanged(val visible: Boolean) : Event
        data class OnAuthUsernameChanged(val authUsername: String) : Event
        data class OnAuthPasswordChanged(val authPassword: String) : Event
        data class OnAuthRealmChanged(val authRealm: String) : Event
        data class OnTrustAllCertsChanged(val trustAllCerts: Boolean) : Event
        data class OnApiVersionChanged(val apiVersion: PiHoleApiVersion) : Event
        data class OnToggleAdvancedSettingsChanged(val showAdvancedSettings: Boolean) : Event
        data object OnOpenBarcodeScanner : Event
        data object OnDismissDialog : Event
        data object SaveConnection : Event
    }
}
