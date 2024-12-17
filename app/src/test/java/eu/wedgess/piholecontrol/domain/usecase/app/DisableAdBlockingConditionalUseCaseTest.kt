package eu.wedgess.piholecontrol.domain.usecase.app

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.StatusEntity
import eu.wedgess.piholecontrol.domain.usecases.ObserveActiveUserUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.DisableAdBlockingConditionalUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.DisableAdBlockingUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.FetchShouldChangeStatusOnAllConnectionsUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.FetchAllConnectionsUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class DisableAdBlockingConditionalUseCaseTest {

    @MockK
    private lateinit var fetchShouldChangeStatusOnAllConnectionsUseCase: FetchShouldChangeStatusOnAllConnectionsUseCase

    @MockK
    private lateinit var fetchAllConnectionsUseCase: FetchAllConnectionsUseCase

    @MockK
    private lateinit var observeActiveUserUseCase: ObserveActiveUserUseCase

    @MockK
    private lateinit var disableAdBlockingUseCase: DisableAdBlockingUseCase

    private lateinit var disableAdBlockingConditionalUseCase: DisableAdBlockingConditionalUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        disableAdBlockingConditionalUseCase = DisableAdBlockingConditionalUseCase(
            fetchShouldChangeStatusOnAllConnectionsUseCase,
            fetchAllConnectionsUseCase,
            observeActiveUserUseCase,
            disableAdBlockingUseCase
        )
    }

    @Test
    fun `invoke - disables ad blocking for all connections when changeForAll is true`() = runTest {
        val connection1 = ConnectionEntity.default
        val connection2 = ConnectionEntity.default
        val connections = listOf(connection1, connection2)
        val duration = 5000L.toDuration(DurationUnit.MILLISECONDS)
        val status = mockk<StatusEntity>(relaxed = true)

        coEvery { fetchShouldChangeStatusOnAllConnectionsUseCase() } returns true
        coEvery { fetchAllConnectionsUseCase() } returns flowOf(Result.success(connections))
        coEvery { disableAdBlockingUseCase(any(), any()) } returns Result.success(status)

        val result = disableAdBlockingConditionalUseCase(duration.toLong(DurationUnit.MILLISECONDS))
            .getOrNull()

        assertThat(result).isEqualTo(status)

        coVerify(exactly = 2) { disableAdBlockingUseCase(any(), duration) }
    }

    @Test
    fun `invoke - disables ad blocking for active connection when changeForAll is false`() =
        runTest {
            val activeConnection = ConnectionEntity.default
            val duration = 5000L.toDuration(DurationUnit.MILLISECONDS)
            val status = mockk<StatusEntity>(relaxed = true)

            coEvery { fetchShouldChangeStatusOnAllConnectionsUseCase() } returns false
            coEvery { observeActiveUserUseCase() } returns flowOf(Result.success(activeConnection))
            coEvery { disableAdBlockingUseCase(any(), any()) } returns Result.success(status)

            val result =
                disableAdBlockingConditionalUseCase(duration.toLong(DurationUnit.MILLISECONDS))
                    .getOrNull()

            assertThat(result).isEqualTo(status)

            coVerify(exactly = 1) { disableAdBlockingUseCase(any(), duration) }
        }

    @Test
    fun `invoke - returns failure when fetching all connections fails`() = runTest {
        val duration = 5000L.toDuration(DurationUnit.MILLISECONDS)

        coEvery { fetchShouldChangeStatusOnAllConnectionsUseCase() } returns true
        coEvery { fetchAllConnectionsUseCase() } returns flowOf(
            Result.failure(Exception("Failed to fetch connections"))
        )

        val result = disableAdBlockingConditionalUseCase(duration.toLong(DurationUnit.MILLISECONDS))

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(Exception::class.java)

        coVerify(exactly = 0) { disableAdBlockingUseCase(any(), any()) }
    }

    @Test
    fun `invoke - returns failure when fetching active connection fails`() = runTest {
        val duration = 5000L.toDuration(DurationUnit.MILLISECONDS)

        coEvery { fetchShouldChangeStatusOnAllConnectionsUseCase() } returns false
        coEvery { observeActiveUserUseCase() } returns flowOf(
            Result.failure(Exception("Failed to fetch active connection"))
        )

        val result = disableAdBlockingConditionalUseCase(duration.toLong(DurationUnit.MILLISECONDS))

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(Exception::class.java)

        coVerify(exactly = 0) { disableAdBlockingUseCase(any(), any()) }
    }
}
