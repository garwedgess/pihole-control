package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.Connection
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

fun Connection.toEntity() = ConnectionEntity(
    id = Id,
    name = Name,
    protocol = Protocol,
    host = Host,
    port = Port,
    password = Password,
    apiPath = ApiPath,
    sid = Sid,
    authUsername = AuthUserName,
    authPassword = AuthPassword,
    authRealm = AuthRealm,
    trustAllCerts = TrustAllCerts,
    isDeleted = IsDeleted,
    isActive = Active
)

fun ConnectionEntity.toData() = Connection(
    Id = id,
    Name = name,
    Protocol = protocol,
    Host = host,
    Port = port,
    Password = password,
    ApiPath = apiPath,
    Sid = sid,
    AuthUserName = authUsername,
    AuthPassword = authPassword,
    AuthRealm = authRealm,
    TrustAllCerts = trustAllCerts,
    IsDeleted = isDeleted,
    Active = isActive
)
