package eu.wedgess.piholecontrol.domain.mappers

import eu.wedgess.piholecontrol.Connection
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

fun Connection.toConnectionInfo() = ConnectionEntity(
    id = Id,
    name = Name,
    protocol = Protocol,
    host = Host,
    port = Port,
    apiPath = ApiPath,
    token = Token ?: "",
    authUsername = AuthUserName,
    authPassword = AuthPassword,
    authRealm = AuthRealm,
    trustAllCerts = TrustAllCerts,
    isActive = Active
)

fun ConnectionEntity.toConnection() = Connection(
    Id = id,
    Name = name,
    Protocol = protocol,
    Host = host,
    Port = port,
    ApiPath = apiPath,
    Token = token,
    AuthUserName = authUsername,
    AuthPassword = authPassword,
    AuthRealm = authRealm,
    TrustAllCerts = trustAllCerts,
    Active = isActive
)
