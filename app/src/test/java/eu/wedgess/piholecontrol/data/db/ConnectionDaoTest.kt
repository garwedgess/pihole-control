package eu.wedgess.piholecontrol.data.db

import TestDispatcherProvider
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.Connection
import eu.wedgess.piholecontrol.data.PiHoleControlDatabase
import eu.wedgess.piholecontrol.data.mappers.toData
import eu.wedgess.piholecontrol.data.utils.sqldelight.portAdapter
import eu.wedgess.piholecontrol.data.utils.sqldelight.protocolAdapter
import eu.wedgess.piholecontrol.data.utils.sqldelight.uuidAdapter
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.http.URLProtocol
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
class ConnectionDaoTest {

    private lateinit var driver: SqlDriver
    private lateinit var database: PiHoleControlDatabase
    private lateinit var dao: ConnectionDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        driver = AndroidSqliteDriver(
            PiHoleControlDatabase.Schema, context, null
        )
        database = PiHoleControlDatabase(
            driver = driver,
            ConnectionAdapter = Connection.Adapter(
                IdAdapter = uuidAdapter,
                ProtocolAdapter = protocolAdapter,
                PortAdapter = portAdapter
            )
        )
        dao = ConnectionDao(database, TestDispatcherProvider())
    }

    @After
    fun tearDown() {
        driver.close()
    }

    @Test
    fun `insert creates new connection`() {
        // Given a connection
        val connection = ConnectionEntity.default.toData()

        // When inserting the connection
        dao.insert(connection)

        // Then the connection is stored in the database
        val storedConnection = dao.fetchAll().first()

        assertThat(storedConnection.Name).isEqualTo(connection.Name)
        assertThat(storedConnection.Host).isEqualTo(connection.Host)
    }

    @Test
    fun `delete removes connection`() {
        // Given a connection in the database
        val connection = ConnectionEntity.default.toData()
        dao.insert(connection)

        // When deleting the connection
        dao.delete(connection.Id)

        // Then the connection is removed
        val count = dao.fetchAll()
        assertThat(count).isEmpty()
    }

    @Test
    fun `update modifies existing connection`() {
        // Given a connection in the database
        val connection = ConnectionEntity.default.toData()
        dao.insert(connection)

        // When updating the connection
        val updatedConnection = connection.copy(
            Name = "Updated Name",
            Host = "updated.host"
        )
        dao.update(updatedConnection)

        // Then the connection is updated
        val storedConnection = dao.fetchAll().first { it.Id == connection.Id }

        assertThat(storedConnection.Name).isEqualTo("Updated Name")
        assertThat(storedConnection.Host).isEqualTo("updated.host")
    }

    @Test
    fun `setActive marks connection as active`() {
        // Given two connections in the database
        val connection1 = ConnectionEntity.default.toData()
        val connection2 =
            ConnectionEntity.default.copy(id = UUID.randomUUID()).toData()
        dao.insert(connection1)
        dao.insert(connection2)

        // When setting connection2 as active
        dao.setActive(connection2.Id)

        // Then only connection2 is active
        val activeConnections = dao.fetchAll().filter { it.Active }

        assertThat(activeConnections).hasSize(1)
        assertThat(activeConnections.first().Id).isEqualTo(connection2.Id)
    }

    @Test
    fun `markAsDeleted marks connection for deletion`() {
        // Given a connection in the database
        val connection = ConnectionEntity.default.toData()
        dao.insert(connection)

        // When marking for deletion
        dao.markAsDeleted(connection.Id)

        // Then the connection is marked as deleted
        val isDeleted = dao.fetchAll().first { it.Id == connection.Id }.IsDeleted

        assertThat(isDeleted).isTrue()
    }

    @Test
    fun `unmarkAsDeleted unmarks connection for deletion`() {
        // Given a deleted connection
        val connection = ConnectionEntity.default.toData()
        dao.insert(connection)
        dao.markAsDeleted(connection.Id)

        // When unmarking for deletion
        dao.unmarkAsDeleted(connection.Id)

        // Then the connection is not marked as deleted
        val isDeleted = dao.fetchAll().first { it.Id == connection.Id }.IsDeleted

        assertThat(isDeleted).isFalse()
    }

    @Test
    fun `fetchAllAsFlow returns all connections`() = runTest {
        // Given a connection in the database
        val id = UUID.randomUUID()
        insertTestConnection(id)

        // When fetching all connections
        val connections = dao.fetchAllAsFlow().first()

        // Then the connection is returned
        assertThat(connections).hasSize(1)
        assertThat(connections.first().Id).isEqualTo(id)
    }

    @Test
    fun `fetchById returns correct connection`() = runTest {
        // Given a connection in the database
        val id = UUID.randomUUID()
        insertTestConnection(id)

        // When fetching by ID
        val connection = dao.fetchById(id)

        // Then the correct connection is returned
        assertThat(connection.Id).isEqualTo(id)
    }

    @Test
    fun `fetchActive returns active connection`() = runTest {
        // Given an active connection in the database
        val id = UUID.randomUUID()
        insertTestConnection(id, active = true)

        // When fetching active connection
        val connection = dao.fetchActive()

        // Then the active connection is returned
        assertThat(connection?.Id).isEqualTo(id)
        assertThat(connection?.Active).isTrue()
    }

    @Test
    fun `fetchActiveFlow emits active connection`() = runTest {
        // Given an active connection in the database
        val id = UUID.randomUUID()
        insertTestConnection(id, active = true)

        // When observing active connection
        val connection = dao.fetchActiveFlow().first()

        // Then the active connection is emitted
        assertThat(connection?.Id).isEqualTo(id)
        assertThat(connection?.Active).isTrue()
    }

    @Test
    fun `checkNotEmpty returns true when database has connections`() = runTest {
        // Given a connection in the database
        insertTestConnection(UUID.randomUUID())

        // When checking if database is not empty
        val isNotEmpty = dao.checkNotEmpty()

        // Then returns true
        assertThat(isNotEmpty).isTrue()
    }

    @Test
    fun `checkNotEmpty returns false when database is empty`() = runTest {
        // When checking if empty database is not empty
        val isNotEmpty = dao.checkNotEmpty()

        // Then returns false
        assertThat(isNotEmpty).isFalse()
    }

    private fun insertTestConnection(id: UUID, active: Boolean = false) {
        database.connectionQueries.insert(
            Id = id,
            Name = "Test Connection",
            Protocol = URLProtocol.HTTP,
            Host = "localhost",
            Sid = "SessionId",
            Port = 80,
            Password = "test-password",
            ApiPath = "/api",
            AuthUserName = "",
            AuthPassword = "",
            AuthRealm = "",
            TrustAllCerts = false,
            IsDeleted = false,
            Active = active
        )
    }
}
