package eu.wedgess.piholecontrol.domain

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.SettingsRepository
import eu.wedgess.piholecontrol.domain.usecases.ObserveActiveUserUseCase
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class PeriodicRefreshUseCaseTest {

    @MockK
    private lateinit var observeActiveUser: ObserveActiveUserUseCase

    @MockK
    private lateinit var settingsRepository: SettingsRepository

    private lateinit var target: PeriodicRefreshUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = PeriodicRefreshUseCase(observeActiveUser, settingsRepository)
    }

    @Test
    fun `invoke - fetchData called with active connection and delay`() = runTest {
        val activeConnection = ConnectionEntity.default
        val fetchData: suspend (ConnectionEntity) -> String = mockk(relaxed = true)
        val refreshInterval = 1000L
        val expectedData = "Fetched Data"
        every { observeActiveUser() } returns flowOf(Result.success(activeConnection))
        every { settingsRepository.getRefreshInterval() } returns flowOf(refreshInterval)
        coEvery { fetchData(activeConnection) } returns expectedData

        target(fetchData).test {
            assertThat(awaitItem()).isEqualTo(expectedData)
            assertThat(awaitItem()).isEqualTo(expectedData)
            assertThat(awaitItem()).isEqualTo(expectedData)
            cancelAndConsumeRemainingEvents()
        }

        coVerify(exactly = 3) { fetchData(activeConnection) }
    }

    @Test
    fun `invoke - fetchData not called when connection is null`() = runTest {
        val fetchData: suspend (ConnectionEntity) -> String = mockk(relaxed = true)

        every { observeActiveUser() } returns flowOf(Result.failure(Exception("No active connection")))
        every { settingsRepository.getRefreshInterval() } returns flowOf(1000L)

        target(fetchData).test {
            expectNoEvents()
        }

        coVerify(exactly = 0) { fetchData(any()) }
    }

    @Test
    fun `invoke - fetchData handles delay`() = runTest {
        val activeConnection = ConnectionEntity.default
        val fetchData: suspend (ConnectionEntity) -> String = mockk(relaxed = true)
        val refreshInterval = 500L
        val expectedData = "Fetched Data"

        every { observeActiveUser() } returns flowOf(Result.success(activeConnection))
        every { settingsRepository.getRefreshInterval() } returns flowOf(refreshInterval)
        coEvery { fetchData(activeConnection) } returns expectedData

        target(fetchData).test {
            assertThat(awaitItem()).isEqualTo(expectedData)
            assertThat(awaitItem()).isEqualTo(expectedData)
            assertThat(awaitItem()).isEqualTo(expectedData)
            cancelAndConsumeRemainingEvents()
        }
        advanceTimeBy(1500L)

        coVerify(exactly = 3) { fetchData(activeConnection) }
    }

    @Test
    fun `triggerRefresh - triggers refresh flow`() = runTest {
        val fetchData: suspend (ConnectionEntity) -> String = mockk(relaxed = true)
        val activeConnection = ConnectionEntity.default
        val refreshInterval = 1000L
        val expectedData = "Fetched Data"
        every { observeActiveUser() } returns flowOf(Result.success(activeConnection))
        every { settingsRepository.getRefreshInterval() } returns flowOf(refreshInterval)
        coEvery { fetchData(activeConnection) } returns expectedData

        target(fetchData).test {
            target.triggerRefresh()
            assertThat(awaitItem()).isEqualTo(expectedData)
            cancelAndConsumeRemainingEvents()
        }

        coVerify(exactly = 2) { fetchData(activeConnection) }
    }
}
