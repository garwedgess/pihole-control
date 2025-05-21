package eu.wedgess.piholecontrol.data.repository

import TestDispatcherProvider
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.toConnection
import eu.wedgess.piholecontrol.data.db.ConnectionDao
import eu.wedgess.piholecontrol.data.mappers.toData
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.UUID

class ConnectionRepositoryImplTest {

    @MockK
    private lateinit var connectionVersion6Dao: ConnectionDao

    private lateinit var dispatcherProvider: TestDispatcherProvider
    private lateinit var target: ConnectionRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dispatcherProvider = TestDispatcherProvider()
        target = spyk(
            ConnectionRepositoryImpl(
                connectionDao = connectionVersion6Dao,
                dispatcherProvider = dispatcherProvider
            )
        )
    }

    @Test
    fun `insert connection should succeed when dao operation is successful`() = runTest {
        val connectionEntity = ConnectionEntity.default
        val connectionData = ConnectionEntity.default.toData()
        coEvery { connectionVersion6Dao.insert(connectionData) } returns Unit

        val result = target.insert(connectionEntity)

        assertThat(result.isSuccess).isTrue()
        verify { connectionVersion6Dao.insert(connectionData) }
    }

    @Test
    fun `insert connection should return failure when dao operation throws exception`() =
        runTest {
            val connectionEntity = ConnectionEntity.default
            val connectionData = connectionEntity.toData()
            coEvery { connectionVersion6Dao.insert(connectionData) } throws RuntimeException(
                "Insertion failed"
            )

            val result = target.insert(connectionEntity)

            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
            verify { connectionVersion6Dao.insert(connectionData) }
        }

    @Test
    fun `checkHasConnections should return true when database has connections`() = runTest {
        every { connectionVersion6Dao.checkNotEmpty() } returns true

        val result = target.checkHasConnections()

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isTrue()
        verify { connectionVersion6Dao.checkNotEmpty() }
    }

    @Test
    fun `checkHasConnections should return failure when dao operation throws exception`() =
        runTest {
            coEvery { connectionVersion6Dao.checkNotEmpty() } throws RuntimeException("Check failed")

            val result = target.checkHasConnections()

            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
            verify { connectionVersion6Dao.checkNotEmpty() }
        }

    @Test
    fun `fetchAll should emit list of connections when dao operation succeeds`() = runTest {
        val connections =
            listOf(ConnectionEntity.default, ConnectionEntity.default)
        every { connectionVersion6Dao.fetchAllAsFlow() } returns flowOf(
            connections.map { it.toConnection() }
        )

        target.fetchAll().test {
            val result = awaitItem()
            assertThat(result.isSuccess).isTrue()
            assertThat(result.getOrNull()).isEqualTo(connections)
            awaitComplete()
        }

        verify { connectionVersion6Dao.fetchAllAsFlow() }
    }

    @Test
    fun `fetchActiveFlow should emit active connection when dao operation succeeds`() = runTest {
        val connection = ConnectionEntity.default
        every { connectionVersion6Dao.fetchActiveFlow() } returns flowOf(connection.toConnection())

        target.fetchActiveFlow().test {
            val result = awaitItem()
            assertThat(result.isSuccess).isTrue()
            assertThat(result.getOrNull()).isEqualTo(connection)
            awaitComplete()
        }

        verify { connectionVersion6Dao.fetchActiveFlow() }
    }

    @Test
    fun `fetchById should return connection when dao operation succeeds`() = runTest {
        val connection = ConnectionEntity.default
        coEvery { connectionVersion6Dao.fetchById(any()) } returns connection.toConnection()

        val result = target.fetchById(UUID.randomUUID())

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(connection)
        verify { connectionVersion6Dao.fetchById(any()) }
    }

    @Test
    fun `fetchById should return failure when dao operation throws exception`() = runTest {
        coEvery { connectionVersion6Dao.fetchById(any()) } throws RuntimeException("Fetch failed")

        val result = target.fetchById(UUID.randomUUID())

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        verify { connectionVersion6Dao.fetchById(any()) }
    }

    @Test
    fun `update - success`() = runTest {
        val connection = ConnectionEntity.default
        coEvery { connectionVersion6Dao.update(any()) } returns Unit

        val result = target.update(connection)

        assertThat(result.isSuccess).isTrue()
        verify { connectionVersion6Dao.update(connection.toData()) }
    }

    @Test
    fun `update - failure`() = runTest {
        val connection = ConnectionEntity.default
        coEvery { connectionVersion6Dao.update(any()) } throws RuntimeException("Update failed")

        val result = target.update(connection)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        coVerify { connectionVersion6Dao.update(connection.toData()) }
    }

    @Test
    fun `deleteById - success`() = runTest {
        coEvery { connectionVersion6Dao.exists(any()) } returns true
        coEvery { connectionVersion6Dao.delete(any()) } returns Unit

        val result = target.deleteById(UUID.randomUUID())

        assertThat(result.isSuccess).isTrue()
        verify { connectionVersion6Dao.delete(any()) }
    }

    @Test
    fun `deleteById - failure`() = runTest {
        coEvery { connectionVersion6Dao.exists(any()) } returns true
        coEvery { connectionVersion6Dao.delete(any()) } throws RuntimeException("Deletion failed")

        val result = target.deleteById(UUID.randomUUID())

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        verify { connectionVersion6Dao.delete(any()) }
    }

    @Test
    fun `deleteAllMarkedForDeletion - success`() = runTest {
        coEvery { connectionVersion6Dao.deleteMarkedForDeletion() } returns Unit

        val result = target.deleteAllMarkedForDeletion()

        assertThat(result.isSuccess).isTrue()
        verify { connectionVersion6Dao.deleteMarkedForDeletion() }
    }

    @Test
    fun `deleteAllMarkedForDeletion - failure`() = runTest {
        coEvery { connectionVersion6Dao.deleteMarkedForDeletion() } throws RuntimeException("Deletion failed")

        val result = target.deleteAllMarkedForDeletion()

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        verify { connectionVersion6Dao.deleteMarkedForDeletion() }
    }

    @Test
    fun `fetchActive - success`() = runTest {
        val connection = ConnectionEntity.default
        coEvery { connectionVersion6Dao.fetchActive() } returns connection.toConnection()

        val result = target.fetchActive()

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(connection)
        verify { connectionVersion6Dao.fetchActive() }
    }

    @Test
    fun `fetchActive - failure`() = runTest {
        coEvery { connectionVersion6Dao.fetchActive() } throws RuntimeException("Fetch active connection failed")

        val result = target.fetchActive()

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        verify { connectionVersion6Dao.fetchActive() }
    }

    @Test
    fun `setActiveById - success`() = runTest {
        val id = UUID.randomUUID()
        coEvery { connectionVersion6Dao.exists(any()) } returns true
        coEvery { connectionVersion6Dao.setActive(id) } returns Unit

        val result = target.setActiveById(id)

        assertThat(result.isSuccess).isTrue()
        verify { connectionVersion6Dao.setActive(id) }
    }

    @Test
    fun `setActiveById - failure`() = runTest {
        val id = UUID.randomUUID()
        coEvery { connectionVersion6Dao.exists(any()) } returns true
        coEvery { connectionVersion6Dao.setActive(id) } throws RuntimeException("Set active connection failed")

        val result = target.setActiveById(id)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        verify { connectionVersion6Dao.setActive(id) }
    }

    @Test
    fun `markForDeletion - success`() = runTest {
        val id = UUID.randomUUID()
        coEvery { connectionVersion6Dao.exists(id) } returns true
        coEvery { connectionVersion6Dao.markAsDeleted(id) } returns Unit

        val result = target.markForDeletion(id)

        assertThat(result.isSuccess).isTrue()
        verify { connectionVersion6Dao.markAsDeleted(id) }
    }

    @Test
    fun `markForDeletion - failure`() = runTest {
        val id = UUID.randomUUID()
        coEvery { connectionVersion6Dao.exists(id) } returns true
        coEvery {
            connectionVersion6Dao.markAsDeleted(id)
        } throws RuntimeException("Marking connection failed")

        val result = target.markForDeletion(id)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        verify { connectionVersion6Dao.markAsDeleted(id) }
    }

    @Test
    fun `unmarkAsDeleted - success`() = runTest {
        val id = UUID.randomUUID()
        coEvery { connectionVersion6Dao.exists(any()) } returns true
        coEvery { connectionVersion6Dao.unmarkAsDeleted(id) } returns Unit

        val result = target.unmarkForDeletion(id)

        assertThat(result.isSuccess).isTrue()
        verify { connectionVersion6Dao.unmarkAsDeleted(id) }
    }

    @Test
    fun `unmarkAsDeleted - failure`() = runTest {
        val id = UUID.randomUUID()
        coEvery { connectionVersion6Dao.exists(any()) } returns true
        coEvery {
            connectionVersion6Dao.unmarkAsDeleted(id)
        } throws RuntimeException("Marking connection failed")

        val result = target.unmarkForDeletion(id)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        verify { connectionVersion6Dao.unmarkAsDeleted(id) }
    }
}
