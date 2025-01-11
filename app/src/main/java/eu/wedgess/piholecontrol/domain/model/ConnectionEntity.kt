package eu.wedgess.piholecontrol.domain.model

import io.ktor.http.URLProtocol
import java.util.UUID

sealed class ConnectionEntity {

    abstract val id: UUID
    abstract val name: String
    abstract val protocol: URLProtocol
    abstract val host: String
    abstract val port: Int
    abstract val authUsername: String
    abstract val authPassword: String
    abstract val authRealm: String
    abstract val trustAllCerts: Boolean
    abstract val isDeleted: Boolean
    abstract val isActive: Boolean

    val hasAuthCredentials: Boolean get() = authUsername.isNotBlank() && authPassword.isNotBlank()

    data class Version5(
        override val id: UUID,
        override val name: String,
        override val protocol: URLProtocol,
        override val host: String,
        override val port: Int,
        val apiPath: String,
        val token: String,
        override val authUsername: String,
        override val authPassword: String,
        override val authRealm: String,
        override val trustAllCerts: Boolean,
        override val isDeleted: Boolean,
        override val isActive: Boolean
    ) : ConnectionEntity() {

        companion object {
            val default = Version5(
                id = UUID.randomUUID(),
                name = "Default",
                protocol = URLProtocol.HTTP,
                host = "pi.hole",
                port = 80,
                apiPath = "/admin/api.php",
                token = "",
                authUsername = "",
                authPassword = "",
                authRealm = "",
                trustAllCerts = false,
                isDeleted = false,
                isActive = true
            )
        }
    }

    data class Version6(
        override val id: UUID,
        override val name: String,
        override val protocol: URLProtocol,
        override val host: String,
        override val port: Int,
        val password: String,
        val sid: String,
        override val authUsername: String,
        override val authPassword: String,
        override val authRealm: String,
        override val trustAllCerts: Boolean,
        override val isDeleted: Boolean,
        override val isActive: Boolean
    ) : ConnectionEntity() {

        companion object {
            val default = Version6(
                id = UUID.randomUUID(),
                name = "Default",
                protocol = URLProtocol.HTTP,
                host = "pi.hole",
                port = 80,
                password = "",
                sid = "",
                authUsername = "",
                authPassword = "",
                authRealm = "",
                trustAllCerts = false,
                isDeleted = false,
                isActive = true
            )
        }
    }
}
