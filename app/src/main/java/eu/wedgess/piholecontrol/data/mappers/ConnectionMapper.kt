package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.Connection
import eu.wedgess.piholecontrol.ConnectionVersion5
import eu.wedgess.piholecontrol.ConnectionVersion6
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.presentation.connections.modify.model.PiHoleApiVersion

fun Connection.toEntity() = when (PiHoleApiVersion[this.ApiVersion]) {
    PiHoleApiVersion.Version5 -> this.toEntityVersion5()
    PiHoleApiVersion.Version6 -> this.toEntityVersion6()
}

fun Connection.toEntityVersion5() = ConnectionEntity.Version5(
    id = Id,
    name = Name,
    protocol = Protocol,
    host = Host,
    port = Port,
    apiPath = ApiPath ?: ConnectionEntity.Version5.default.apiPath,
    token = Token ?: "",
    authUsername = AuthUserName,
    authPassword = AuthPassword,
    authRealm = AuthRealm,
    trustAllCerts = TrustAllCerts,
    isDeleted = IsDeleted,
    isActive = Active
)

fun Connection.toEntityVersion6() = ConnectionEntity.Version6(
    id = Id,
    name = Name,
    protocol = Protocol,
    host = Host,
    port = Port,
    password = Password ?: "",
    sid = Sid ?: "",
    authUsername = AuthUserName,
    authPassword = AuthPassword,
    authRealm = AuthRealm,
    trustAllCerts = TrustAllCerts,
    isDeleted = IsDeleted,
    isActive = Active
)

fun ConnectionEntity.Version5.toVersion5() = ConnectionVersion5(
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
    IsDeleted = isDeleted,
    Active = isActive
)

fun ConnectionEntity.Version6.toVersion6() = ConnectionVersion6(
    Id = id,
    Name = name,
    Protocol = protocol,
    Host = host,
    Port = port,
    Password = password,
    Sid = sid,
    AuthUserName = authUsername,
    AuthPassword = authPassword,
    AuthRealm = authRealm,
    TrustAllCerts = trustAllCerts,
    IsDeleted = isDeleted,
    Active = isActive
)
