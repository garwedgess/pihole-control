package eu.wedgess.mihole.ui.connections.modify

import eu.wedgess.mihole.R
import eu.wedgess.mihole.data.model.MiHolesInfo
import eu.wedgess.mihole.ui.base.UiResult
import eu.wedgess.mihole.ui.base.UnidirectionalViewModel
import eu.wedgess.mihole.utils.UiText
import io.ktor.http.URLProtocol

interface ModifyConnectionsContract :
    UnidirectionalViewModel<ModifyConnectionsContract.UiState, ModifyConnectionsContract.Event, ModifyConnectionsContract.Effect> {

    data class UiState(
        val currentConnection: UiResult<MiHolesInfo>,
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
        val showBarcodeScanner: Boolean,
        val showAdvancedSettings: Boolean
    ) {

        fun connection(connection: MiHolesInfo?): UiState =
            this.copy(
                currentConnection = connection?.run {
                    UiResult.Success(this)
                } ?: UiResult.Error(UiText.StringResource(R.string.error_connection_not_found)),
                showAdvancedSettings = connection?.run {
                    this.authUsername.isNotBlank() || this.authPassword.isNotBlank()
                } ?: false
            ).also {
                return if (connection != null) {
                    this.copy(
                        name = connection.name,
                        host = connection.host,
                        port = connection.port,
                        protocol = connection.protocol,
                        apiPath = connection.apiPath,
                        apiToken = connection.apiPath,
                        authUsername = this.authUsername,
                        authPassword = this.authPassword,
                        authRealm = this.authRealm,
                        trustAllCerts = this.trustAllCerts
                    )
                } else {
                    this
                }
            }

        fun connectionError(errorMessage: UiText): UiState =
            this.copy(currentConnection = UiResult.Error(errorMessage))

        fun toMiHoleInfo(id: Long? = null): MiHolesInfo =
            MiHolesInfo(
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
            private val DEFAULT_INFO = MiHolesInfo.default

            fun initial() = UiState(
                currentConnection = UiResult.Loading,
                showBarcodeScanner = false,
                showAdvancedSettings = false,
                name = DEFAULT_INFO.name,
                host = DEFAULT_INFO.host,
                port = DEFAULT_INFO.port,
                protocol = DEFAULT_INFO.protocol,
                apiPath = DEFAULT_INFO.apiPath,
                apiToken = DEFAULT_INFO.token ?: "",
                authUsername = DEFAULT_INFO.authUsername,
                authPassword = DEFAULT_INFO.authPassword,
                trustAllCerts = DEFAULT_INFO.trustAllCerts,
                authRealm = DEFAULT_INFO.authRealm
            )
        }
    }

    sealed interface Effect {
        sealed interface Navigation : Effect {
            object Back : Navigation
        }

    }

    sealed interface Event {
        object FetchCurrentConnection : Event
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
        data class OnShowAdvancedSettingsChanged(val showAdvancedSettings: Boolean) : Event
        object OnOpenBarcodeScanner : Event
        object OnDismissBarcodeScanner : Event
        object SaveConnection : Event
    }
}