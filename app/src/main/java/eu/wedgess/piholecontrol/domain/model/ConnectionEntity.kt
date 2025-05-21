package eu.wedgess.piholecontrol.domain.model

import io.ktor.http.URLProtocol
import java.util.UUID

data class ConnectionEntity(
    val id: UUID,
    val name: String,
    val protocol: URLProtocol,
    val host: String,
    val port: Int,
    val password: String,
    val apiPath: String,
    val sid: String,
    val authUsername: String,
    val authPassword: String,
    val authRealm: String,
    val trustAllCerts: Boolean,
    val isDeleted: Boolean,
    val isActive: Boolean
) {

    val hasAuthCredentials: Boolean get() = authUsername.isNotBlank() && authPassword.isNotBlank()

    companion object {
        val default = ConnectionEntity(
            id = UUID.randomUUID(),
            name = "Default",
            protocol = URLProtocol.HTTP,
            host = "pi.hole",
            port = 80,
            password = "",
            apiPath = "/api",
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
