package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.Connection
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import java.io.File

internal fun loadJson(directory: String, fileName: String): String {
    return File("src/test/resources/json/$directory/$fileName").readText(Charsets.UTF_8)
}

fun ConnectionEntity.toConnection() = Connection(
    Id = this.id,
    Name = this.name,
    Protocol = this.protocol,
    Host = this.host,
    Port = this.port,
    Password = this.password,
    ApiPath = this.apiPath,
    Sid = this.sid,
    AuthUserName = this.authUsername,
    AuthPassword = this.authPassword,
    AuthRealm = this.authRealm,
    TrustAllCerts = this.trustAllCerts,
    IsDeleted = this.isDeleted,
    Active = this.isActive
)
