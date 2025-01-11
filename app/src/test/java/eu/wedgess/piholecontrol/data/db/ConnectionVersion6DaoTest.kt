package eu.wedgess.piholecontrol.data.db

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.ConnectionVersion5
import eu.wedgess.piholecontrol.ConnectionVersion6
import eu.wedgess.piholecontrol.data.PiHoleControlDatabase
import eu.wedgess.piholecontrol.data.mappers.toVersion6
import eu.wedgess.piholecontrol.data.utils.sqldelight.portAdapter
import eu.wedgess.piholecontrol.data.utils.sqldelight.protocolAdapter
import eu.wedgess.piholecontrol.data.utils.sqldelight.uuidAdapter
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
class ConnectionVersion6DaoTest {

    private lateinit var driver: SqlDriver
    private lateinit var database: PiHoleControlDatabase
    private lateinit var dao: ConnectionVersion6Dao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        driver = AndroidSqliteDriver(
            PiHoleControlDatabase.Schema, context, null
        )
        database = PiHoleControlDatabase(
            driver = driver,
            ConnectionVersion5Adapter = ConnectionVersion5.Adapter(
                IdAdapter = uuidAdapter,
                ProtocolAdapter = protocolAdapter,
                PortAdapter = portAdapter
            ),
            ConnectionVersion6Adapter = ConnectionVersion6.Adapter(
                IdAdapter = uuidAdapter,
                ProtocolAdapter = protocolAdapter,
                PortAdapter = portAdapter
            )
        )
        dao = ConnectionVersion6Dao(database)
    }

    @After
    fun tearDown() {
        driver.close()
    }

    @Test
    fun `insert creates new connection`() {
        // Given a connection
        val connection = ConnectionEntity.Version6.default.toVersion6()

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
        val connection = ConnectionEntity.Version6.default.toVersion6()
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
        val connection = ConnectionEntity.Version6.default.toVersion6()
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
        val connection1 = ConnectionEntity.Version6.default.toVersion6()
        val connection2 =
            ConnectionEntity.Version6.default.copy(id = UUID.randomUUID()).toVersion6()
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
        val connection = ConnectionEntity.Version6.default.toVersion6()
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
        val connection = ConnectionEntity.Version6.default.toVersion6()
        dao.insert(connection)
        dao.markAsDeleted(connection.Id)

        // When unmarking for deletion
        dao.unmarkAsDeleted(connection.Id)

        // Then the connection is not marked as deleted
        val isDeleted = dao.fetchAll().first { it.Id == connection.Id }.IsDeleted

        assertThat(isDeleted).isFalse()
    }
}
