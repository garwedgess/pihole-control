package eu.wedgess.mihole.data.db

import app.cash.sqldelight.coroutines.asFlow
import eu.wedgess.mihole.MiHoles
import eu.wedgess.mihole.data.MiHoleDatabase
import eu.wedgess.mihole.data.model.MiHolesInfo
import eu.wedgess.mihole.data.toMiHoleInfo
import kotlinx.coroutines.flow.map

class MiHolesDao(db: MiHoleDatabase) {
    private val queries = db.miHolesQueries

    fun fetchAll() = queries.selectAll().asFlow().map { query -> query.executeAsList() }.map { it.map { miHole -> miHole.toMiHoleInfo() } }

    fun fetchById(id: Long) = queries.selectById(id).executeAsOneOrNull()?.toMiHoleInfo()

    fun fetchActive() = queries.selectActive().asFlow()
        .map { query -> query.executeAsOneOrNull() }.map { it?.toMiHoleInfo() ?: MiHolesInfo.default }

    fun delete(id: Long) = queries.delete(id)

    fun insert(miHole: MiHoles) = with(miHole) {
        queries.insert(
            Id = null,
            Name = Name,
            Protocol = Protocol,
            Host = Host,
            ApiPath = ApiPath,
            Port = Port,
            Token = Token,
            AuthUserName = AuthUserName,
            AuthPassword = AuthPassword,
            AuthRealm = AuthRealm,
            TrustAllCerts = TrustAllCerts,
            Active = true
        )
    }

    fun setActive(id: Long) =
        queries.updateAsActive(id)

    fun update(miHole: MiHoles) = with(miHole) {
        queries.update(
            id = Id,
            name = Name,
            protocol = Protocol,
            host = Host,
            path = ApiPath,
            port = Port,
            token = Token,
            username = AuthUserName,
            password = AuthPassword,
            trustAllCerts = TrustAllCerts,
            realm = AuthRealm
        )
    }
}