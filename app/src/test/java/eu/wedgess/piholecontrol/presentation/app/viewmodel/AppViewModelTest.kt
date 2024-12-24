package eu.wedgess.piholecontrol.presentation.app.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.StatusEntity
import eu.wedgess.piholecontrol.domain.usecases.app.DisableAdBlockingConditionalUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.EnableAdBlockingConditionalUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.FetchAppInfoUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.SetConnectionAsActiveUseCase
import eu.wedgess.piholecontrol.presentation.app.AppContract
import eu.wedgess.piholecontrol.presentation.app.model.AppBarState
import eu.wedgess.piholecontrol.presentation.app.model.AppDialogType
import eu.wedgess.piholecontrol.presentation.app.model.PiHoleAppInfo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AppViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    @RelaxedMockK
    private lateinit var fetchAppInfoUseCase: FetchAppInfoUseCase

    @RelaxedMockK
    private lateinit var enableAdBlockingConditionalUseCase: EnableAdBlockingConditionalUseCase

    @RelaxedMockK
    private lateinit var disableAdBlockingConditionalUseCase: DisableAdBlockingConditionalUseCase

    @RelaxedMockK
    private lateinit var setConnectionAsActiveUseCase: SetConnectionAsActiveUseCase

    private lateinit var viewModel: AppViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN viewmodel is initialized THEN uiState should emit initial state`() = runTest {
        // Given
        coEvery { fetchAppInfoUseCase() } returns flowOf(mockk(relaxed = true))

        // When
        viewModel = AppViewModel(
            fetchAppInfoUseCase,
            enableAdBlockingConditionalUseCase,
            disableAdBlockingConditionalUseCase,
            setConnectionAsActiveUseCase
        )

        // Then
        val result = viewModel.uiState.first()
        assertThat(result).isEqualTo(AppContract.UiState.initial())
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

            // When
            viewModel = AppViewModel(
                fetchAppInfoUseCase,
                enableAdBlockingConditionalUseCase,
                disableAdBlockingConditionalUseCase,
                setConnectionAsActiveUseCase
            )
            advanceUntilIdle()

            // Then
            val result = viewModel.uiState.first { it.appInfo.connections.isNotEmpty() }
            assertThat(result.appInfo).isEqualTo(appInfo)
            assertThat(result.appBarState.adBlockingEnabled).isTrue()
            assertThat(result.appBarState.currentConnection).isEqualTo(appInfo.currentConnection)
            assertThat(result.appBarState.connections).isEqualTo(appInfo.connections)
        }

    @Test
    fun `WHEN DismissDialog event is received THEN uiState should update dialogType to None`() =
        runTest {
            // Given
            coEvery { fetchAppInfoUseCase() } returns flowOf(mockk(relaxed = true))
            viewModel = AppViewModel(
                fetchAppInfoUseCase,
                enableAdBlockingConditionalUseCase,
                disableAdBlockingConditionalUseCase,
                setConnectionAsActiveUseCase
            )
            viewModel.onEvent(AppContract.Event.ShowEnabledStatusDialog)

            // When
            viewModel.onEvent(AppContract.Event.DismissDialog)

            // Then
            val result = viewModel.uiState.first { it.dialogType == AppDialogType.None }
            assertThat(result.dialogType).isEqualTo(AppDialogType.None)
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
            viewModel = AppViewModel(
                fetchAppInfoUseCase,
                enableAdBlockingConditionalUseCase,
                disableAdBlockingConditionalUseCase,
                setConnectionAsActiveUseCase
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
            viewModel = AppViewModel(
                fetchAppInfoUseCase,
                enableAdBlockingConditionalUseCase,
                disableAdBlockingConditionalUseCase,
                setConnectionAsActiveUseCase
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
            viewModel = AppViewModel(
                fetchAppInfoUseCase,
                enableAdBlockingConditionalUseCase,
                disableAdBlockingConditionalUseCase,
                setConnectionAsActiveUseCase
            )
            val newAppBarState = AppBarState(
                adBlockingEnabled = true,
                currentConnection = ConnectionEntity.default.copy(id = 1),
                connections = listOf(ConnectionEntity.default.copy(id = 1))
            )

            // When
            viewModel.onEvent(AppContract.Event.UpdateAppBarState(newAppBarState))
            advanceUntilIdle()

            // Then
            val result = viewModel.uiState.first { it.appBarState == newAppBarState }
            assertThat(result.appBarState).isEqualTo(newAppBarState)
        }

    @Test
    fun `WHEN SetEnabledStatus event is received THEN enableAdBlockingConditionalUseCase should be called`() =
        runTest {
            // Given
            coEvery { fetchAppInfoUseCase() } returns flowOf(mockk(relaxed = true))
            coEvery { enableAdBlockingConditionalUseCase() } returns Result.success(StatusEntity.ENABLED)
            viewModel = AppViewModel(
                fetchAppInfoUseCase,
                enableAdBlockingConditionalUseCase,
                disableAdBlockingConditionalUseCase,
                setConnectionAsActiveUseCase
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
            viewModel = AppViewModel(
                fetchAppInfoUseCase,
                enableAdBlockingConditionalUseCase,
                disableAdBlockingConditionalUseCase,
                setConnectionAsActiveUseCase
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
            viewModel = AppViewModel(
                fetchAppInfoUseCase,
                enableAdBlockingConditionalUseCase,
                disableAdBlockingConditionalUseCase,
                setConnectionAsActiveUseCase
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
            viewModel = AppViewModel(
                fetchAppInfoUseCase,
                enableAdBlockingConditionalUseCase,
                disableAdBlockingConditionalUseCase,
                setConnectionAsActiveUseCase
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
            viewModel = AppViewModel(
                fetchAppInfoUseCase,
                enableAdBlockingConditionalUseCase,
                disableAdBlockingConditionalUseCase,
                setConnectionAsActiveUseCase
            )

            // When
            viewModel.onEvent(AppContract.Event.ShowEnabledStatusDialog)

            // Then
            val result = viewModel.uiState.first { it.dialogType == AppDialogType.EnableAdBlocking }
            assertThat(result.dialogType).isEqualTo(AppDialogType.EnableAdBlocking)
        }

    @Test
    fun `WHEN ShowDisabledStatusDialog event is received THEN uiState should update dialogType to DisableAdBlocking`() =
        runTest {
            // Given
            coEvery { fetchAppInfoUseCase() } returns flowOf(mockk(relaxed = true))
            viewModel = AppViewModel(
                fetchAppInfoUseCase,
                enableAdBlockingConditionalUseCase,
                disableAdBlockingConditionalUseCase,
                setConnectionAsActiveUseCase
            )

            // When
            viewModel.onEvent(AppContract.Event.ShowDisabledStatusDialog)

            // Then
            val result =
                viewModel.uiState.first { it.dialogType == AppDialogType.DisableAdBlocking }
            assertThat(result.dialogType).isEqualTo(AppDialogType.DisableAdBlocking)
        }
}
