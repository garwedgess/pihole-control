package eu.wedgess.piholecontrol.domain

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.SettingsRepository
import eu.wedgess.piholecontrol.domain.usecases.ObserveActiveUserUseCase
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class PeriodicRefreshUseCaseTest {

    @RelaxedMockK
    private lateinit var observeActiveUser: ObserveActiveUserUseCase

    @RelaxedMockK
    private lateinit var settingsRepository: SettingsRepository

    private lateinit var target: PeriodicRefreshUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = PeriodicRefreshUseCase(observeActiveUser, settingsRepository)
    }

    @Test
    fun `invoke - fetchData called with active connection and delay`() = runTest {
        var fetchDataCallCount = 0
        val activeConnection = ConnectionEntity.default
        val expectedData = "Fetched Data"
        val fetchData: suspend (ConnectionEntity) -> Result<String> = {
            fetchDataCallCount++
            Result.success(expectedData)
        }
        val refreshInterval = 1000L
        every { observeActiveUser() } returns flowOf(Result.success(activeConnection))
        every { settingsRepository.getRefreshInterval() } returns flowOf(refreshInterval)

        target(fetchData).test {
            assertThat(awaitItem()).isEqualTo(Result.success(expectedData))
            advanceTimeBy(refreshInterval)
            assertThat(awaitItem()).isEqualTo(Result.success(expectedData))
            advanceTimeBy(refreshInterval)
            assertThat(awaitItem()).isEqualTo(Result.success(expectedData))
            cancelAndConsumeRemainingEvents()
        }

        assertThat(fetchDataCallCount).isEqualTo(3)
    }


    @Test
    fun `invoke - fetchData not called when connection is null`() = runTest {
        val fetchData: suspend (ConnectionEntity) -> Result<String> = mockk(relaxed = true)

        every { observeActiveUser() } returns flowOf(Result.failure(Exception("No active connection")))
        every { settingsRepository.getRefreshInterval() } returns flowOf(1000L)

        target(fetchData).test {
            expectNoEvents()
        }

        coVerify(exactly = 0) { fetchData(any()) }
    }

    @Test
    fun `invoke - fetchData handles delay`() = runTest {
        var fetchDataCallCount = 0
        val activeConnection = ConnectionEntity.default
        val expectedData = "Fetched Data"
        val fetchData: suspend (ConnectionEntity) -> Result<String> = {
            fetchDataCallCount++
            Result.success(expectedData)
        }
        val refreshInterval = 500L

        every { observeActiveUser() } returns flowOf(Result.success(activeConnection))
        every { settingsRepository.getRefreshInterval() } returns flowOf(refreshInterval)

        target(fetchData).test {
            // Verify the first emission
            assertThat(awaitItem()).isEqualTo(Result.success(expectedData))
            // Simulate the delay and verify subsequent emissions
            advanceTimeBy(refreshInterval)
            assertThat(awaitItem()).isEqualTo(Result.success(expectedData))
            advanceTimeBy(refreshInterval)
            assertThat(awaitItem()).isEqualTo(Result.success(expectedData))
            cancelAndConsumeRemainingEvents()
        }

        assertThat(fetchDataCallCount).isEqualTo(3)
    }

    @Test
    fun `triggerRefresh - triggers refresh flow`() = runTest {
        var fetchDataCallCount = 0
        val expectedData = "Fetched Data"
        val fetchData: suspend (ConnectionEntity) -> Result<String> = {
            fetchDataCallCount++
            Result.success(expectedData)
        }
        val activeConnection = ConnectionEntity.default
        val refreshInterval = 1000L

        every { observeActiveUser() } returns flowOf(Result.success(activeConnection))
        every { settingsRepository.getRefreshInterval() } returns flowOf(refreshInterval)

        target(fetchData).test {
            assertThat(awaitItem()).isEqualTo(Result.success(expectedData))
            target.triggerRefresh()
            assertThat(awaitItem()).isEqualTo(Result.success(expectedData))
            cancelAndConsumeRemainingEvents()
        }

        assertThat(fetchDataCallCount).isEqualTo(2)
    }
}
