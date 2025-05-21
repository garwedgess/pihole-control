package eu.wedgess.piholecontrol.domain.usecases.app

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.StatusEntity
import eu.wedgess.piholecontrol.domain.usecases.ObserveActiveUserUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.FetchAllConnectionsUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FetchAppInfoUseCaseTest {

    @MockK
    private lateinit var fetchAllConnectionsUseCase: FetchAllConnectionsUseCase

    @MockK
    private lateinit var observeActiveUserUseCase: ObserveActiveUserUseCase

    @MockK
    private lateinit var fetchStatusUseCase: FetchStatusUseCase

    private lateinit var target: FetchAppInfoUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = FetchAppInfoUseCase(
            fetchAllConnectionsUseCase,
            observeActiveUserUseCase,
            fetchStatusUseCase
        )
    }

    @Test
    fun `invoke - returns PiHoleAppInfo with combined results`() = runTest {
        val activeConnection = ConnectionEntity.default
        val connections = listOf(activeConnection, ConnectionEntity.default)
        val status = StatusEntity.ENABLED

        coEvery { fetchAllConnectionsUseCase() } returns flowOf(Result.success(connections))
        coEvery { observeActiveUserUseCase() } returns flowOf(Result.success(activeConnection))
        coEvery { fetchStatusUseCase() } returns flowOf(Result.success(status))

        target().test {
            val result = awaitItem()
            assertThat(result).isNotNull()
            assertThat(result.connections).isEqualTo(connections)
            assertThat(result.currentConnection).isEqualTo(activeConnection)
            assertThat(result.status).isEqualTo(status)
            cancelAndConsumeRemainingEvents()
        }

        coVerify { fetchAllConnectionsUseCase() }
        coVerify { observeActiveUserUseCase() }
        coVerify { fetchStatusUseCase() }
    }

    @Test
    fun `invoke - returns PiHoleAppInfo with default values when repository returns empty or null`() =
        runTest {
            val emptyConnections = emptyList<ConnectionEntity>()
            val activeConnection = ConnectionEntity.default
            val defaultStatus = StatusEntity.UNKNOWN

            coEvery { fetchAllConnectionsUseCase() } returns flowOf(Result.success(emptyConnections))
            coEvery { observeActiveUserUseCase() } returns flowOf(Result.success(activeConnection))
            coEvery { fetchStatusUseCase() } returns flowOf(Result.success(defaultStatus))

            target().test {
                val result = awaitItem()
                assertThat(result).isNotNull()
                assertThat(result.connections).isEqualTo(emptyConnections)
                assertThat(result.currentConnection).isEqualTo(activeConnection)
                assertThat(result.status).isEqualTo(defaultStatus)
                cancelAndConsumeRemainingEvents()
            }

            coVerify { fetchAllConnectionsUseCase() }
            coVerify { observeActiveUserUseCase() }
            coVerify { fetchStatusUseCase() }
        }

    @Test
    fun `invoke - returns PiHoleAppInfo with error status when fetching status fails`() = runTest {
        val connection1 = ConnectionEntity.default
        val connections = listOf(connection1)
        val activeConnection = connection1
        val exception = Exception("Failed to fetch status")

        coEvery { fetchAllConnectionsUseCase() } returns flowOf(Result.success(connections))
        coEvery { observeActiveUserUseCase() } returns flowOf(Result.success(activeConnection))
        coEvery { fetchStatusUseCase() } returns flowOf(Result.failure(exception))

        target().test {
            val result = awaitItem()
            assertThat(result).isNotNull()
            assertThat(result.connections).isEqualTo(connections)
            assertThat(result.currentConnection).isEqualTo(activeConnection)
            assertThat(result.status).isEqualTo(StatusEntity.UNKNOWN)
            cancelAndConsumeRemainingEvents()
        }

        coVerify { fetchAllConnectionsUseCase() }
        coVerify { observeActiveUserUseCase() }
        coVerify { fetchStatusUseCase() }
    }
}
