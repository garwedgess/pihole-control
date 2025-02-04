package eu.wedgess.piholecontrol.data.db

import androidx.annotation.VisibleForTesting
import eu.wedgess.piholecontrol.ConnectionVersion6
import eu.wedgess.piholecontrol.data.PiHoleControlDatabase
import java.util.UUID
import javax.inject.Inject

class ConnectionVersion6Dao @Inject constructor(db: PiHoleControlDatabase) {
    private val queries = db.connectionVersion6Queries

    fun insert(miHole: ConnectionVersion6) = with(miHole) {
        queries.insert(
            Id = Id,
            Name = Name,
            Protocol = Protocol,
            Host = Host,
            Port = Port,
            Password = Password,
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

    fun exists(id: UUID) = queries.exists(id).executeAsOne()

    fun setActive(id: UUID) = queries.updateAsActive(id)

    fun update(miHole: ConnectionVersion6) = with(miHole) {
        queries.update(
            id = Id,
            name = Name,
            protocol = Protocol,
            host = Host,
            port = Port,
            password = Password,
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
