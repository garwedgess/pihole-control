package eu.wedgess.piholecontrol.data

import eu.wedgess.piholecontrol.Connection
import eu.wedgess.piholecontrol.data.model.ConnectionInfo

fun Connection.toConnectionInfo() = ConnectionInfo(
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


fun ConnectionInfo.toConnection() = Connection(
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