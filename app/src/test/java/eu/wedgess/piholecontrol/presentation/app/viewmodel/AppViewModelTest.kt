package eu.wedgess.piholecontrol.presentation.app.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.MainDispatcherRule
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.NetworkConnectionState
import eu.wedgess.piholecontrol.domain.model.StatusEntity
import eu.wedgess.piholecontrol.domain.usecases.app.DisableAdBlockingConditionalUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.EnableAdBlockingConditionalUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.FetchAppInfoUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.ObserveNetworkConnectivityUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.SetConnectionAsActiveUseCase
import eu.wedgess.piholecontrol.presentation.app.AppContract
import eu.wedgess.piholecontrol.presentation.app.model.AppBarState
import eu.wedgess.piholecontrol.presentation.app.model.AppDialogType
import eu.wedgess.piholecontrol.presentation.app.model.PiHoleAppInfo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AppViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @RelaxedMockK
    private lateinit var fetchAppInfoUseCase: FetchAppInfoUseCase

    @RelaxedMockK
    private lateinit var enableAdBlockingConditionalUseCase: EnableAdBlockingConditionalUseCase

    @RelaxedMockK
    private lateinit var disableAdBlockingConditionalUseCase: DisableAdBlockingConditionalUseCase

    @RelaxedMockK
    private lateinit var setConnectionAsActiveUseCase: SetConnectionAsActiveUseCase

    @RelaxedMockK
    private lateinit var observeNetworkConnectivityUseCase: ObserveNetworkConnectivityUseCase

    private lateinit var viewModel: AppViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `WHEN viewmodel is initialized THEN uiState should emit initial state`() = runTest {
        // Given
        coEvery { fetchAppInfoUseCase() } returns flowOf(mockk(relaxed = true))
        every { observeNetworkConnectivityUseCase() } returns flowOf(NetworkConnectionState.Available)

        // When
        viewModel = AppViewModel(
            fetchAppInfoUseCase,
            enableAdBlockingConditionalUseCase,
            disableAdBlockingConditionalUseCase,
            setConnectionAsActiveUseCase,
            observeNetworkConnectivityUseCase
        )

        // Then
        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(AppContract.UiState.initial())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `GIVEN usecase returns success WHEN viewmodel is initialized THEN uiState should emit updated state`() =
        runTest {
            // Given
            val appInfo = PiHoleAppInfo(
                status = StatusEntity.ENABLED,
                currentConnection = ConnectionEntity.default,
                connections = listOf(ConnectionEntity.default)
            )
            coEvery { fetchAppInfoUseCase() } returns flowOf(appInfo)
            every { observeNetworkConnectivityUseCase() } returns flowOf(NetworkConnectionState.Available)

            // When
            viewModel = AppViewModel(
                fetchAppInfoUseCase,
                enableAdBlockingConditionalUseCase,
                disableAdBlockingConditionalUseCase,
                setConnectionAsActiveUseCase,
                observeNetworkConnectivityUseCase
            )

            // Then
            viewModel.uiState.test {
                // Skip the initial state
                skipItems(1)
                // Await the updated state
                with(awaitItem()) {
                    assertThat(this.appInfo).isEqualTo(appInfo)
                    assertThat(appBarState.adBlockingEnabled).isTrue()
                    assertThat(appBarState.currentConnection).isEqualTo(appInfo.currentConnection)
                    assertThat(appBarState.connections).isEqualTo(appInfo.connections)
                }
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN DismissDialog event is received THEN uiState should update dialogType to None`() =
        runTest {
            // Given
            coEvery { fetchAppInfoUseCase() } returns flowOf(mockk(relaxed = true))
            every { observeNetworkConnectivityUseCase() } returns flowOf(NetworkConnectionState.Available)
            viewModel = AppViewModel(
                fetchAppInfoUseCase,
                enableAdBlockingConditionalUseCase,
                disableAdBlockingConditionalUseCase,
                setConnectionAsActiveUseCase,
                observeNetworkConnectivityUseCase
            )

            // Track emitted states
            val emittedStates = mutableListOf<AppContract.UiState>()
            val job = backgroundScope.launch {
                viewModel.uiState.collect { emittedStates.add(it) }
            }

            // When
            viewModel.onEvent(AppContract.Event.ShowEnabledStatusDialog)
            runCurrent()
            viewModel.onEvent(AppContract.Event.DismissDialog)
            runCurrent()
            job.cancel()

            assertThat(emittedStates[0].dialogType).isEqualTo(AppDialogType.EnableAdBlocking)
            assertThat(emittedStates[1].dialogType).isEqualTo(AppDialogType.None)
        }

    @Test
    fun `WHEN SetDisabledStatus event is received THEN disableAdBlockingConditionalUseCase should be called`() =
        runTest {
            // Given
            val duration = 300L
            coEvery { fetchAppInfoUseCase() } returns flowOf(mockk(relaxed = true))
            coEvery { disableAdBlockingConditionalUseCase(duration) } returns Result.success(
                StatusEntity.ENABLED
            )
            every { observeNetworkConnectivityUseCase() } returns flowOf(NetworkConnectionState.Available)
            viewModel = AppViewModel(
                fetchAppInfoUseCase,
                enableAdBlockingConditionalUseCase,
                disableAdBlockingConditionalUseCase,
                setConnectionAsActiveUseCase,
                observeNetworkConnectivityUseCase
            )

            // When
            viewModel.onEvent(AppContract.Event.SetDisabledStatus(duration))
            advanceUntilIdle()

            // Then
            coVerify { disableAdBlockingConditionalUseCase(duration) }
            coVerify { fetchAppInfoUseCase.refresh() }
        }

    @Test
    fun `WHEN OnConnectionSelected event is received THEN setConnectionAsActiveUseCase should be called`() =
        runTest {
            // Given
            val connection = ConnectionEntity.default
            coEvery { fetchAppInfoUseCase() } returns flowOf(mockk(relaxed = true))
            coEvery { setConnectionAsActiveUseCase(connection.id) } returns Result.success(Unit)
            every { observeNetworkConnectivityUseCase() } returns flowOf(NetworkConnectionState.Available)
            viewModel = AppViewModel(
                fetchAppInfoUseCase,
                enableAdBlockingConditionalUseCase,
                disableAdBlockingConditionalUseCase,
                setConnectionAsActiveUseCase,
                observeNetworkConnectivityUseCase
            )

            // When
            viewModel.onEvent(AppContract.Event.OnConnectionSelected(connection))
            advanceUntilIdle()

            // Then
            coVerify { setConnectionAsActiveUseCase(connection.id) }
        }

    @Test
    fun `WHEN UpdateAppBarState event is received THEN uiState should update appBarState`() =
        runTest {
            // Given
            coEvery { fetchAppInfoUseCase() } returns flowOf(
                PiHoleAppInfo(
                    currentConnection = ConnectionEntity.default.copy(id = 1),
                    connections = listOf(ConnectionEntity.default.copy(id = 1)),
                    status = StatusEntity.ENABLED
                )
            )
            every { observeNetworkConnectivityUseCase() } returns flowOf(NetworkConnectionState.Available)
            viewModel = AppViewModel(
                fetchAppInfoUseCase,
                enableAdBlockingConditionalUseCase,
                disableAdBlockingConditionalUseCase,
                setConnectionAsActiveUseCase,
                observeNetworkConnectivityUseCase
            )
            val newAppBarState = AppBarState(
                adBlockingEnabled = true,
                currentConnection = ConnectionEntity.default.copy(id = 1),
                connections = listOf(ConnectionEntity.default.copy(id = 1))
            )

            // When
            viewModel.onEvent(AppContract.Event.UpdateAppBarState(newAppBarState))

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.appBarState).isEqualTo(newAppBarState)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN SetEnabledStatus event is received THEN enableAdBlockingConditionalUseCase should be called`() =
        runTest {
            // Given
            coEvery { fetchAppInfoUseCase() } returns flowOf(mockk(relaxed = true))
            coEvery { enableAdBlockingConditionalUseCase() } returns Result.success(StatusEntity.ENABLED)
            every { observeNetworkConnectivityUseCase() } returns flowOf(NetworkConnectionState.Available)
            viewModel = AppViewModel(
                fetchAppInfoUseCase,
                enableAdBlockingConditionalUseCase,
                disableAdBlockingConditionalUseCase,
                setConnectionAsActiveUseCase,
                observeNetworkConnectivityUseCase
            )

            // When
            viewModel.onEvent(AppContract.Event.SetEnabledStatus)
            advanceUntilIdle()

            // Then
            coVerify { enableAdBlockingConditionalUseCase() }
            coVerify { fetchAppInfoUseCase.refresh() }
        }

    @Test
    fun `GIVEN usecase returns failure WHEN OnConnectionSelected event is received THEN error should be logged`() =
        runTest {
            // Given
            val connection = ConnectionEntity.default
            coEvery { fetchAppInfoUseCase() } returns flowOf(mockk(relaxed = true))
            coEvery { setConnectionAsActiveUseCase(connection.id) } returns Result.failure(
                Exception("Test")
            )
            every { observeNetworkConnectivityUseCase() } returns flowOf(NetworkConnectionState.Available)
            viewModel = AppViewModel(
                fetchAppInfoUseCase,
                enableAdBlockingConditionalUseCase,
                disableAdBlockingConditionalUseCase,
                setConnectionAsActiveUseCase,
                observeNetworkConnectivityUseCase
            )

            // When
            viewModel.onEvent(AppContract.Event.OnConnectionSelected(connection))
            advanceUntilIdle()

            // Then
            coVerify { setConnectionAsActiveUseCase(connection.id) }
        }

    @Test
    fun `GIVEN usecase returns failure WHEN SetDisabledStatus event is received THEN error should be logged`() =
        runTest {
            // Given
            val duration = 300L
            coEvery { fetchAppInfoUseCase() } returns flowOf(mockk(relaxed = true))
            coEvery { disableAdBlockingConditionalUseCase(duration) } returns Result.failure(
                Exception("Test")
            )
            every { observeNetworkConnectivityUseCase() } returns flowOf(NetworkConnectionState.Available)
            viewModel = AppViewModel(
                fetchAppInfoUseCase,
                enableAdBlockingConditionalUseCase,
                disableAdBlockingConditionalUseCase,
                setConnectionAsActiveUseCase,
                observeNetworkConnectivityUseCase
            )

            // When
            viewModel.onEvent(AppContract.Event.SetDisabledStatus(duration))
            advanceUntilIdle()

            // Then
            coVerify { disableAdBlockingConditionalUseCase(duration) }
        }

    @Test
    fun `GIVEN usecase returns failure WHEN SetEnabledStatus event is received THEN error should be logged`() =
        runTest {
            // Given
            coEvery { fetchAppInfoUseCase() } returns flowOf(mockk(relaxed = true))
            coEvery { enableAdBlockingConditionalUseCase() } returns Result.failure(Exception("Test"))
            every { observeNetworkConnectivityUseCase() } returns flowOf(NetworkConnectionState.Available)
            viewModel = AppViewModel(
                fetchAppInfoUseCase,
                enableAdBlockingConditionalUseCase,
                disableAdBlockingConditionalUseCase,
                setConnectionAsActiveUseCase,
                observeNetworkConnectivityUseCase
            )

            // When
            viewModel.onEvent(AppContract.Event.SetEnabledStatus)
            advanceUntilIdle()

            // Then
            coVerify { enableAdBlockingConditionalUseCase() }
        }

    @Test
    fun `WHEN ShowEnabledStatusDialog event is received THEN uiState should update dialogType to EnableAdBlocking`() =
        runTest {
            // Given
            coEvery { fetchAppInfoUseCase() } returns flowOf(mockk(relaxed = true))
            every { observeNetworkConnectivityUseCase() } returns flowOf(NetworkConnectionState.Available)
            viewModel = AppViewModel(
                fetchAppInfoUseCase,
                enableAdBlockingConditionalUseCase,
                disableAdBlockingConditionalUseCase,
                setConnectionAsActiveUseCase,
                observeNetworkConnectivityUseCase
            )

            // When
            viewModel.onEvent(AppContract.Event.ShowEnabledStatusDialog)

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.dialogType).isEqualTo(AppDialogType.EnableAdBlocking)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN ShowDisabledStatusDialog event is received THEN uiState should update dialogType to DisableAdBlocking`() =
        runTest {
            // Given
            coEvery { fetchAppInfoUseCase() } returns flowOf(mockk(relaxed = true))
            every { observeNetworkConnectivityUseCase() } returns flowOf(NetworkConnectionState.Available)
            viewModel = AppViewModel(
                fetchAppInfoUseCase,
                enableAdBlockingConditionalUseCase,
                disableAdBlockingConditionalUseCase,
                setConnectionAsActiveUseCase,
                observeNetworkConnectivityUseCase
            )

            // When
            viewModel.onEvent(AppContract.Event.ShowDisabledStatusDialog)

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.dialogType).isEqualTo(AppDialogType.DisableAdBlocking)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `GIVEN network is available WHEN observeNetworkStatus is called THEN uiState should update networkConnectionState`() =
        runTest {
            // Given
            coEvery { fetchAppInfoUseCase() } returns flowOf(mockk(relaxed = true))
            every { observeNetworkConnectivityUseCase() } returns flowOf(NetworkConnectionState.Available)
            viewModel = AppViewModel(
                fetchAppInfoUseCase,
                enableAdBlockingConditionalUseCase,
                disableAdBlockingConditionalUseCase,
                setConnectionAsActiveUseCase,
                observeNetworkConnectivityUseCase
            )

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.networkConnectionState.networkConnectionState)
                    .isEqualTo(NetworkConnectionState.Available)
                assertThat(result.networkConnectionState.isVisible).isFalse()
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `GIVEN network is unavailable WHEN observeNetworkStatus is called THEN uiState should update networkConnectionState`() =
        runTest {
            // Given
            coEvery { fetchAppInfoUseCase() } returns flowOf(mockk(relaxed = true))
            every { observeNetworkConnectivityUseCase() } returns flowOf(NetworkConnectionState.Unavailable)
            viewModel = AppViewModel(
                fetchAppInfoUseCase,
                enableAdBlockingConditionalUseCase,
                disableAdBlockingConditionalUseCase,
                setConnectionAsActiveUseCase,
                observeNetworkConnectivityUseCase
            )

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.networkConnectionState.networkConnectionState)
                    .isEqualTo(NetworkConnectionState.Unavailable)
                assertThat(result.networkConnectionState.isVisible).isTrue()
                cancelAndConsumeRemainingEvents()
            }
        }

//    @Test
//    fun `GIVEN network is available WHEN observeNetworkStatus is called THEN timer should start and set isVisible to false after 5 seconds`() =
//        runTest {
//            // Given
//            coEvery { fetchAppInfoUseCase() } returns flowOf(mockk(relaxed = true))
//            every { observeNetworkConnectivityUseCase() } coAnswers {
//                flowOf(NetworkConnectionState.Unavailable)
//                flowOf(NetworkConnectionState.Available)
//            }
//            viewModel = AppViewModel(
//                fetchAppInfoUseCase,
//                enableAdBlockingConditionalUseCase,
//                disableAdBlockingConditionalUseCase,
//                setConnectionAsActiveUseCase,
//                observeNetworkConnectivityUseCase
//            )
//
//            // Track emitted states
//            val emittedStates = mutableListOf<AppContract.UiState>()
//            val job = backgroundScope.launch {
//                viewModel.uiState.collect { emittedStates.add(it) }
//            }
//            runCurrent()
//            advanceUntilIdle()
//            advanceTimeBy(6_000)
//            job.cancel()
//
//            // Then
//            assertThat(emittedStates[0].networkConnectionState.isVisible).isFalse()
//            assertThat(emittedStates[1].networkConnectionState.isVisible).isFalse()
//            assertThat(emittedStates[2].networkConnectionState.isVisible).isFalse()
//        }
//
//    @Test
//    fun `GIVEN network is unavailable and then available WHEN observeNetworkStatus is called THEN timer should start and set isVisible to false after 5 seconds`() =
//        runTest {
//            // Given
//            coEvery { fetchAppInfoUseCase() } returns flowOf(mockk(relaxed = true))
//            every { observeNetworkConnectivityUseCase() } returns flowOf(
//                NetworkConnectionState.Unavailable,
//                NetworkConnectionState.Available
//            )
//            viewModel = AppViewModel(
//                fetchAppInfoUseCase,
//                enableAdBlockingConditionalUseCase,
//                disableAdBlockingConditionalUseCase,
//                setConnectionAsActiveUseCase,
//                observeNetworkConnectivityUseCase
//            )
//
//            // Then
//            viewModel.uiState.test {
//                assertThat(awaitItem().networkConnectionState.isVisible).isTrue()
//                assertThat(awaitItem().networkConnectionState.networkConnectionState)
//                .isEqualTo(NetworkConnectionState.Unavailable)
//                assertThat(awaitItem().networkConnectionState.isVisible).isTrue()
//                assertThat(awaitItem().networkConnectionState.networkConnectionState)
//                .isEqualTo(NetworkConnectionState.Available)
//                advanceTimeBy(5000)
//                assertThat(awaitItem().networkConnectionState.isVisible).isFalse()
//                cancelAndConsumeRemainingEvents()
//            }
//        }
//
//    @Test
//    fun `GIVEN network is available and then unavailable WHEN observeNetworkStatus is called THEN isVisible should be true`() =
//        runTest {
//            // Given
//            coEvery { fetchAppInfoUseCase() } returns flowOf(mockk(relaxed = true))
//            every { observeNetworkConnectivityUseCase() } returns flowOf(
//                NetworkConnectionState.Available,
//                NetworkConnectionState.Unavailable
//            )
//            viewModel = AppViewModel(
//                fetchAppInfoUseCase,
//                enableAdBlockingConditionalUseCase,
//                disableAdBlockingConditionalUseCase,
//                setConnectionAsActiveUseCase,
//                observeNetworkConnectivityUseCase
//            )
//
//            // Then
//            viewModel.uiState.test {
//                assertThat(awaitItem().networkConnectionState.isVisible).isTrue()
//                assertThat(awaitItem().networkConnectionState.networkConnectionState)
//                .isEqualTo(NetworkConnectionState.Available)
//                assertThat(awaitItem().networkConnectionState.isVisible).isTrue()
//                assertThat(awaitItem().networkConnectionState.networkConnectionState)
//                .isEqualTo(NetworkConnectionState.Unavailable)
//                cancelAndConsumeRemainingEvents()
//            }
//        }
}
