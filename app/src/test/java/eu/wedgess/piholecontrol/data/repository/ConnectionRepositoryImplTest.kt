package eu.wedgess.piholecontrol.data.repository

import TestDispatcherProvider
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.toConnection
import eu.wedgess.piholecontrol.data.db.ConnectionVersion5Dao
import eu.wedgess.piholecontrol.data.db.ConnectionVersion6Dao
import eu.wedgess.piholecontrol.data.db.ConnectionViewDao
import eu.wedgess.piholecontrol.data.mappers.toVersion5
import eu.wedgess.piholecontrol.data.mappers.toVersion6
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
    private lateinit var connectionVersion5Dao: ConnectionVersion5Dao

    @MockK
    private lateinit var connectionVersion6Dao: ConnectionVersion6Dao

    @MockK
    private lateinit var connectionViewDao: ConnectionViewDao
    private lateinit var dispatcherProvider: TestDispatcherProvider
    private lateinit var target: ConnectionRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dispatcherProvider = TestDispatcherProvider()
        target = spyk(
            ConnectionRepositoryImpl(
                connectionVersion5Dao = connectionVersion5Dao,
                connectionVersion6Dao = connectionVersion6Dao,
                connectionViewDao = connectionViewDao,
                dispatcherProvider = dispatcherProvider
            )
        )
    }

    @Test
    fun `insert version 5 connection should succeed when dao operation is successful`() = runTest {
        val connectionEntity = ConnectionEntity.Version5.default
        val connectionData = ConnectionEntity.Version5.default.toVersion5()
        coEvery { connectionVersion5Dao.insert(connectionData) } returns Unit

        val result = target.insert(connectionEntity)

        assertThat(result.isSuccess).isTrue()
        verify { connectionVersion5Dao.insert(connectionData) }
    }

    @Test
    fun `insert version 6 connection should succeed when dao operation is successful`() = runTest {
        val connectionEntity = ConnectionEntity.Version6.default
        val connectionData = ConnectionEntity.Version6.default.toVersion6()
        coEvery { connectionVersion6Dao.insert(connectionData) } returns Unit

        val result = target.insert(connectionEntity)

        assertThat(result.isSuccess).isTrue()
        verify { connectionVersion6Dao.insert(connectionData) }
    }

    @Test
    fun `insert version 5 connection should return failure when dao operation throws exception`() =
        runTest {
            val connectionEntity = ConnectionEntity.Version5.default
            val connectionData = connectionEntity.toVersion5()
            coEvery { connectionVersion5Dao.insert(connectionData) } throws RuntimeException(
                "Insertion failed"
            )

            val result = target.insert(connectionEntity)

            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
            verify { connectionVersion5Dao.insert(connectionData) }
        }

    @Test
    fun `insert version 6 connection should return failure when dao operation throws exception`() =
        runTest {
            val connectionEntity = ConnectionEntity.Version6.default
            val connectionData = connectionEntity.toVersion6()
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
        every { connectionViewDao.checkNotEmpty() } returns true

        val result = target.checkHasConnections()

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isTrue()
        verify { connectionViewDao.checkNotEmpty() }
    }

    @Test
    fun `checkHasConnections should return failure when dao operation throws exception`() =
        runTest {
            coEvery { connectionViewDao.checkNotEmpty() } throws RuntimeException("Check failed")

            val result = target.checkHasConnections()

            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
            verify { connectionViewDao.checkNotEmpty() }
        }

    @Test
    fun `fetchAll should emit list of connections when dao operation succeeds`() = runTest {
        val connections =
            listOf(ConnectionEntity.Version5.default, ConnectionEntity.Version6.default)
        every { connectionViewDao.fetchAllAsFlow() } returns flowOf(
            connections.map { it.toConnection() }
        )

        target.fetchAll().test {
            val result = awaitItem()
            assertThat(result.isSuccess).isTrue()
            assertThat(result.getOrNull()).isEqualTo(connections)
            awaitComplete()
        }

        verify { connectionViewDao.fetchAllAsFlow() }
    }

    @Test
    fun `fetchActiveFlow should emit active connection when dao operation succeeds`() = runTest {
        val connection = ConnectionEntity.Version5.default
        every { connectionViewDao.fetchActiveFlow() } returns flowOf(connection.toConnection())

        target.fetchActiveFlow().test {
            val result = awaitItem()
            assertThat(result.isSuccess).isTrue()
            assertThat(result.getOrNull()).isEqualTo(connection)
            awaitComplete()
        }

        verify { connectionViewDao.fetchActiveFlow() }
    }

    @Test
    fun `fetchById should return connection when dao operation succeeds`() = runTest {
        val connection = ConnectionEntity.Version6.default
        coEvery { connectionViewDao.fetchById(any()) } returns connection.toConnection()

        val result = target.fetchById(UUID.randomUUID())

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(connection)
        verify { connectionViewDao.fetchById(any()) }
    }

    @Test
    fun `fetchById should return failure when dao operation throws exception`() = runTest {
        coEvery { connectionViewDao.fetchById(any()) } throws RuntimeException("Fetch failed")

        val result = target.fetchById(UUID.randomUUID())

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        verify { connectionViewDao.fetchById(any()) }
    }

    @Test
    fun `update version 5 - success`() = runTest {
        val connection = ConnectionEntity.Version5.default
        coEvery { connectionVersion5Dao.update(any()) } returns Unit

        val result = target.update(connection)

        assertThat(result.isSuccess).isTrue()
        verify { connectionVersion5Dao.update(connection.toVersion5()) }
    }

    @Test
    fun `update version 5 - failure`() = runTest {
        val connection = ConnectionEntity.Version5.default
        coEvery { connectionVersion5Dao.update(any()) } throws RuntimeException("Update failed")

        val result = target.update(connection)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        coVerify { connectionVersion5Dao.update(connection.toVersion5()) }
    }

    @Test
    fun `update version 6 - success`() = runTest {
        val connection = ConnectionEntity.Version6.default
        coEvery { connectionVersion6Dao.update(any()) } returns Unit

        val result = target.update(connection)

        assertThat(result.isSuccess).isTrue()
        verify { connectionVersion6Dao.update(connection.toVersion6()) }
    }

    @Test
    fun `update version 6 - failure`() = runTest {
        val connection = ConnectionEntity.Version6.default
        coEvery { connectionVersion6Dao.update(any()) } throws RuntimeException("Update failed")

        val result = target.update(connection)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        coVerify { connectionVersion6Dao.update(connection.toVersion6()) }
    }

    @Test
    fun `deleteById version 5 - success`() = runTest {
        coEvery { connectionVersion5Dao.exists(any()) } returns true
        coEvery { connectionVersion6Dao.exists(any()) } returns false
        coEvery { connectionVersion5Dao.delete(any()) } returns Unit

        val result = target.deleteById(UUID.randomUUID())

        assertThat(result.isSuccess).isTrue()
        verify { connectionVersion5Dao.delete(any()) }
    }

    @Test
    fun `deleteById version 5 - failure`() = runTest {
        coEvery { connectionVersion5Dao.exists(any()) } returns true
        coEvery { connectionVersion6Dao.exists(any()) } returns false
        coEvery { connectionVersion5Dao.delete(any()) } throws RuntimeException("Deletion failed")

        val result = target.deleteById(UUID.randomUUID())

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        verify { connectionVersion5Dao.delete(any()) }
    }

    @Test
    fun `deleteById version 6 - success`() = runTest {
        coEvery { connectionVersion5Dao.exists(any()) } returns false
        coEvery { connectionVersion6Dao.exists(any()) } returns true
        coEvery { connectionVersion6Dao.delete(any()) } returns Unit

        val result = target.deleteById(UUID.randomUUID())

        assertThat(result.isSuccess).isTrue()
        verify { connectionVersion6Dao.delete(any()) }
    }

    @Test
    fun `deleteById version 6 - failure`() = runTest {
        coEvery { connectionVersion5Dao.exists(any()) } returns false
        coEvery { connectionVersion6Dao.exists(any()) } returns true
        coEvery { connectionVersion6Dao.delete(any()) } throws RuntimeException("Deletion failed")

        val result = target.deleteById(UUID.randomUUID())

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        verify { connectionVersion6Dao.delete(any()) }
    }

    @Test
    fun `deleteAllMarkedForDeletion version 5 - success`() = runTest {
        coEvery { connectionVersion5Dao.deleteMarkedForDeletion() } returns Unit
        coEvery { connectionVersion6Dao.deleteMarkedForDeletion() } returns Unit

        val result = target.deleteAllMarkedForDeletion()

        assertThat(result.isSuccess).isTrue()
        verify { connectionVersion5Dao.deleteMarkedForDeletion() }
    }

    @Test
    fun `deleteAllMarkedForDeletion version 5 - failure`() = runTest {
        coEvery { connectionVersion5Dao.exists(any()) } returns true
        coEvery { connectionVersion6Dao.exists(any()) } returns false
        coEvery { connectionVersion5Dao.deleteMarkedForDeletion() } throws RuntimeException("Deletion failed")

        val result = target.deleteAllMarkedForDeletion()

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        verify { connectionVersion5Dao.deleteMarkedForDeletion() }
    }

    @Test
    fun `deleteAllMarkedForDeletion version 6 - success`() = runTest {
        coEvery { connectionVersion5Dao.deleteMarkedForDeletion() } returns Unit
        coEvery { connectionVersion6Dao.deleteMarkedForDeletion() } returns Unit

        val result = target.deleteAllMarkedForDeletion()

        assertThat(result.isSuccess).isTrue()
        verify { connectionVersion6Dao.deleteMarkedForDeletion() }
    }

    @Test
    fun `deleteAllMarkedForDeletion version 6 - failure`() = runTest {
        coEvery { connectionVersion5Dao.deleteMarkedForDeletion() } returns Unit
        coEvery { connectionVersion6Dao.deleteMarkedForDeletion() } throws RuntimeException("Deletion failed")

        val result = target.deleteAllMarkedForDeletion()

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        verify { connectionVersion6Dao.deleteMarkedForDeletion() }
    }

    @Test
    fun `fetchActive - success`() = runTest {
        val connection = ConnectionEntity.Version5.default
        coEvery { connectionViewDao.fetchActive() } returns connection.toConnection()

        val result = target.fetchActive()

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(connection)
        verify { connectionViewDao.fetchActive() }
    }

    @Test
    fun `fetchActive - failure`() = runTest {
        coEvery { connectionViewDao.fetchActive() } throws RuntimeException("Fetch active connection failed")

        val result = target.fetchActive()

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        verify { connectionViewDao.fetchActive() }
    }

    @Test
    fun `setActiveById version 5 - success`() = runTest {
        val id = UUID.randomUUID()
        coEvery { connectionVersion5Dao.exists(any()) } returns true
        coEvery { connectionVersion6Dao.exists(any()) } returns false
        coEvery { connectionVersion5Dao.setActive(id) } returns Unit

        val result = target.setActiveById(id)

        assertThat(result.isSuccess).isTrue()
        verify { connectionVersion5Dao.setActive(id) }
    }

    @Test
    fun `setActiveById version 5 - failure`() = runTest {
        val id = UUID.randomUUID()
        coEvery { connectionVersion5Dao.exists(any()) } returns true
        coEvery { connectionVersion6Dao.exists(any()) } returns false
        coEvery { connectionVersion5Dao.setActive(id) } throws RuntimeException("Set active connection failed")

        val result = target.setActiveById(id)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        verify { connectionVersion5Dao.setActive(id) }
    }

    @Test
    fun `setActiveById version 6 - success`() = runTest {
        val id = UUID.randomUUID()
        coEvery { connectionVersion5Dao.exists(any()) } returns false
        coEvery { connectionVersion6Dao.exists(any()) } returns true
        coEvery { connectionVersion6Dao.setActive(id) } returns Unit

        val result = target.setActiveById(id)

        assertThat(result.isSuccess).isTrue()
        verify { connectionVersion6Dao.setActive(id) }
    }

    @Test
    fun `setActiveById version 6 - failure`() = runTest {
        val id = UUID.randomUUID()
        coEvery { connectionVersion5Dao.exists(any()) } returns false
        coEvery { connectionVersion6Dao.exists(any()) } returns true
        coEvery { connectionVersion6Dao.setActive(id) } throws RuntimeException("Set active connection failed")

        val result = target.setActiveById(id)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        verify { connectionVersion6Dao.setActive(id) }
    }

    @Test
    fun `markForDeletion version 5 - success`() = runTest {
        val id = UUID.randomUUID()
        coEvery { connectionVersion5Dao.exists(any()) } returns true
        coEvery { connectionVersion6Dao.exists(any()) } returns false
        coEvery { connectionVersion5Dao.markAsDeleted(id) } returns Unit

        val result = target.markForDeletion(id)

        assertThat(result.isSuccess).isTrue()
        verify { connectionVersion5Dao.markAsDeleted(id) }
    }

    @Test
    fun `markForDeletion version 5 - failure`() = runTest {
        val id = UUID.randomUUID()
        coEvery { connectionVersion5Dao.exists(any()) } returns true
        coEvery { connectionVersion6Dao.exists(any()) } returns false
        coEvery {
            connectionVersion5Dao.markAsDeleted(id)
        } throws RuntimeException("Marking connection failed")

        val result = target.markForDeletion(id)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        verify { connectionVersion5Dao.markAsDeleted(id) }
    }

    @Test
    fun `markForDeletion version 6 - success`() = runTest {
        val id = UUID.randomUUID()
        coEvery { connectionVersion5Dao.exists(id) } returns false
        coEvery { connectionVersion6Dao.exists(id) } returns true
        coEvery { connectionVersion6Dao.markAsDeleted(id) } returns Unit

        val result = target.markForDeletion(id)

        assertThat(result.isSuccess).isTrue()
        verify { connectionVersion6Dao.markAsDeleted(id) }
    }

    @Test
    fun `markForDeletion version 6 - failure`() = runTest {
        val id = UUID.randomUUID()
        coEvery { connectionVersion5Dao.exists(id) } returns false
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
    fun `unmarkAsDeleted version 5 - success`() = runTest {
        val id = UUID.randomUUID()
        coEvery { connectionVersion5Dao.exists(any()) } returns true
        coEvery { connectionVersion6Dao.exists(any()) } returns false
        coEvery { connectionVersion5Dao.unmarkAsDeleted(id) } returns Unit

        val result = target.unmarkForDeletion(id)

        assertThat(result.isSuccess).isTrue()
        verify { connectionVersion5Dao.unmarkAsDeleted(id) }
    }

    @Test
    fun `unmarkAsDeleted version 5 - failure`() = runTest {
        val id = UUID.randomUUID()
        coEvery { connectionVersion5Dao.exists(any()) } returns true
        coEvery { connectionVersion6Dao.exists(any()) } returns false
        coEvery {
            connectionVersion5Dao.unmarkAsDeleted(id)
        } throws RuntimeException("Marking connection failed")

        val result = target.unmarkForDeletion(id)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        verify { connectionVersion5Dao.unmarkAsDeleted(id) }
    }

    @Test
    fun `unmarkAsDeleted version 6 - success`() = runTest {
        val id = UUID.randomUUID()
        coEvery { connectionVersion5Dao.exists(any()) } returns false
        coEvery { connectionVersion6Dao.exists(any()) } returns true
        coEvery { connectionVersion6Dao.unmarkAsDeleted(id) } returns Unit

        val result = target.unmarkForDeletion(id)

        assertThat(result.isSuccess).isTrue()
        verify { connectionVersion6Dao.unmarkAsDeleted(id) }
    }

    @Test
    fun `unmarkAsDeleted version 6 - failure`() = runTest {
        val id = UUID.randomUUID()
        coEvery { connectionVersion5Dao.exists(any()) } returns false
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
