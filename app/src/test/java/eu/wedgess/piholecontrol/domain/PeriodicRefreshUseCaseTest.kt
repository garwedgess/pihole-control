package eu.wedgess.piholecontrol.domain

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.NetworkConnectionState
import eu.wedgess.piholecontrol.domain.model.RefreshMode
import eu.wedgess.piholecontrol.domain.repository.SettingsRepository
import eu.wedgess.piholecontrol.domain.usecases.ObserveActiveUserUseCase
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.ObserveNetworkConnectivityUseCase
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
    private lateinit var observeNetworkConnectivityUseCase: ObserveNetworkConnectivityUseCase

    @RelaxedMockK
    private lateinit var settingsRepository: SettingsRepository

    private lateinit var target: PeriodicRefreshUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = PeriodicRefreshUseCase(
            observeActiveUser,
            observeNetworkConnectivityUseCase,
            settingsRepository
        )
    }

    @Test
    fun `invoke - automatic mode fetch success`() = runTest {
        var fetchDataCallCount = 0
        val activeConnection = ConnectionEntity.Version5.default
        val expectedData = "Fetched Data"
        val fetchData: suspend (ConnectionEntity) -> Result<String> = {
            fetchDataCallCount++
            Result.success(expectedData)
        }
        val refreshInterval = 1000L
        every { observeActiveUser() } returns flowOf(Result.success(activeConnection))
        every { observeNetworkConnectivityUseCase() } returns flowOf(NetworkConnectionState.Available)
        every { settingsRepository.getRefreshInterval() } returns flowOf(refreshInterval)

        target(fetchData).test {
            repeat(3) {
                assertThat(awaitItem()).isEqualTo(Result.success(expectedData))
                advanceTimeBy(refreshInterval)
            }
            cancelAndConsumeRemainingEvents()
        }

        assertThat(fetchDataCallCount).isEqualTo(3)
    }

    @Test
    fun `invoke - automatic mode fetch failure transitions to manual mode`() = runTest {
        var fetchDataCallCount = 0
        val activeConnection = ConnectionEntity.Version5.default
        val fetchData: suspend (ConnectionEntity) -> Result<String> = {
            fetchDataCallCount++
            Result.failure(Exception("Fetch failed"))
        }
        val refreshInterval = 1000L

        every { observeActiveUser() } returns flowOf(Result.success(activeConnection))
        every { observeNetworkConnectivityUseCase() } returns flowOf(NetworkConnectionState.Available)
        every { settingsRepository.getRefreshInterval() } returns flowOf(refreshInterval)

        target(fetchData).test {
            val firstResult = awaitItem()
            assertThat(firstResult.isFailure).isTrue()
            cancelAndConsumeRemainingEvents()
        }

        assertThat(fetchDataCallCount).isEqualTo(1)
    }

    @Test
    fun `invoke - manual mode fetch success transitions to automatic mode`() = runTest {
        var fetchDataCallCount = 0
        val activeConnection = ConnectionEntity.Version5.default
        val expectedData = "Fetched Data"
        val fetchData: suspend (ConnectionEntity) -> Result<String> = {
            fetchDataCallCount++
            Result.success(expectedData)
        }
        val refreshInterval = 1000L
        every { observeActiveUser() } returns flowOf(Result.success(activeConnection))
        every { observeNetworkConnectivityUseCase() } returns flowOf(NetworkConnectionState.Available)
        every { settingsRepository.getRefreshInterval() } returns flowOf(refreshInterval)

        target(fetchData).test {
            // First emission: Automatic mode fetch success
            assertThat(awaitItem()).isEqualTo(Result.success(expectedData))
            // Trigger manual refresh (manual mode fetch also success)
            target.triggerRefresh()
            assertThat(awaitItem()).isEqualTo(Result.success(expectedData))
            cancelAndConsumeRemainingEvents()
        }

        assertThat(fetchDataCallCount).isEqualTo(2)
    }

    @Test
    fun `invoke - manual mode fetch failure remains in manual mode`() = runTest {
        var fetchDataCallCount = 0
        val activeConnection = ConnectionEntity.Version5.default
        val fetchData: suspend (ConnectionEntity) -> Result<String> = {
            fetchDataCallCount++
            // All calls fail
            Result.failure(Exception("Fetch failed"))
        }
        val refreshInterval = 1000L

        every { observeActiveUser() } returns flowOf(Result.success(activeConnection))
        every { observeNetworkConnectivityUseCase() } returns flowOf(NetworkConnectionState.Available)
        every { settingsRepository.getRefreshInterval() } returns flowOf(refreshInterval)

        target(fetchData).test {
            // First emission: Automatic mode fetch fails
            val firstResult = awaitItem()
            assertThat(firstResult.isFailure).isTrue()

            // Trigger manual refresh (manual mode fetch also fails)
            target.triggerRefresh()
            val secondResult = awaitItem()
            assertThat(secondResult.isFailure).isTrue()
            // Trigger manual refresh again (still fails)
            target.triggerRefresh()
            val thirdResult = awaitItem()
            assertThat(thirdResult.isFailure).isTrue()

            cancelAndConsumeRemainingEvents()
        }

        assertThat(fetchDataCallCount).isEqualTo(3)
    }

    @Test
    fun `invoke - manual refresh mode with null active pihole`() = runTest {
        val fetchData: suspend (ConnectionEntity) -> Result<String> = mockk(relaxed = true)
        val refreshInterval = 1000L
        every { observeActiveUser() } returns flowOf(Result.failure(Exception("No active connection")))
        every { observeNetworkConnectivityUseCase() } returns flowOf(NetworkConnectionState.Available)
        every { settingsRepository.getRefreshInterval() } returns flowOf(refreshInterval)

        target(fetchData).test {
            expectNoEvents()
        }

        coVerify(exactly = 0) { fetchData(any()) }
    }

    @Test
    fun `invoke - network unavailable transitions to none mode and emits last result`() = runTest {
        val activeConnection = ConnectionEntity.Version5.default
        val expectedData = "Fetched Data"
        val fetchData: suspend (ConnectionEntity) -> Result<String> =
            { Result.success(expectedData) }
        val refreshInterval = 1000L
        every { observeActiveUser() } returns flowOf(Result.success(activeConnection))
        every { observeNetworkConnectivityUseCase() } returns flowOf(
            NetworkConnectionState.Available,
            NetworkConnectionState.Unavailable
        )
        every { settingsRepository.getRefreshInterval() } returns flowOf(refreshInterval)

        target(fetchData).test {
            assertThat(awaitItem()).isEqualTo(Result.success(expectedData))
            assertThat(awaitItem()).isEqualTo(Result.success(expectedData))
            cancelAndConsumeRemainingEvents()
        }
        // Verify that the mode is changed to RefreshMode.None
        val currentMode = target.javaClass.getDeclaredField("currentRefreshMode")
        currentMode.isAccessible = true
        assertThat(currentMode.get(target)).isEqualTo(RefreshMode.None)
    }

    @Test
    fun `invoke - network unavailable transitions to none mode and emits failure when no last result`() =
        runTest {
            val activeConnection = ConnectionEntity.Version5.default
            val fetchData: suspend (ConnectionEntity) -> Result<String> =
                { Result.failure(Exception("No data")) }
            every { observeActiveUser() } returns flowOf(Result.success(activeConnection))
            every { observeNetworkConnectivityUseCase() } returns flowOf(NetworkConnectionState.Unavailable)
            every { settingsRepository.getRefreshInterval() } returns flowOf(1000L)

            target(fetchData).test {
                val result = awaitItem()
                assertThat(result.isFailure).isTrue()
                assertThat(result.exceptionOrNull()?.message)
                    .isEqualTo("Failed to connect to ${activeConnection.host}")
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `invoke - network recovers transitions to automatic mode and resumes refresh`() = runTest {
        var fetchDataCallCount = 0
        val activeConnection = ConnectionEntity.Version5.default
        val expectedData = "Fetched Data"
        val fetchData: suspend (ConnectionEntity) -> Result<String> = {
            fetchDataCallCount++
            Result.success(expectedData)
        }
        val refreshInterval = 1000L
        every { observeActiveUser() } returns flowOf(Result.success(activeConnection))
        every { observeNetworkConnectivityUseCase() } returns flowOf(
            NetworkConnectionState.Available,
            NetworkConnectionState.Unavailable,
            NetworkConnectionState.Available
        )
        every { settingsRepository.getRefreshInterval() } returns flowOf(refreshInterval)

        target(fetchData).test {
            assertThat(awaitItem()).isEqualTo(Result.success(expectedData))
            assertThat(awaitItem()).isEqualTo(Result.success(expectedData))
            assertThat(awaitItem()).isEqualTo(Result.success(expectedData))
            cancelAndConsumeRemainingEvents()
        }

        assertThat(fetchDataCallCount).isEqualTo(2)
    }

    @Test
    fun `triggerRefresh - forces manual refresh`() = runTest {
        var fetchDataCallCount = 0
        val activeConnection = ConnectionEntity.Version5.default
        val expectedData = "Fetched Data"
        val fetchData: suspend (ConnectionEntity) -> Result<String> = {
            fetchDataCallCount++
            Result.success(expectedData)
        }
        val refreshInterval = 1000L
        every { observeActiveUser() } returns flowOf(Result.success(activeConnection))
        every { observeNetworkConnectivityUseCase() } returns flowOf(NetworkConnectionState.Available)
        every { settingsRepository.getRefreshInterval() } returns flowOf(refreshInterval)

        target(fetchData).test {
            assertThat(awaitItem()).isEqualTo(Result.success(expectedData))
            target.triggerRefresh()
            assertThat(awaitItem()).isEqualTo(Result.success(expectedData))
            cancelAndConsumeRemainingEvents()
        }

        assertThat(fetchDataCallCount).isEqualTo(2)
    }

    @Test
    fun `setRefreshModeBasedOnNetworkStatus - available network transitions to automatic mode`() =
        runTest {
            val activeConnection = ConnectionEntity.Version5.default
            val expectedData = "Data"
            val fetchData: suspend (ConnectionEntity) -> Result<String> =
                { Result.success(expectedData) }
            val refreshInterval = 1000L
            every { observeActiveUser() } returns flowOf(Result.success(activeConnection))
            every { observeNetworkConnectivityUseCase() } returns flowOf(
                NetworkConnectionState.Unavailable,
                NetworkConnectionState.Available
            )
            every { settingsRepository.getRefreshInterval() } returns flowOf(refreshInterval)

            target(fetchData).test {
                assertThat(awaitItem().isFailure).isTrue()
                assertThat(awaitItem()).isEqualTo(Result.success(expectedData))
                cancelAndConsumeRemainingEvents()
            }
        }
}
