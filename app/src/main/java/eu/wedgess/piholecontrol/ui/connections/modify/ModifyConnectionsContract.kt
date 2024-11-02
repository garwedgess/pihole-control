package eu.wedgess.piholecontrol.ui.connections.modify

import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.ui.connections.modify.model.ModifyConnectionDialogType
import io.ktor.http.URLProtocol

interface ModifyConnectionsContract {

    data class UiState(
        val currentConnection: ConnectionInfo?,
        val name: String,
        val host: String,
        val port: Int,
        val protocol: URLProtocol,
        val apiPath: String,
        val apiToken: String,
        val authUsername: String,
        val authPassword: String,
        val authRealm: String,
        val trustAllCerts: Boolean,
        val dialogType: ModifyConnectionDialogType,
        val showAdvancedSettings: Boolean
    ) {

        fun toMiHoleInfo(id: Long? = null): ConnectionInfo =
            ConnectionInfo(
                id = id ?: -1,
                name = this.name,
                host = this.host,
                port = this.port,
                protocol = this.protocol,
                apiPath = this.apiPath,
                token = this.apiToken,
                authUsername = this.authUsername,
                authPassword = this.authPassword,
                authRealm = this.authRealm,
                trustAllCerts = this.trustAllCerts,
                isActive = false
            )

        companion object {
            private val DEFAULT_INFO = ConnectionInfo.default

            fun initial() = UiState(
                currentConnection = null,
                dialogType = ModifyConnectionDialogType.None,
                showAdvancedSettings = false,
                name = DEFAULT_INFO.name,
                host = DEFAULT_INFO.host,
                port = DEFAULT_INFO.port,
                protocol = DEFAULT_INFO.protocol,
                apiPath = DEFAULT_INFO.apiPath,
                apiToken = DEFAULT_INFO.token,
                authUsername = DEFAULT_INFO.authUsername,
                authPassword = DEFAULT_INFO.authPassword,
                trustAllCerts = DEFAULT_INFO.trustAllCerts,
                authRealm = DEFAULT_INFO.authRealm
            )
        }
    }

    sealed interface Effect {
        sealed interface Navigation : Effect {
           data  object Back : Navigation
        }

    }

    sealed interface Event {
        data object FetchCurrentConnection : Event
        data class OnNameChanged(val name: String) : Event
        data class OnHostChanged(val host: String) : Event
        data class OnPortChanged(val port: Int) : Event
        data class OnProtocolChanged(val protocol: URLProtocol) : Event
        data class OnApiPathChanged(val apiPath: String) : Event
        data class OnApiTokenChanged(val apiToken: String) : Event
        data class OnAuthUsernameChanged(val authUsername: String) : Event
        data class OnAuthPasswordChanged(val authPassword: String) : Event
        data class OnAuthRealmChanged(val authRealm: String) : Event
        data class OnTrustAllCertsChanged(val trustAllCerts: Boolean) : Event
        data class OnToggleAdvancedSettingsChanged(val showAdvancedSettings: Boolean) : Event
        data object OnOpenBarcodeScanner : Event
        data object OnDismissDialog : Event
        data object SaveConnection : Event
    }
}