package eu.wedgess.piholecontrol.presentation.connections.list.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.MainDispatcherRule
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.usecases.connections.DeleteConnectionMarkedForDeletionUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.FetchAllConnectionsUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.MarkConnectionForDeletionUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.SetConnectionAsActiveUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.UnMarkConnectionForDeletionUseCase
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.connections.list.ConnectionsContract
import eu.wedgess.piholecontrol.utils.UiText
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ConnectionsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @RelaxedMockK
    private lateinit var fetchAllConnectionsUseCase: FetchAllConnectionsUseCase

    @RelaxedMockK
    private lateinit var setConnectionAsActiveUseCase: SetConnectionAsActiveUseCase

    @RelaxedMockK
    private lateinit var deleteConnectionMarkedForDeletionUseCase: DeleteConnectionMarkedForDeletionUseCase

    @RelaxedMockK
    private lateinit var markConnectionForDeletionUseCase: MarkConnectionForDeletionUseCase

    @RelaxedMockK
    private lateinit var unMarkConnectionForDeletionUseCase: UnMarkConnectionForDeletionUseCase

    private lateinit var viewModel: ConnectionsViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `WHEN viewmodel is initialized THEN uiResult should emit Loading state initially`() =
        runTest {
            // When
            viewModel = ConnectionsViewModel(
                fetchAllConnectionsUseCase,
                setConnectionAsActiveUseCase,
                deleteConnectionMarkedForDeletionUseCase,
                markConnectionForDeletionUseCase,
                unMarkConnectionForDeletionUseCase
            )

            // Then
            val result = viewModel.uiResult.first()
            assertThat(result).isInstanceOf(UIResult.Loading::class.java)
            assertThat((result as UIResult.Loading).loadingType)
                .isEqualTo(ResultType.Loading.WithTitle())
        }

    @Test
    fun `GIVEN usecase returns success WHEN viewmodel is initialized THEN uiResult should emit Loaded state`() =
        runTest {
            // Given
            val connections = listOf(mockk<ConnectionEntity>(relaxed = true))
            coEvery { fetchAllConnectionsUseCase() } returns flowOf(Result.success(connections))

            // When
            viewModel = ConnectionsViewModel(
                fetchAllConnectionsUseCase,
                setConnectionAsActiveUseCase,
                deleteConnectionMarkedForDeletionUseCase,
                markConnectionForDeletionUseCase,
                unMarkConnectionForDeletionUseCase
            )
            advanceUntilIdle()

            // Then
            val result = viewModel.uiResult.first { it is UIResult.Loaded }
            assertThat(result).isInstanceOf(UIResult.Loaded::class.java)
            assertThat((result as UIResult.Loaded).data.connections).isEqualTo(connections)
        }

    @Test
    fun `GIVEN usecase returns success with empty list WHEN viewmodel is initialized THEN uiResult should emit Empty state`() =
        runTest {
            // Given
            coEvery { fetchAllConnectionsUseCase() } returns flowOf(Result.success(emptyList()))

            // When
            viewModel = ConnectionsViewModel(
                fetchAllConnectionsUseCase,
                setConnectionAsActiveUseCase,
                deleteConnectionMarkedForDeletionUseCase,
                markConnectionForDeletionUseCase,
                unMarkConnectionForDeletionUseCase
            )
            advanceUntilIdle()

            // Then
            val result = viewModel.uiResult.first { it is UIResult.Empty }
            assertThat(result).isInstanceOf(UIResult.Empty::class.java)
            assertThat((result as UIResult.Empty).emptyType)
                .isInstanceOf(ResultType.Empty.WithTitleAndSubTitle::class.java)
            assertThat((result.emptyType as ResultType.Empty.WithTitleAndSubTitle).title)
                .isEqualTo(UiText.StringResource(R.string.connections_empty_title))
            assertThat((result.emptyType as ResultType.Empty.WithTitleAndSubTitle).subTitle)
                .isEqualTo(UiText.StringResource(R.string.connections_empty_message))
        }

    @Test
    fun `GIVEN usecase returns failure WHEN viewmodel is initialized THEN uiResult should emit Error state`() =
        runTest {
            // Given
            val exception = Exception("Test Exception")
            coEvery { fetchAllConnectionsUseCase() } returns flowOf(Result.failure(exception))

            // When
            viewModel = ConnectionsViewModel(
                fetchAllConnectionsUseCase,
                setConnectionAsActiveUseCase,
                deleteConnectionMarkedForDeletionUseCase,
                markConnectionForDeletionUseCase,
                unMarkConnectionForDeletionUseCase
            )
            advanceUntilIdle()

            // Then
            val result = viewModel.uiResult.first { it is UIResult.Error }
            assertThat(result).isInstanceOf(UIResult.Error::class.java)
            assertThat((result as UIResult.Error).errorType)
                .isInstanceOf(ResultType.Error.WithTitleAndSubTitle::class.java)
            assertThat((result.errorType as ResultType.Error.WithTitleAndSubTitle).title)
                .isEqualTo(UiText.StringResource(R.string.connections_error_title))
            assertThat((result.errorType as ResultType.Error.WithTitleAndSubTitle).subTitle)
                .isEqualTo(
                    UiText.DynamicString(exception.message ?: "Unknown error")
                )
        }

    @Test
    fun `WHEN AddConnection event is received THEN Navigation Add side effect should be emitted`() =
        runTest {
            // Given
            coEvery { fetchAllConnectionsUseCase() } returns flowOf(Result.success(emptyList()))
            viewModel = ConnectionsViewModel(
                fetchAllConnectionsUseCase,
                setConnectionAsActiveUseCase,
                deleteConnectionMarkedForDeletionUseCase,
                markConnectionForDeletionUseCase,
                unMarkConnectionForDeletionUseCase
            )
            val sideEffects = mutableListOf<ConnectionsContract.Effect>()
            backgroundScope.launch {
                viewModel.sideEffect.toList(sideEffects)
            }

            // When
            viewModel.onEvent(ConnectionsContract.Event.AddConnection)
            advanceUntilIdle()

            // Then
            assertThat(sideEffects).containsExactly(ConnectionsContract.Effect.Navigation.Add)
        }

    @Test
    fun `WHEN SetActive event is received THEN setConnectionAsActiveUseCase should be called`() =
        runTest {
            // Given
            val connectionId = 1L
            val connectionName = "Test Connection"
            coEvery { fetchAllConnectionsUseCase() } returns flowOf(Result.success(emptyList()))
            coEvery { setConnectionAsActiveUseCase(connectionId) } returns Result.success(Unit)
            viewModel = ConnectionsViewModel(
                fetchAllConnectionsUseCase,
                setConnectionAsActiveUseCase,
                deleteConnectionMarkedForDeletionUseCase,
                markConnectionForDeletionUseCase,
                unMarkConnectionForDeletionUseCase
            )

            // When
            viewModel.onEvent(ConnectionsContract.Event.SetActive(connectionId, connectionName))
            advanceUntilIdle()

            // Then
            coVerify { setConnectionAsActiveUseCase(connectionId) }
        }

    @Test
    fun `WHEN EditConnection event is received THEN Navigation Edit side effect should be emitted`() =
        runTest {
            // Given
            val connectionId = 1L
            coEvery { fetchAllConnectionsUseCase() } returns flowOf(Result.success(emptyList()))
            viewModel = ConnectionsViewModel(
                fetchAllConnectionsUseCase,
                setConnectionAsActiveUseCase,
                deleteConnectionMarkedForDeletionUseCase,
                markConnectionForDeletionUseCase,
                unMarkConnectionForDeletionUseCase
            )
            val sideEffects = mutableListOf<ConnectionsContract.Effect>()
            backgroundScope.launch {
                viewModel.sideEffect.toList(sideEffects)
            }

            // When
            viewModel.onEvent(ConnectionsContract.Event.EditConnection(connectionId))
            advanceUntilIdle()

            // Then
            assertThat(sideEffects).containsExactly(
                ConnectionsContract.Effect.Navigation.Edit(
                    connectionId
                )
            )
        }

    @Test
    fun `WHEN CompleteDeleteConnection event is received THEN deleteConnectionMarkedForDeletionUseCase should be called`() =
        runTest {
            // Given
            val connectionName = "Test Connection"
            coEvery { fetchAllConnectionsUseCase() } returns flowOf(Result.success(emptyList()))
            coEvery { deleteConnectionMarkedForDeletionUseCase() } returns Result.success(Unit)
            viewModel = ConnectionsViewModel(
                fetchAllConnectionsUseCase,
                setConnectionAsActiveUseCase,
                deleteConnectionMarkedForDeletionUseCase,
                markConnectionForDeletionUseCase,
                unMarkConnectionForDeletionUseCase
            )

            // When
            viewModel.onEvent(ConnectionsContract.Event.CompleteDeleteConnection(connectionName))
            advanceUntilIdle()

            // Then
            coVerify { deleteConnectionMarkedForDeletionUseCase() }
        }

    @Test
    fun `WHEN UndoDeleteConnection event is received THEN unMarkConnectionForDeletionUseCase should be called`() =
        runTest {
            // Given
            val connectionId = 1L
            val connectionName = "Test Connection"
            coEvery { fetchAllConnectionsUseCase() } returns flowOf(Result.success(emptyList()))
            coEvery {
                unMarkConnectionForDeletionUseCase(connectionId)
            } returns Result.success(Unit)
            viewModel = ConnectionsViewModel(
                fetchAllConnectionsUseCase,
                setConnectionAsActiveUseCase,
                deleteConnectionMarkedForDeletionUseCase,
                markConnectionForDeletionUseCase,
                unMarkConnectionForDeletionUseCase
            )

            // When
            viewModel.onEvent(
                ConnectionsContract.Event.UndoDeleteConnection(
                    connectionId,
                    connectionName
                )
            )
            advanceUntilIdle()

            // Then
            coVerify { unMarkConnectionForDeletionUseCase(connectionId) }
        }

    @Test
    fun `GIVEN usecase returns failure WHEN SetActive event is received THEN SetActiveConnectionFailed side effect should be emitted`() =
        runTest {
            // Given
            val connectionId = 1L
            val connectionName = "Test Connection"
            coEvery { fetchAllConnectionsUseCase() } returns flowOf(Result.success(emptyList()))
            coEvery { setConnectionAsActiveUseCase(connectionId) } returns Result.failure(
                Exception(
                    "Test"
                )
            )
            viewModel = ConnectionsViewModel(
                fetchAllConnectionsUseCase,
                setConnectionAsActiveUseCase,
                deleteConnectionMarkedForDeletionUseCase,
                markConnectionForDeletionUseCase,
                unMarkConnectionForDeletionUseCase
            )

            // When
            viewModel.onEvent(ConnectionsContract.Event.SetActive(connectionId, connectionName))
            advanceUntilIdle()

            // Then
            val sideEffect = viewModel.sideEffect.first()
            assertThat(sideEffect)
                .isInstanceOf(
                    ConnectionsContract.Effect.Snackbar.SetActiveConnectionFailed::class.java
                )
            (sideEffect as ConnectionsContract.Effect.Snackbar.SetActiveConnectionFailed).run {
                assertThat(id).isEqualTo(connectionId)
                assertThat(name).isEqualTo(connectionName)
                assertThat(message).isInstanceOf(UiText.StringResourceWithArgs::class.java)
            }
        }

    @Test
    fun `GIVEN usecase returns failure WHEN DeleteConnection event is received THEN DeleteConnectionFailed side effect should be emitted`() =
        runTest {
            // Given
            val connectionId = 1L
            val connectionName = "Test Connection"
            coEvery { fetchAllConnectionsUseCase() } returns flowOf(Result.success(emptyList()))
            coEvery { markConnectionForDeletionUseCase(connectionId) } returns Result.failure(
                Exception("Test")
            )
            viewModel = ConnectionsViewModel(
                fetchAllConnectionsUseCase,
                setConnectionAsActiveUseCase,
                deleteConnectionMarkedForDeletionUseCase,
                markConnectionForDeletionUseCase,
                unMarkConnectionForDeletionUseCase
            )

            // When
            viewModel.onEvent(
                ConnectionsContract.Event.DeleteConnection(
                    connectionId,
                    connectionName
                )
            )
            advanceUntilIdle()

            // Then
            val sideEffect = viewModel.sideEffect.first()
            assertThat(sideEffect)
                .isInstanceOf(ConnectionsContract.Effect.Snackbar.DeleteConnectionFailed::class.java)
            assertThat(
                (sideEffect as ConnectionsContract.Effect.Snackbar.DeleteConnectionFailed).name
            ).isEqualTo(connectionName)
        }

    @Test
    fun `GIVEN usecase returns success WHEN DeleteConnection event is received THEN DeleteConnection side effect should be emitted`() =
        runTest {
            // Given
            val connectionId = 1L
            val connectionName = "Test Connection"
            coEvery { fetchAllConnectionsUseCase() } returns flowOf(Result.success(emptyList()))
            coEvery { markConnectionForDeletionUseCase(connectionId) } returns Result.success(Unit)
            viewModel = ConnectionsViewModel(
                fetchAllConnectionsUseCase,
                setConnectionAsActiveUseCase,
                deleteConnectionMarkedForDeletionUseCase,
                markConnectionForDeletionUseCase,
                unMarkConnectionForDeletionUseCase
            )

            // When
            viewModel.onEvent(
                ConnectionsContract.Event.DeleteConnection(
                    connectionId,
                    connectionName
                )
            )
            advanceUntilIdle()

            // Then
            val sideEffect = viewModel.sideEffect.first()
            assertThat(sideEffect)
                .isInstanceOf(ConnectionsContract.Effect.Snackbar.DeleteConnection::class.java)
            assertThat((sideEffect as ConnectionsContract.Effect.Snackbar.DeleteConnection).id)
                .isEqualTo(connectionId)
            assertThat(sideEffect.name).isEqualTo(connectionName)
        }

    @Test
    fun `GIVEN usecase returns failure WHEN CompleteDeleteConnection event is received THEN DeleteConnectionFailed side effect should be emitted`() =
        runTest {
            // Given
            val connectionName = "Test Connection"
            coEvery { fetchAllConnectionsUseCase() } returns flowOf(Result.success(emptyList()))
            coEvery { deleteConnectionMarkedForDeletionUseCase() } returns Result.failure(
                Exception(
                    "Test"
                )
            )
            viewModel = ConnectionsViewModel(
                fetchAllConnectionsUseCase,
                setConnectionAsActiveUseCase,
                deleteConnectionMarkedForDeletionUseCase,
                markConnectionForDeletionUseCase,
                unMarkConnectionForDeletionUseCase
            )

            // When
            viewModel.onEvent(ConnectionsContract.Event.CompleteDeleteConnection(connectionName))
            advanceUntilIdle()

            // Then
            val sideEffect = viewModel.sideEffect.first()
            assertThat(sideEffect)
                .isInstanceOf(
                    ConnectionsContract.Effect.Snackbar.DeleteConnectionFailed::class.java
                )
            assertThat(
                (sideEffect as ConnectionsContract.Effect.Snackbar.DeleteConnectionFailed).message
            )
                .isInstanceOf(UiText.StringResourceWithArgs::class.java)
            assertThat(sideEffect.name).isEqualTo(connectionName)
        }

    @Test
    fun `GIVEN usecase returns failure WHEN UndoDeleteConnection event is received THEN RestoreConnectionFailed side effect should be emitted`() =
        runTest {
            // Given
            val connectionId = 1L
            val connectionName = "Test Connection"
            coEvery { fetchAllConnectionsUseCase() } returns flowOf(Result.success(emptyList()))
            coEvery { unMarkConnectionForDeletionUseCase(connectionId) } returns Result.failure(
                Exception("Test")
            )
            viewModel = ConnectionsViewModel(
                fetchAllConnectionsUseCase,
                setConnectionAsActiveUseCase,
                deleteConnectionMarkedForDeletionUseCase,
                markConnectionForDeletionUseCase,
                unMarkConnectionForDeletionUseCase
            )

            // When
            viewModel.onEvent(
                ConnectionsContract.Event.UndoDeleteConnection(
                    connectionId,
                    connectionName
                )
            )
            advanceUntilIdle()

            // Then
            val sideEffect = viewModel.sideEffect.first()
            assertThat(sideEffect)
                .isInstanceOf(
                    ConnectionsContract.Effect.Snackbar.RestoreConnectionFailed::class.java
                )
            assertThat(
                (sideEffect as ConnectionsContract.Effect.Snackbar.RestoreConnectionFailed).id
            )
                .isEqualTo(connectionId)
            assertThat(sideEffect.name).isEqualTo(connectionName)
        }
}
