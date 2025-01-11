package eu.wedgess.piholecontrol.data.db

import androidx.annotation.VisibleForTesting
import eu.wedgess.piholecontrol.ConnectionVersion5
import eu.wedgess.piholecontrol.data.PiHoleControlDatabase
import java.util.UUID
import javax.inject.Inject

class ConnectionVersion5Dao @Inject constructor(db: PiHoleControlDatabase) {
    private val queries = db.connectionVersion5Queries

    fun insert(miHole: ConnectionVersion5) = with(miHole) {
        queries.insert(
            Id = miHole.Id,
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

    @VisibleForTesting
    fun fetchAll() = queries.selectAll().executeAsList()

    fun exists(id: UUID) = queries.exists(id).executeAsOne()

    fun setActive(id: UUID) = queries.updateAsActive(id)

    fun update(miHole: ConnectionVersion5) = with(miHole) {
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

    fun markAsDeleted(id: UUID) = queries.markAsDeleted(id)

    fun unmarkAsDeleted(id: UUID) = queries.unmarkAsDeleted(id)

    fun delete(id: UUID) = queries.delete(id)

    fun deleteMarkedForDeletion() = queries.deleteAllMarkedAsDeleted()
}
