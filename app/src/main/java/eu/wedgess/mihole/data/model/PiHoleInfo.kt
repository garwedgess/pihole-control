package eu.wedgess.mihole.data.model

import io.ktor.http.URLProtocol

data class PiHoleInfo(
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
    val isActive: Boolean
) {
    val hasAuthCredentials: Boolean get() = authUsername.isNotBlank() && authPassword.isNotBlank()

    companion object {
        val default = PiHoleInfo(
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
            isActive = true
        )
    }
}