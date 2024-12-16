package eu.wedgess.piholecontrol.domain

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.SettingsRepository
import eu.wedgess.piholecontrol.domain.usecases.ObserveActiveUserUseCase
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.presentation.base.RefreshFlow
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PeriodicRefreshUseCaseTest {

    @MockK
    private lateinit var observeActiveUser: ObserveActiveUserUseCase

    @MockK
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var refreshFlow: RefreshFlow
    private lateinit var target: PeriodicRefreshUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        refreshFlow = RefreshFlow()
        target = PeriodicRefreshUseCase(observeActiveUser, settingsRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `invoke - fetchData called with active connection and delay`() = runTest {
        val activeConnection = ConnectionEntity.default
        val fetchData: suspend (ConnectionEntity) -> String = mockk(relaxed = true)
        val refreshInterval = 1000L
        val expectedData = "Fetched Data"
        every { observeActiveUser() } returns flowOf(Result.success(activeConnection))
        every { settingsRepository.getRefreshInterval() } returns flowOf(refreshInterval)
        coEvery { fetchData(activeConnection) } returns expectedData

        val testJob = launch {
            val result = target(fetchData).take(3).toList()

            assertThat(result).containsExactly(expectedData, expectedData, expectedData)
        }

        advanceTimeBy(3000L)
        testJob.join()

        coVerify(exactly = 3) { fetchData(activeConnection) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `invoke - fetchData not called when connection is null`() = runTest {
        val fetchData: suspend (ConnectionEntity) -> String = mockk(relaxed = true)

        every { observeActiveUser() } returns flowOf(Result.failure(Exception("No active connection")))
        every { settingsRepository.getRefreshInterval() } returns flowOf(1000L)

        val result = mutableListOf<String>()

        val job = launch {
            target(fetchData).take(1).collect { result.add(it) }
        }
        advanceUntilIdle()
        job.cancelAndJoin()

        assertThat(result).isEmpty()
        coVerify(exactly = 0) { fetchData(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `invoke - fetchData handles delay`() = runTest {
        val activeConnection = ConnectionEntity.default
        val fetchData: suspend (ConnectionEntity) -> String = mockk(relaxed = true)
        val refreshInterval = 500L
        val expectedData = "Fetched Data"

        // Mock the dependencies
        every { observeActiveUser() } returns flowOf(Result.success(activeConnection))
        every { settingsRepository.getRefreshInterval() } returns flowOf(refreshInterval)
        coEvery { fetchData(activeConnection) } returns expectedData

        val result = mutableListOf<String>()
        val job = launch {
            target(fetchData).take(3).collect { result.add(it) }
        }
        advanceTimeBy(1500L)
        job.cancelAndJoin()

        assertThat(result).containsExactly(expectedData, expectedData, expectedData)
        coVerify(exactly = 3) { fetchData(activeConnection) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `triggerRefresh - triggers refresh flow`() = runTest {
        val fetchData: suspend (ConnectionEntity) -> String = mockk(relaxed = true)
        val activeConnection = ConnectionEntity.default
        val refreshInterval = 1000L
        val expectedData = "Fetched Data"
        every { observeActiveUser() } returns flowOf(Result.success(activeConnection))
        every { settingsRepository.getRefreshInterval() } returns flowOf(refreshInterval)
        coEvery { fetchData(activeConnection) } returns expectedData

        val result = mutableListOf<String>()
        val job = launch {
            target(fetchData).take(1).collect { result.add(it) }
        }
        target.triggerRefresh()
        advanceUntilIdle()
        job.cancelAndJoin()

        // Assertions
        assertThat(result).containsExactly(expectedData)
        coVerify(exactly = 1) { fetchData(activeConnection) }
    }
}
