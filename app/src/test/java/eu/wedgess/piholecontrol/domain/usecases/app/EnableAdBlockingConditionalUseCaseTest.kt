package eu.wedgess.piholecontrol.domain.usecases.app

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

class EnableAdBlockingConditionalUseCaseTest {

    @MockK
    private lateinit var fetchShouldChangeStatusOnAllConnectionsUseCase: FetchShouldChangeStatusOnAllConnectionsUseCase

    @MockK
    private lateinit var fetchAllConnectionsUseCase: FetchAllConnectionsUseCase

    @MockK
    private lateinit var observeActiveUserUseCase: ObserveActiveUserUseCase

    @MockK
    private lateinit var enableAdBlockingUseCase: EnableAdBlockingUseCase

    private lateinit var enableAdBlockingConditionalUseCase: EnableAdBlockingConditionalUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        enableAdBlockingConditionalUseCase = EnableAdBlockingConditionalUseCase(
            fetchShouldChangeStatusOnAllConnectionsUseCase,
            fetchAllConnectionsUseCase,
            observeActiveUserUseCase,
            enableAdBlockingUseCase
        )
    }

    @Test
    fun `invoke - enables ad blocking for all connections when changeForAll is true`() = runTest {
        val connection1 = ConnectionEntity.default
        val connection2 = ConnectionEntity.default
        val connections = listOf(connection1, connection2)
        val status = StatusEntity.ENABLED

        coEvery { fetchShouldChangeStatusOnAllConnectionsUseCase() } returns true
        coEvery { fetchAllConnectionsUseCase() } returns flowOf(Result.success(connections))
        coEvery { enableAdBlockingUseCase(any()) } returns Result.success(status)

        val result = enableAdBlockingConditionalUseCase().getOrNull()

        assertThat(result).isEqualTo(status)
        coVerify(exactly = 2) { enableAdBlockingUseCase(any()) }
    }

    @Test
    fun `invoke - enables ad blocking for active connection when changeForAll is false`() =
        runTest {
            val activeConnection = ConnectionEntity.default
            val status = StatusEntity.ENABLED

            coEvery { fetchShouldChangeStatusOnAllConnectionsUseCase() } returns false
            coEvery { observeActiveUserUseCase() } returns flowOf(Result.success(activeConnection))
            coEvery { enableAdBlockingUseCase(any()) } returns Result.success(status)

            val result = enableAdBlockingConditionalUseCase().getOrNull()

            assertThat(result).isEqualTo(status)
            coVerify(exactly = 1) { enableAdBlockingUseCase(any()) }
        }

    @Test
    fun `invoke - returns failure when fetching all connections fails`() = runTest {
        val exception = Exception("Failed to fetch connections")
        coEvery { fetchShouldChangeStatusOnAllConnectionsUseCase() } returns true
        coEvery { fetchAllConnectionsUseCase() } returns flowOf(
            Result.failure(exception)
        )

        val result = enableAdBlockingConditionalUseCase()

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify(exactly = 0) { enableAdBlockingUseCase(any()) }
    }

    @Test
    fun `invoke - returns failure when fetching active connection fails`() = runTest {
        val exception = Exception("Failed to fetch active connection")
        coEvery { fetchShouldChangeStatusOnAllConnectionsUseCase() } returns false
        coEvery { observeActiveUserUseCase() } returns flowOf(
            Result.failure(exception)
        )

        val result = enableAdBlockingConditionalUseCase()

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify(exactly = 0) { enableAdBlockingUseCase(any()) }
    }
}
