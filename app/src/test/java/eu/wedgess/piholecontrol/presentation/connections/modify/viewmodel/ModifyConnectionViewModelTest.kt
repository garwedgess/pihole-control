package eu.wedgess.piholecontrol.presentation.connections.modify.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.MainDispatcherRule
import eu.wedgess.piholecontrol.SavedStateHandleRule
import eu.wedgess.piholecontrol.domain.model.AuthSessionStatusEntity
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.usecases.auth.GenerateSessionIdUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.AddConnectionUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.FetchConnectionByIdUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.UpdateConnectionUseCase
import eu.wedgess.piholecontrol.presentation.connections.modify.ModifyConnectionsContract
import eu.wedgess.piholecontrol.presentation.connections.modify.model.ConnectionInputError
import eu.wedgess.piholecontrol.presentation.connections.modify.model.ModifyConnectionDialogType
import eu.wedgess.piholecontrol.presentation.navigation.Screens
import io.ktor.http.URLProtocol
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.UUID

@ExperimentalCoroutinesApi
class ModifyConnectionViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val savedStateHandleRule = SavedStateHandleRule()

    @RelaxedMockK
    private lateinit var fetchConnectionByIdUseCase: FetchConnectionByIdUseCase

    @RelaxedMockK
    private lateinit var addConnectionUseCase: AddConnectionUseCase

    @RelaxedMockK
    private lateinit var updateConnectionUseCase: UpdateConnectionUseCase

    @RelaxedMockK
    private lateinit var generateSessionIdUseCase: GenerateSessionIdUseCase

    private lateinit var viewModel: ModifyConnectionViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `WHEN viewmodel is initialized for adding THEN uiState should emit initial state`() =
        runTest {
            // Given
            savedStateHandleRule.setRoute(Screens.ModifyConnection(null))

            // When
            viewModel = ModifyConnectionViewModel(
                fetchConnectionByIdUseCase,
                addConnectionUseCase,
                updateConnectionUseCase,
                generateSessionIdUseCase,
                savedStateHandleRule.savedStateHandleMock
            )

            // Then
            viewModel.uiState.test {
                assertThat(awaitItem()).isEqualTo(ModifyConnectionsContract.UiState.initial())
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `GIVEN connectionId WHEN viewmodel is initialized for editing THEN uiState should emit updated state`() =
        runTest {
            // Given
            val connectionId = UUID.randomUUID()
            savedStateHandleRule.setRoute(Screens.ModifyConnection(connectionId.toString()))
            val connection = ConnectionEntity.default.copy(id = connectionId)

            coEvery { fetchConnectionByIdUseCase(connectionId) } returns Result.success(connection)

            // When
            viewModel = ModifyConnectionViewModel(
                fetchConnectionByIdUseCase,
                addConnectionUseCase,
                updateConnectionUseCase,
                generateSessionIdUseCase,
                savedStateHandleRule.savedStateHandleMock
            )
            viewModel.onEvent(ModifyConnectionsContract.Event.FetchCurrentConnection)

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.currentConnection).isEqualTo(connection)
                assertThat(result.name).isEqualTo(connection.name)
                assertThat(result.host).isEqualTo(connection.host)
                assertThat(result.port).isEqualTo(connection.port.toString())
                assertThat(result.protocol).isEqualTo(connection.protocol)
                assertThat(result.apiPath).isEqualTo(connection.apiPath)
                assertThat(result.authUsername).isEqualTo(connection.authUsername)
                assertThat(result.authPassword).isEqualTo(connection.authPassword)
                assertThat(result.authRealm).isEqualTo(connection.authRealm)
                assertThat(result.trustAllCerts).isEqualTo(connection.trustAllCerts)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN SaveConnection event is received for adding THEN addConnectionUseCase should be called and Navigation Back side effect should be emitted`() =
        runTest {
            // Given
            savedStateHandleRule.setRoute(Screens.ModifyConnection(null))
            val sessionResponse = mockk<AuthSessionStatusEntity>(relaxed = true).apply {
                every { this@apply.sid } returns "test_session_id"
            }
            coEvery { generateSessionIdUseCase(any()) } returns Result.success(sessionResponse)
            coEvery { addConnectionUseCase(any()) } returns Result.success(Unit)
            viewModel = ModifyConnectionViewModel(
                fetchConnectionByIdUseCase,
                addConnectionUseCase,
                updateConnectionUseCase,
                generateSessionIdUseCase,
                savedStateHandleRule.savedStateHandleMock
            )

            // When
            viewModel.onEvent(ModifyConnectionsContract.Event.SaveConnection)

            // Then
            coVerify { addConnectionUseCase(any()) }
            viewModel.sideEffect.test {
                assertThat(awaitItem())
                    .isInstanceOf(ModifyConnectionsContract.Effect.Navigation.Back::class.java)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN SaveConnection event is received for updating THEN updateConnectionUseCase should be called and Navigation Back side effect should be emitted`() =
        runTest {
            // Given
            savedStateHandleRule.setRoute(Screens.ModifyConnection(UUID.randomUUID().toString()))
            coEvery { updateConnectionUseCase(any()) } returns Result.success(Unit)
            viewModel = ModifyConnectionViewModel(
                fetchConnectionByIdUseCase,
                addConnectionUseCase,
                updateConnectionUseCase,
                generateSessionIdUseCase,
                savedStateHandleRule.savedStateHandleMock
            )

            // When
            viewModel.onEvent(ModifyConnectionsContract.Event.SaveConnection)

            // Then
            coVerify { updateConnectionUseCase(any()) }
            viewModel.sideEffect.test {
                assertThat(awaitItem())
                    .isInstanceOf(ModifyConnectionsContract.Effect.Navigation.Back::class.java)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnDismissDialog event is received THEN uiState should update dialogType to None`() =
        runTest {
            // Given
            coEvery { updateConnectionUseCase(any()) } returns Result.failure(RuntimeException())
            savedStateHandleRule.setRoute(Screens.ModifyConnection(UUID.randomUUID().toString()))
            viewModel = ModifyConnectionViewModel(
                fetchConnectionByIdUseCase,
                addConnectionUseCase,
                updateConnectionUseCase,
                generateSessionIdUseCase,
                savedStateHandleRule.savedStateHandleMock
            )
            viewModel.onEvent(ModifyConnectionsContract.Event.SaveConnection)

            // When
            viewModel.onEvent(ModifyConnectionsContract.Event.OnDismissDialog)

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.dialogType).isEqualTo(ModifyConnectionDialogType.None)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnApiPathChanged event is received THEN uiState should update apiPath`() = runTest {
        // Given
        savedStateHandleRule.setRoute(Screens.ModifyConnection(null))
        viewModel = ModifyConnectionViewModel(
            fetchConnectionByIdUseCase,
            addConnectionUseCase,
            updateConnectionUseCase,
            generateSessionIdUseCase,
            savedStateHandleRule.savedStateHandleMock
        )
        val newApiPath = "/new/api/path"

        // When
        viewModel.onEvent(ModifyConnectionsContract.Event.OnApiPathChanged(newApiPath))
        advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val result = awaitItem()
            assertThat(result.apiPath).isEqualTo(newApiPath)
        }
    }

    @Test
    fun `WHEN OnAuthUsernameChanged event is received THEN uiState should update authUsername`() =
        runTest {
            // Given
            savedStateHandleRule.setRoute(Screens.ModifyConnection(null))
            viewModel = ModifyConnectionViewModel(
                fetchConnectionByIdUseCase,
                addConnectionUseCase,
                updateConnectionUseCase,
                generateSessionIdUseCase,
                savedStateHandleRule.savedStateHandleMock
            )
            val newAuthUsername = "new_auth_username"

            // When
            viewModel.onEvent(
                ModifyConnectionsContract.Event.OnAuthUsernameChanged(newAuthUsername)
            )

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.authUsername).isEqualTo(newAuthUsername)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnAuthPasswordChanged event is received THEN uiState should update authPassword`() =
        runTest {
            // Given
            savedStateHandleRule.setRoute(Screens.ModifyConnection(null))
            viewModel = ModifyConnectionViewModel(
                fetchConnectionByIdUseCase,
                addConnectionUseCase,
                updateConnectionUseCase,
                generateSessionIdUseCase,
                savedStateHandleRule.savedStateHandleMock
            )
            val newAuthPassword = "new_auth_password"

            // When
            viewModel.onEvent(
                ModifyConnectionsContract.Event.OnAuthPasswordChanged(newAuthPassword)
            )

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.authPassword).isEqualTo(newAuthPassword)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnAuthRealmChanged event is received THEN uiState should update authRealm`() =
        runTest {
            // Given
            savedStateHandleRule.setRoute(Screens.ModifyConnection(null))
            viewModel = ModifyConnectionViewModel(
                fetchConnectionByIdUseCase,
                addConnectionUseCase,
                updateConnectionUseCase,
                generateSessionIdUseCase,
                savedStateHandleRule.savedStateHandleMock
            )
            val newAuthRealm = "new_auth_realm"

            // When
            viewModel.onEvent(ModifyConnectionsContract.Event.OnAuthRealmChanged(newAuthRealm))

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.authRealm).isEqualTo(newAuthRealm)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnTrustAllCertsChanged event is received THEN uiState should update trustAllCerts`() =
        runTest {
            // Given
            savedStateHandleRule.setRoute(Screens.ModifyConnection(null))
            viewModel = ModifyConnectionViewModel(
                fetchConnectionByIdUseCase,
                addConnectionUseCase,
                updateConnectionUseCase,
                generateSessionIdUseCase,
                savedStateHandleRule.savedStateHandleMock
            )
            val newTrustAllCerts = true

            // When
            viewModel.onEvent(
                ModifyConnectionsContract.Event.OnTrustAllCertsChanged(
                    newTrustAllCerts
                )
            )

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.trustAllCerts).isEqualTo(newTrustAllCerts)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnToggleAdvancedSettingsChanged event is received THEN uiState should update showAdvancedSettings`() =
        runTest {
            // Given
            savedStateHandleRule.setRoute(Screens.ModifyConnection(null))
            viewModel = ModifyConnectionViewModel(
                fetchConnectionByIdUseCase,
                addConnectionUseCase,
                updateConnectionUseCase,
                generateSessionIdUseCase,
                savedStateHandleRule.savedStateHandleMock
            )
            val newShowAdvancedSettings = true

            // When
            viewModel.onEvent(
                ModifyConnectionsContract.Event.OnToggleAdvancedSettingsChanged(
                    newShowAdvancedSettings
                )
            )

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.showAdvancedSettings).isEqualTo(newShowAdvancedSettings)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnHostChanged event is received THEN uiState should update host`() = runTest {
        // Given
        savedStateHandleRule.setRoute(Screens.ModifyConnection(null))
        viewModel = ModifyConnectionViewModel(
            fetchConnectionByIdUseCase,
            addConnectionUseCase,
            updateConnectionUseCase,
            generateSessionIdUseCase,
            savedStateHandleRule.savedStateHandleMock
        )
        val newHost = "new_host"

        // When
        viewModel.onEvent(ModifyConnectionsContract.Event.OnHostChanged(newHost))

        // Then
        viewModel.uiState.test {
            val result = awaitItem()
            assertThat(result.host).isEqualTo(newHost)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `WHEN OnNameChanged event is received THEN uiState should update name`() = runTest {
        // Given
        savedStateHandleRule.setRoute(Screens.ModifyConnection(null))
        viewModel = ModifyConnectionViewModel(
            fetchConnectionByIdUseCase,
            addConnectionUseCase,
            updateConnectionUseCase,
            generateSessionIdUseCase,
            savedStateHandleRule.savedStateHandleMock
        )
        val newName = "new_name"

        // When
        viewModel.onEvent(ModifyConnectionsContract.Event.OnNameChanged(newName))

        // Then
        viewModel.uiState.test {
            val result = awaitItem()
            assertThat(result.name).isEqualTo(newName)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `WHEN OnPortChanged event is received THEN uiState should update port`() = runTest {
        // Given
        savedStateHandleRule.setRoute(Screens.ModifyConnection(null))
        viewModel = ModifyConnectionViewModel(
            fetchConnectionByIdUseCase,
            addConnectionUseCase,
            updateConnectionUseCase,
            generateSessionIdUseCase,
            savedStateHandleRule.savedStateHandleMock
        )
        val newPort = 8080.toString()

        // When
        viewModel.onEvent(ModifyConnectionsContract.Event.OnPortChanged(newPort))

        // Then
        viewModel.uiState.test {
            val result = awaitItem()
            assertThat(result.port).isEqualTo(newPort)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `WHEN OnProtocolChanged event is received THEN uiState should update protocol and port`() =
        runTest {
            // Given
            savedStateHandleRule.setRoute(Screens.ModifyConnection(null))
            viewModel = ModifyConnectionViewModel(
                fetchConnectionByIdUseCase,
                addConnectionUseCase,
                updateConnectionUseCase,
                generateSessionIdUseCase,
                savedStateHandleRule.savedStateHandleMock
            )
            val newProtocol = URLProtocol.HTTPS

            // When
            viewModel.onEvent(ModifyConnectionsContract.Event.OnProtocolChanged(newProtocol))

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.protocol).isEqualTo(newProtocol)
                assertThat(result.port).isEqualTo(newProtocol.defaultPort.toString())
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnPasswordFieldVisibilityChanged event is received THEN uiState should update apiKeyPasswordVisible`() =
        runTest {
            // Given
            savedStateHandleRule.setRoute(Screens.ModifyConnection(null))
            viewModel = ModifyConnectionViewModel(
                fetchConnectionByIdUseCase,
                addConnectionUseCase,
                updateConnectionUseCase,
                generateSessionIdUseCase,
                savedStateHandleRule.savedStateHandleMock
            )
            val newVisibility = true

            // When
            viewModel.onEvent(
                ModifyConnectionsContract.Event.OnPasswordFieldVisibilityChanged(
                    newVisibility
                )
            )

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.passwordVisible).isEqualTo(newVisibility)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnBasicPasswordFieldVisibilityChanged event is received THEN uiState should update basicAuthPasswordVisible`() =
        runTest {
            // Given
            savedStateHandleRule.setRoute(Screens.ModifyConnection(null))
            viewModel = ModifyConnectionViewModel(
                fetchConnectionByIdUseCase,
                addConnectionUseCase,
                updateConnectionUseCase,
                generateSessionIdUseCase,
                savedStateHandleRule.savedStateHandleMock
            )
            val newVisibility = true

            // When
            viewModel.onEvent(
                ModifyConnectionsContract.Event.OnBasicPasswordFieldVisibilityChanged(
                    newVisibility
                )
            )

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.basicAuthPasswordVisible).isEqualTo(newVisibility)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnPasswordChanged event is received with valid password THEN uiState should update password without error`() =
        runTest {
            // Given
            savedStateHandleRule.setRoute(Screens.ModifyConnection(null))
            viewModel = ModifyConnectionViewModel(
                fetchConnectionByIdUseCase,
                addConnectionUseCase,
                updateConnectionUseCase,
                generateSessionIdUseCase,
                savedStateHandleRule.savedStateHandleMock
            )
            val validPassword = "validPassword"

            // When
            viewModel.onEvent(ModifyConnectionsContract.Event.OnPasswordChanged(validPassword))

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.password).isEqualTo(validPassword)
                assertThat(result.inputErrors).isEmpty()
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnPasswordChanged event is received with blank password THEN uiState should update with error`() =
        runTest {
            // Given
            savedStateHandleRule.setRoute(Screens.ModifyConnection(null))
            viewModel = ModifyConnectionViewModel(
                fetchConnectionByIdUseCase,
                addConnectionUseCase,
                updateConnectionUseCase,
                generateSessionIdUseCase,
                savedStateHandleRule.savedStateHandleMock
            )
            val blankPassword = ""

            // When
            viewModel.onEvent(ModifyConnectionsContract.Event.OnPasswordChanged(blankPassword))

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.password).isEqualTo(blankPassword)
                assertThat(result.inputErrors).hasSize(1)
                assertThat(result.inputErrors.first()).isInstanceOf(ConnectionInputError.Password::class.java)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN saving Version6 connection fails with password error THEN uiState should update with password error`() =
        runTest {
            // Given
            savedStateHandleRule.setRoute(Screens.ModifyConnection(null))
            viewModel = ModifyConnectionViewModel(
                fetchConnectionByIdUseCase,
                addConnectionUseCase,
                updateConnectionUseCase,
                generateSessionIdUseCase,
                savedStateHandleRule.savedStateHandleMock
            )

            // Mock generate session failure
            coEvery { generateSessionIdUseCase(any()) } returns Result.failure(
                Exception("password incorrect")
            )

            // When
            viewModel.onEvent(ModifyConnectionsContract.Event.SaveConnection)

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.dialogType).isInstanceOf(ModifyConnectionDialogType.FailedToSaveConnection::class.java)
                assertThat(result.inputErrors.first()).isInstanceOf(ConnectionInputError.Password::class.java)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN saving Version6 connection fails with other error THEN uiState should show error dialog without password error`() =
        runTest {
            // Given
            savedStateHandleRule.setRoute(Screens.ModifyConnection(null))
            viewModel = ModifyConnectionViewModel(
                fetchConnectionByIdUseCase,
                addConnectionUseCase,
                updateConnectionUseCase,
                generateSessionIdUseCase,
                savedStateHandleRule.savedStateHandleMock
            )

            // Mock generate session failure
            coEvery { generateSessionIdUseCase(any()) } returns Result.failure(
                Exception("network error")
            )

            // When
            viewModel.onEvent(ModifyConnectionsContract.Event.SaveConnection)

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.dialogType).isInstanceOf(ModifyConnectionDialogType.FailedToSaveConnection::class.java)
                assertThat(result.inputErrors).isEmpty()
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN saving Version6 connection succeeds THEN connection should be saved with session ID`() =
        runTest {
            // Given
            savedStateHandleRule.setRoute(Screens.ModifyConnection(null))
            viewModel = ModifyConnectionViewModel(
                fetchConnectionByIdUseCase,
                addConnectionUseCase,
                updateConnectionUseCase,
                generateSessionIdUseCase,
                savedStateHandleRule.savedStateHandleMock
            )

            // Mock successful session generation
            val sessionResponse = mockk<AuthSessionStatusEntity>(relaxed = true).apply {
                every { this@apply.sid } returns "test_session_id"
            }
            coEvery { generateSessionIdUseCase(any()) } returns Result.success(sessionResponse)
            coEvery { addConnectionUseCase(any()) } returns Result.success(Unit)

            // When
            viewModel.onEvent(ModifyConnectionsContract.Event.SaveConnection)

            // Then
            coVerify {
                generateSessionIdUseCase(any())
                addConnectionUseCase(match { it is ConnectionEntity && it.sid == "test_session_id" })
            }
            viewModel.sideEffect.test {
                assertThat(awaitItem()).isInstanceOf(ModifyConnectionsContract.Effect.Navigation.Back::class.java)
                cancelAndConsumeRemainingEvents()
            }
        }
}
