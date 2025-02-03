package eu.wedgess.piholecontrol.data.db

import TestDispatcherProvider
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.ConnectionVersion5
import eu.wedgess.piholecontrol.ConnectionVersion6
import eu.wedgess.piholecontrol.data.PiHoleControlDatabase
import eu.wedgess.piholecontrol.data.utils.sqldelight.portAdapter
import eu.wedgess.piholecontrol.data.utils.sqldelight.protocolAdapter
import eu.wedgess.piholecontrol.data.utils.sqldelight.uuidAdapter
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
class ConnectionViewDaoTest {

    private lateinit var driver: SqlDriver
    private lateinit var database: PiHoleControlDatabase
    private lateinit var dao: ConnectionViewDao
    private lateinit var dispatcherProvider: TestDispatcherProvider

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
        dispatcherProvider = TestDispatcherProvider()
        dao = ConnectionViewDao(database, dispatcherProvider)
    }

    @After
    fun tearDown() {
        driver.close()
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
        assertThat(connection.Id).isEqualTo(id)
        assertThat(connection.Active).isTrue()
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
        database.connectionVersion5Queries.insert(
            Id = id,
            Name = "Test Connection",
            Protocol = URLProtocol.HTTP,
            Host = "localhost",
            ApiPath = "/admin",
            Port = 80,
            Token = "test-token",
            AuthUserName = "",
            AuthPassword = "",
            AuthRealm = "",
            TrustAllCerts = false,
            IsDeleted = false,
            Active = active
        )
    }
}
