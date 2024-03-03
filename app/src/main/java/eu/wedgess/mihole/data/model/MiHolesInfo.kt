package eu.wedgess.mihole.data.model

import io.ktor.http.URLProtocol

data class MiHolesInfo(
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
    companion object {
        val default = MiHolesInfo(
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