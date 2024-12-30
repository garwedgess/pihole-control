package eu.wedgess.piholecontrol.domain.model

import io.ktor.http.URLProtocol

data class ConnectionEntity(
    val id: Long,
    val name: String,
    val protocol: URLProtocol,
    val host: String,
    val port: Int,
    val apiPath: String,
    val token: String,
    val authUsername: String,
    val authPassword: String,
    val authRealm: String,
    val trustAllCerts: Boolean,
    val apiVersion: PiHoleApiVersionEntity,
    val isDeleted: Boolean,
    val isActive: Boolean
) {
    val hasAuthCredentials: Boolean get() = authUsername.isNotBlank() && authPassword.isNotBlank()

    companion object {
        val default = ConnectionEntity(
            id = -1,
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
            apiVersion = PiHoleApiVersionEntity.Version5,
            isDeleted = false,
            isActive = true
        )
    }
}
