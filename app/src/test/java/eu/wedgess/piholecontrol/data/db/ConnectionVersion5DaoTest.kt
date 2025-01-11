package eu.wedgess.piholecontrol.data.db

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.ConnectionVersion5
import eu.wedgess.piholecontrol.ConnectionVersion6
import eu.wedgess.piholecontrol.data.PiHoleControlDatabase
import eu.wedgess.piholecontrol.data.mappers.toVersion5
import eu.wedgess.piholecontrol.data.utils.sqldelight.portAdapter
import eu.wedgess.piholecontrol.data.utils.sqldelight.protocolAdapter
import eu.wedgess.piholecontrol.data.utils.sqldelight.uuidAdapter
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
class ConnectionVersion5DaoTest {

    private lateinit var driver: SqlDriver
    private lateinit var database: PiHoleControlDatabase
    private lateinit var dao: ConnectionVersion5Dao
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        driver = AndroidSqliteDriver(
            PiHoleControlDatabase.Schema,
            context,
            null
        ) // Use null for in-memory
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
        dao = ConnectionVersion5Dao(database)
    }

    @After
    fun tearDown() {
        driver.close()
    }

    @Test
    fun `insert - inserts connections correctly`() = runTest {
        val connection1 = ConnectionEntity.Version5.default.toVersion5()
        val connection2 =
            ConnectionEntity.Version5.default.copy(id = UUID.randomUUID()).toVersion5()

        dao.insert(connection1)
        dao.insert(connection2)

        val connections = dao.fetchAll()
        assertThat(connections).hasSize(2)
        assertThat(connections).containsExactly(connection1.copy(Active = false), connection2)
    }

    @Test
    fun `delete - deletes connection correctly`() = runTest {
        val connection = ConnectionEntity.Version5.default.toVersion5()
        dao.insert(connection)

        dao.delete(connection.Id)

        val connections = dao.fetchAll()
        assertThat(connections).isEmpty()
    }

    @Test
    fun `deleteMarkedForDeletion - deletes marked connections correctly`() = runTest {
        val connection1 = ConnectionEntity.Version5.default.toVersion5()
        val connection2 =
            ConnectionEntity.Version5.default.copy(id = UUID.randomUUID()).toVersion5()
        dao.insert(connection1)
        dao.insert(connection2)
        dao.markAsDeleted(connection1.Id)

        dao.deleteMarkedForDeletion()

        val connections = dao.fetchAll()
        assertThat(connections).hasSize(1)
        assertThat(connections).containsExactly(connection2)
    }

    @Test
    fun `setActive - sets connection as active correctly`() = runTest {
        val connection1 = ConnectionEntity.Version5.default.toVersion5()
        val connection2 =
            ConnectionEntity.Version5.default.copy(id = UUID.randomUUID()).toVersion5()
        dao.insert(connection1)
        dao.insert(connection2)

        dao.setActive(connection1.Id)

        val activeConnection = dao.fetchAll().first { it.Active }
        assertThat(activeConnection).isEqualTo(connection1.copy(Active = true))
    }

    @Test
    fun `update - updates connection correctly`() = runTest {
        val connection = ConnectionEntity.Version5.default.toVersion5()
        dao.insert(connection)

        val updatedConnection = connection.copy(Name = "Updated Test", Port = 8080)
        dao.update(updatedConnection)

        val fetchedConnection = dao.fetchAll().first()
        assertThat(fetchedConnection).isEqualTo(updatedConnection.copy(Active = true))
    }

    @Test
    fun `markAsDeleted - marks connection as deleted correctly`() = runTest {
        val connection = ConnectionEntity.Version5.default.toVersion5()
        dao.insert(connection)

        dao.markAsDeleted(connection.Id)

        val fetchedConnection = dao.fetchAll().first()
        assertThat(fetchedConnection.IsDeleted).isTrue()
    }

    @Test
    fun `unmarkAsDeleted - unmarks connection as deleted correctly`() = runTest {
        val connection = ConnectionEntity.Version5.default.toVersion5()
        dao.insert(connection)

        dao.unmarkAsDeleted(connection.Id)

        val fetchedConnection = dao.fetchAll().first()
        assertThat(fetchedConnection.IsDeleted).isFalse()
    }
}
