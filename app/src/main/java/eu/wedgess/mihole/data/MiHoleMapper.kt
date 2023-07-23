package eu.wedgess.mihole.data

import eu.wedgess.mihole.MiHoles
import eu.wedgess.mihole.data.model.MiHolesInfo

fun MiHoles.toMiHoleInfo() = MiHolesInfo(
    id = Id,
    name = Name,
    protocol = Protocol,
    host = Host,
    port = Port,
    apiPath = ApiPath,
    token = Token,
    authUsername = AuthUserName,
    authPassword = AuthPassword,
    authRealm = AuthRealm,
    trustAllCerts = TrustAllCerts,
    isActive = Active
)


fun MiHolesInfo.toMiHole() = MiHoles(
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