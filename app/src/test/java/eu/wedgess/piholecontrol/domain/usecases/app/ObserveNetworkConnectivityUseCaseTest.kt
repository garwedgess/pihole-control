package eu.wedgess.piholecontrol.domain.usecases.app

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.NetworkConnectionState
import eu.wedgess.piholecontrol.domain.network.NetworkConnectivityObserver
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ObserveNetworkConnectivityUseCaseTest {

    @RelaxedMockK
    private lateinit var networkConnectivityObserver: NetworkConnectivityObserver

    private lateinit var observeNetworkConnectivityUseCase: ObserveNetworkConnectivityUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        observeNetworkConnectivityUseCase = ObserveNetworkConnectivityUseCase(networkConnectivityObserver)
    }

    @Test
    fun `WHEN invoke is called THEN networkConnectivityObserver observe should be called`() = runTest {
        // Given
        every { networkConnectivityObserver.observe() } returns flowOf(NetworkConnectionState.Available)

        // When
        observeNetworkConnectivityUseCase()

        // Then
        verify { networkConnectivityObserver.observe() }
    }

    @Test
    fun `GIVEN networkConnectivityObserver returns Available WHEN invoke is called THEN flow should emit Available`() =
        runTest {
            // Given
            val expectedState = NetworkConnectionState.Available
            every { networkConnectivityObserver.observe() } returns flowOf(expectedState)

            // When
            val resultFlow = observeNetworkConnectivityUseCase()

            // Then
            resultFlow.test {
                assertThat(awaitItem()).isEqualTo(expectedState)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `GIVEN networkConnectivityObserver returns Unavailable WHEN invoke is called THEN flow should emit Unavailable`() =
        runTest {
            // Given
            val expectedState = NetworkConnectionState.Unavailable
            every { networkConnectivityObserver.observe() } returns flowOf(expectedState)

            // When
            val resultFlow = observeNetworkConnectivityUseCase()

            // Then
            resultFlow.test {
                assertThat(awaitItem()).isEqualTo(expectedState)
                cancelAndConsumeRemainingEvents()
            }
        }
}
