package eu.wedgess.piholecontrol.data.db

import TestDispatcherProvider
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.Connection
import eu.wedgess.piholecontrol.data.PiHoleControlDatabase
import eu.wedgess.piholecontrol.data.model.enums.PiHoleApiVersionData
import eu.wedgess.piholecontrol.data.utils.sqldelight.apiVersionAdapter
import eu.wedgess.piholecontrol.data.utils.sqldelight.portAdapter
import eu.wedgess.piholecontrol.data.utils.sqldelight.protocolAdapter
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import io.ktor.http.URLProtocol
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ConnectionDaoTest {

    private lateinit var driver: SqlDriver
    private lateinit var database: PiHoleControlDatabase
    private lateinit var dao: ConnectionDao
    private lateinit var dispatcherProvider: DispatcherProvider
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
            ConnectionAdapter = Connection.Adapter(
                ProtocolAdapter = protocolAdapter,
                PortAdapter = portAdapter,
                ApiVersionAdapter = apiVersionAdapter
            )
        )
        dispatcherProvider = TestDispatcherProvider()
        dao = ConnectionDao(database, dispatcherProvider)
    }

    @After
    fun tearDown() {
        driver.close()
    }

    @Test
    fun `insert and fetchAllAsFlow - inserts and retrieves connections correctly`() = runTest {
        val connection1 = Connection(
            Id = 1,
            Name = "Test",
            Protocol = URLProtocol.HTTP,
            Host = "localhost",
            ApiPath = "/admin",
            Port = 80,
            Token = "token",
            AuthUserName = "",
            AuthPassword = "",
            AuthRealm = "",
            TrustAllCerts = false,
            ApiVersion = PiHoleApiVersionData.Version5,
            IsDeleted = false,
            Active = false
        )
        val connection2 = Connection(
            Id = 2,
            Name = "Test2",
            Protocol = URLProtocol.HTTP,
            Host = "192.168.1.1",
            ApiPath = "/admin",
            Port = 443,
            Token = "token2",
            AuthUserName = "",
            AuthPassword = "",
            AuthRealm = "",
            TrustAllCerts = true,
            ApiVersion = PiHoleApiVersionData.Version5,
            IsDeleted = false,
            Active = true
        )

        dao.insert(connection1)
        dao.insert(connection2)

        val connections = dao.fetchAllAsFlow().first()
        assertThat(connections).hasSize(2)
        assertThat(connections).containsExactly(connection1, connection2)
    }

    @Test
    fun `fetchById - retrieves connection by ID correctly`() = runTest {
        val connection = Connection(
            Id = 1,
            Name = "Test",
            Protocol = URLProtocol.HTTP,
            Host = "localhost",
            ApiPath = "/admin",
            Port = 80,
            Token = "token",
            AuthUserName = "",
            AuthPassword = "",
            AuthRealm = "",
            TrustAllCerts = false,
            ApiVersion = PiHoleApiVersionData.Version5,
            IsDeleted = false,
            Active = false
        )
        dao.insert(connection)

        val fetchedConnection = dao.fetchById(1)

        assertThat(fetchedConnection).isEqualTo(connection.copy(Active = true))
    }

    @Test
    fun `fetchActive - retrieves active connection correctly`() = runTest {
        val connection1 = Connection(
            Id = 1,
            Name = "Test",
            Protocol = URLProtocol.HTTP,
            Host = "localhost",
            ApiPath = "/admin",
            Port = 80,
            Token = "token",
            AuthUserName = "",
            AuthPassword = "",
            AuthRealm = "",
            TrustAllCerts = false,
            ApiVersion = PiHoleApiVersionData.Version5,
            IsDeleted = false,
            Active = false
        )
        val connection2 = Connection(
            Id = 2,
            Name = "Test2",
            Protocol = URLProtocol.HTTP,
            Host = "192.168.1.1",
            ApiPath = "/admin",
            Port = 443,
            Token = "token2",
            AuthUserName = "",
            AuthPassword = "",
            AuthRealm = "",
            TrustAllCerts = true,
            ApiVersion = PiHoleApiVersionData.Version5,
            IsDeleted = false,
            Active = true
        )
        dao.insert(connection1)
        dao.insert(connection2)

        val activeConnection = dao.fetchActive()

        assertThat(activeConnection).isEqualTo(connection2)
    }

    @Test
    fun `fetchActiveFlow - retrieves active connection flow correctly`() = runTest {
        val connection1 = Connection(
            Id = 1,
            Name = "Test",
            Protocol = URLProtocol.HTTP,
            Host = "localhost",
            ApiPath = "/admin",
            Port = 80,
            Token = "token",
            AuthUserName = "",
            AuthPassword = "",
            AuthRealm = "",
            TrustAllCerts = false,
            ApiVersion = PiHoleApiVersionData.Version5,
            IsDeleted = false,
            Active = false
        )
        val connection2 = Connection(
            Id = 2,
            Name = "Test2",
            Protocol = URLProtocol.HTTP,
            Host = "192.168.1.1",
            ApiPath = "/admin",
            Port = 443,
            Token = "token2",
            AuthUserName = "",
            AuthPassword = "",
            AuthRealm = "",
            TrustAllCerts = true,
            ApiVersion = PiHoleApiVersionData.Version5,
            IsDeleted = false,
            Active = true
        )
        dao.insert(connection1)
        dao.insert(connection2)

        val activeConnection = dao.fetchActiveFlow().first()

        assertThat(activeConnection).isEqualTo(connection2)
    }

    @Test
    fun `checkNotEmpty - returns true when database is not empty`() = runTest {
        val connection = Connection(
            Id = 1,
            Name = "Test",
            Protocol = URLProtocol.HTTP,
            Host = "localhost",
            ApiPath = "/admin",
            Port = 80,
            Token = "token",
            AuthUserName = "",
            AuthPassword = "",
            AuthRealm = "",
            TrustAllCerts = false,
            ApiVersion = PiHoleApiVersionData.Version5,
            IsDeleted = false,
            Active = false
        )
        dao.insert(connection)

        val isNotEmpty = dao.checkNotEmpty()

        assertThat(isNotEmpty).isTrue()
    }

    @Test
    fun `checkNotEmpty - returns false when database is empty`() = runTest {
        val isNotEmpty = dao.checkNotEmpty()

        assertThat(isNotEmpty).isFalse()
    }

    @Test
    fun `delete - deletes connection correctly`() = runTest {
        val connection = Connection(
            Id = 1,
            Name = "Test",
            Protocol = URLProtocol.HTTP,
            Host = "localhost",
            ApiPath = "/admin",
            Port = 80,
            Token = "token",
            AuthUserName = "",
            AuthPassword = "",
            AuthRealm = "",
            TrustAllCerts = false,
            ApiVersion = PiHoleApiVersionData.Version5,
            IsDeleted = false,
            Active = false
        )
        dao.insert(connection)

        dao.delete(1)

        val connections = dao.fetchAllAsFlow().first()
        assertThat(connections).isEmpty()
    }

    @Test
    fun `deleteMarkedForDeletion - deletes marked connections correctly`() = runTest {
        val connection1 = Connection(
            Id = 1,
            Name = "Test",
            Protocol = URLProtocol.HTTP,
            Host = "localhost",
            ApiPath = "/admin",
            Port = 80,
            Token = "token",
            AuthUserName = "",
            AuthPassword = "",
            AuthRealm = "",
            TrustAllCerts = false,
            ApiVersion = PiHoleApiVersionData.Version5,
            IsDeleted = false,
            Active = false
        )
        val connection2 = Connection(
            Id = 2,
            Name = "Test2",
            Protocol = URLProtocol.HTTP,
            Host = "192.168.1.1",
            ApiPath = "/admin",
            Port = 443,
            Token = "token2",
            AuthUserName = "",
            AuthPassword = "",
            AuthRealm = "",
            TrustAllCerts = true,
            ApiVersion = PiHoleApiVersionData.Version5,
            IsDeleted = false,
            Active = true
        )
        dao.insert(connection1)
        dao.insert(connection2)
        dao.markAsDeleted(1)

        dao.deleteMarkedForDeletion()

        val connections = dao.fetchAllAsFlow().first()
        assertThat(connections).hasSize(1)
        assertThat(connections).containsExactly(connection2)
    }

    @Test
    fun `setActive - sets connection as active correctly`() = runTest {
        val connection1 = Connection(
            Id = 1,
            Name = "Test",
            Protocol = URLProtocol.HTTP,
            Host = "localhost",
            ApiPath = "/admin",
            Port = 80,
            Token = "token",
            AuthUserName = "",
            AuthPassword = "",
            AuthRealm = "",
            TrustAllCerts = false,
            ApiVersion = PiHoleApiVersionData.Version5,
            IsDeleted = false,
            Active = false
        )
        val connection2 = Connection(
            Id = 2,
            Name = "Test2",
            Protocol = URLProtocol.HTTP,
            Host = "192.168.1.1",
            ApiPath = "/admin",
            Port = 443,
            Token = "token2",
            AuthUserName = "",
            AuthPassword = "",
            AuthRealm = "",
            TrustAllCerts = true,
            ApiVersion = PiHoleApiVersionData.Version5,
            IsDeleted = false,
            Active = true
        )
        dao.insert(connection1)
        dao.insert(connection2)

        dao.setActive(1)

        val activeConnection = dao.fetchActive()
        assertThat(activeConnection).isEqualTo(connection1.copy(Active = true))
    }

    @Test
    fun `update - updates connection correctly`() = runTest {
        val connection = Connection(
            Id = 1,
            Name = "Test",
            Protocol = URLProtocol.HTTP,
            Host = "localhost",
            ApiPath = "/admin",
            Port = 80,
            Token = "token",
            AuthUserName = "",
            AuthPassword = "",
            AuthRealm = "",
            TrustAllCerts = false,
            ApiVersion = PiHoleApiVersionData.Version5,
            IsDeleted = false,
            Active = false
        )
        dao.insert(connection)

        val updatedConnection = connection.copy(Name = "Updated Test", Port = 8080)
        dao.update(updatedConnection)

        val fetchedConnection = dao.fetchById(1)
        assertThat(fetchedConnection).isEqualTo(updatedConnection.copy(Active = true))
    }

    @Test
    fun `markAsDeleted - marks connection as deleted correctly`() = runTest {
        val connection = Connection(
            Id = 1,
            Name = "Test",
            Protocol = URLProtocol.HTTP,
            Host = "localhost",
            ApiPath = "/admin",
            Port = 80,
            Token = "token",
            AuthUserName = "",
            AuthPassword = "",
            AuthRealm = "",
            TrustAllCerts = false,
            ApiVersion = PiHoleApiVersionData.Version5,
            IsDeleted = false,
            Active = false
        )
        dao.insert(connection)

        dao.markAsDeleted(1)

        val fetchedConnection = dao.fetchById(1)
        assertThat(fetchedConnection.IsDeleted).isTrue()
    }

    @Test
    fun `unmarkAsDeleted - unmarks connection as deleted correctly`() = runTest {
        val connection = Connection(
            Id = 1,
            Name = "Test",
            Protocol = URLProtocol.HTTP,
            Host = "localhost",
            ApiPath = "/admin",
            Port = 80,
            Token = "token",
            AuthUserName = "",
            AuthPassword = "",
            AuthRealm = "",
            TrustAllCerts = false,
            ApiVersion = PiHoleApiVersionData.Version5,
            IsDeleted = false,
            Active = false
        )
        dao.insert(connection)

        dao.unmarkAsDeleted(1)

        val fetchedConnection = dao.fetchById(1)
        assertThat(fetchedConnection.IsDeleted).isFalse()
    }
}
