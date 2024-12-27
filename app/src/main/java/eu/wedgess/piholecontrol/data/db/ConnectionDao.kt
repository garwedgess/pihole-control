package eu.wedgess.piholecontrol.data.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import eu.wedgess.piholecontrol.Connection
import eu.wedgess.piholecontrol.data.PiHoleControlDatabase
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import javax.inject.Inject

class ConnectionDao @Inject constructor(
    db: PiHoleControlDatabase,
    private val dispatcherProvider: DispatcherProvider
) {
    private val queries = db.connectionQueries

    fun fetchAllAsFlow() = queries.selectAll().asFlow().mapToList(dispatcherProvider.io)

    fun fetchById(id: Long) = queries.selectById(id).executeAsOne()

    fun fetchActive() = queries.selectActive().executeAsOne()

    fun fetchActiveFlow() = queries.selectActive().asFlow()
        .mapToOneOrNull(dispatcherProvider.io)

    fun checkNotEmpty(): Boolean = queries.checkNotEmpty().executeAsOne()

    fun delete(id: Long) = queries.delete(id)

    fun deleteMarkedForDeletion() = queries.deleteAllMarkedAsDeleted()

    fun insert(miHole: Connection) = with(miHole) {
        queries.insert(
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
            IsDeleted = false,
            Active = true
        )
    }

    fun setActive(id: Long) = queries.updateAsActive(id)

    fun update(miHole: Connection) = with(miHole) {
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

    fun markAsDeleted(id: Long) = queries.markAsDeleted(id)

    fun unmarkAsDeleted(id: Long) = queries.unmarkAsDeleted(id)
}
