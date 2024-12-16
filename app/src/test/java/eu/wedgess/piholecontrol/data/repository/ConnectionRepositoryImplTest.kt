package eu.wedgess.piholecontrol.data.repository

import TestDispatcherProvider
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.db.ConnectionDao
import eu.wedgess.piholecontrol.domain.mappers.toConnection
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ConnectionRepositoryImplTest {

    @MockK
    private lateinit var connectionDao: ConnectionDao
    private lateinit var dispatcherProvider: TestDispatcherProvider
    private lateinit var target: ConnectionRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dispatcherProvider = TestDispatcherProvider()
        target = ConnectionRepositoryImpl(connectionDao, dispatcherProvider)
    }

    @Test
    fun `insert - success`() = runTest {
        val connection = ConnectionEntity.default
        coEvery { connectionDao.insert(connection.toConnection()) } returns Unit

        val result = target.insert(connection)

        assertThat(result.isSuccess).isTrue()
        verify { connectionDao.insert(connection.toConnection()) }
    }

    @Test
    fun `insert - failure`() = runTest {
        val connection = ConnectionEntity.default
        coEvery { connectionDao.insert(connection.toConnection()) } throws RuntimeException("Insertion failed")

        val result = target.insert(connection)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        verify { connectionDao.insert(connection.toConnection()) }
    }

    @Test
    fun `checkHasConnections - success`() = runTest {
        coEvery { connectionDao.checkNotEmpty() } returns true

        val result = target.checkHasConnections()

        assertThat(result.isSuccess).isTrue()
        verify { connectionDao.checkNotEmpty() }
    }

    @Test
    fun `checkHasConnections - failure`() = runTest {
        coEvery { connectionDao.checkNotEmpty() } throws RuntimeException("Check failed")

        val result = target.checkHasConnections()

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        verify { connectionDao.checkNotEmpty() }
    }

    @Test
    fun `fetchAll - success`() = runTest {
        val connections = listOf(ConnectionEntity.default)
        every { connectionDao.fetchAllAsFlow() } returns flowOf(connections.map { it.toConnection() })

        target.fetchAll().test {
            val result = awaitItem()
            assertThat(result.isSuccess).isTrue()
            assertThat(result.getOrNull()).isEqualTo(connections)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchActiveFlow - success`() = runTest {
        val connection = ConnectionEntity.default
        every { connectionDao.fetchActiveFlow() } returns flowOf(connection.toConnection())

        target.fetchActiveFlow().test {
            val result = awaitItem()
            assertThat(result.isSuccess).isTrue()
            assertThat(result.getOrNull()).isEqualTo(connection)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchById - success`() = runTest {
        val connection = ConnectionEntity.default
        coEvery { connectionDao.fetchById(1L) } returns connection.toConnection()

        val result = target.fetchById(1L)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(connection)
        verify { connectionDao.fetchById(1L) }
    }

    @Test
    fun `fetchById - failure`() = runTest {
        coEvery { connectionDao.fetchById(1L) } throws RuntimeException("Fetch failed")

        val result = target.fetchById(1L)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        verify { connectionDao.fetchById(1L) }
    }

    @Test
    fun `update - success`() = runTest {
        val connection = ConnectionEntity.default
        coEvery { connectionDao.update(any()) } returns Unit

        val result = target.update(connection)

        assertThat(result.isSuccess).isTrue()
        verify { connectionDao.update(connection.toConnection()) }
    }

    @Test
    fun `update - failure`() = runTest {
        val connection = ConnectionEntity.default
        coEvery { connectionDao.update(any()) } throws RuntimeException("Update failed")

        val result = target.update(connection)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        coVerify { connectionDao.update(connection.toConnection()) }
    }

    @Test
    fun `deleteById - success`() = runTest {
        coEvery { connectionDao.delete(1L) } returns Unit

        val result = target.deleteById(1L)

        assertThat(result.isSuccess).isTrue()
        verify { connectionDao.delete(1L) }
    }

    @Test
    fun `deleteById - failure`() = runTest {
        coEvery { connectionDao.delete(1L) } throws RuntimeException("Deletion failed")

        val result = target.deleteById(1L)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        verify { connectionDao.delete(1L) }
    }

    @Test
    fun `fetchActive - success`() = runTest {
        val connection = ConnectionEntity.default
        coEvery { connectionDao.fetchActive() } returns connection.toConnection()

        val result = target.fetchActive()

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(connection)
        verify { connectionDao.fetchActive() }
    }

    @Test
    fun `fetchActive - failure`() = runTest {
        coEvery { connectionDao.fetchActive() } throws RuntimeException("Fetch active connection failed")

        val result = target.fetchActive()

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        verify { connectionDao.fetchActive() }
    }

    @Test
    fun `setActiveById - success`() = runTest {
        val id = 1L
        coEvery { connectionDao.setActive(id) } returns Unit

        val result = target.setActiveById(id)

        assertThat(result.isSuccess).isTrue()
        verify { connectionDao.setActive(id) }
    }

    @Test
    fun `setActiveById - failure`() = runTest {
        val id = 1L
        coEvery { connectionDao.setActive(id) } throws RuntimeException("Set active connection failed")

        val result = target.setActiveById(id)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        verify { connectionDao.setActive(id) }
    }
}
