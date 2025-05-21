package eu.wedgess.piholecontrol.data.db

import androidx.annotation.VisibleForTesting
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import eu.wedgess.piholecontrol.Connection
import eu.wedgess.piholecontrol.data.PiHoleControlDatabase
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import java.util.UUID
import javax.inject.Inject

class ConnectionDao @Inject constructor(
    db: PiHoleControlDatabase,
    private val dispatcherProvider: DispatcherProvider
) {
    private val queries = db.connectionQueries

    fun insert(miHole: Connection) = with(miHole) {
        queries.insert(
            Id = Id,
            Name = Name,
            Protocol = Protocol,
            Host = Host,
            Port = Port,
            Password = Password,
            ApiPath = ApiPath,
            Sid = Sid,
            AuthUserName = AuthUserName,
            AuthPassword = AuthPassword,
            AuthRealm = AuthRealm,
            TrustAllCerts = TrustAllCerts,
            IsDeleted = false,
            Active = true
        )
    }

    @VisibleForTesting
    fun fetchAll() = queries.selectAll().executeAsList()

    fun fetchAllAsFlow() = queries.selectAll().asFlow().mapToList(dispatcherProvider.io)

    fun fetchById(id: UUID) = queries.selectById(id).executeAsOne()

    fun fetchActive() = queries.selectActive().executeAsOneOrNull()

    fun fetchActiveFlow() = queries.selectActive().asFlow().mapToOneOrNull(dispatcherProvider.io)

    fun checkNotEmpty(): Boolean = queries.checkNotEmpty().executeAsOne()

    fun exists(id: UUID) = queries.exists(id).executeAsOne()

    fun setActive(id: UUID) = queries.updateAsActive(id)

    fun update(miHole: Connection) = with(miHole) {
        queries.update(
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
            trustAllCerts = TrustAllCerts,
            realm = AuthRealm
        )
    }

    fun markAsDeleted(id: UUID) = queries.markAsDeleted(id)

    fun unmarkAsDeleted(id: UUID) = queries.unmarkAsDeleted(id)

    fun delete(id: UUID) = queries.delete(id)

    fun deleteMarkedForDeletion() = queries.deleteAllMarkedAsDeleted()
}
