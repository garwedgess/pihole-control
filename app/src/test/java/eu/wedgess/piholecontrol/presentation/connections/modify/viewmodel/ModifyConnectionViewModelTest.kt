package eu.wedgess.piholecontrol.presentation.connections.modify.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.google.mlkit.vision.barcode.BarcodeScanner
import eu.wedgess.piholecontrol.MainDispatcherRule
import eu.wedgess.piholecontrol.SavedStateHandleRule
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.usecases.connections.AddConnectionUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.FetchConnectionByIdUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.UpdateConnectionUseCase
import eu.wedgess.piholecontrol.presentation.connections.modify.ModifyConnectionsContract
import eu.wedgess.piholecontrol.presentation.connections.modify.model.ModifyConnectionDialogType
import eu.wedgess.piholecontrol.presentation.navigation.Screens
import io.ktor.http.URLProtocol
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
    private lateinit var barcodeScanner: BarcodeScanner

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
                barcodeScanner,
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
            val connectionId = 21L
            savedStateHandleRule.setRoute(Screens.ModifyConnection(connectionId))
            val connection = ConnectionEntity.default.copy(id = connectionId)

            coEvery { fetchConnectionByIdUseCase(connectionId) } returns Result.success(connection)

            // When
            viewModel = ModifyConnectionViewModel(
                fetchConnectionByIdUseCase,
                addConnectionUseCase,
                updateConnectionUseCase,
                barcodeScanner,
                savedStateHandleRule.savedStateHandleMock
            )
            viewModel.onEvent(ModifyConnectionsContract.Event.FetchCurrentConnection)

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.currentConnection).isEqualTo(connection)
                assertThat(result.name).isEqualTo(connection.name)
                assertThat(result.host).isEqualTo(connection.host)
                assertThat(result.port).isEqualTo(connection.port)
                assertThat(result.protocol).isEqualTo(connection.protocol)
                assertThat(result.apiPath).isEqualTo(connection.apiPath)
                assertThat(result.apiToken).isEqualTo(connection.token)
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
            coEvery { addConnectionUseCase(any()) } returns Result.success(Unit)
            viewModel = ModifyConnectionViewModel(
                fetchConnectionByIdUseCase,
                addConnectionUseCase,
                updateConnectionUseCase,
                barcodeScanner,
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
            savedStateHandleRule.setRoute(Screens.ModifyConnection(12))
            coEvery { updateConnectionUseCase(any()) } returns Result.success(Unit)
            viewModel = ModifyConnectionViewModel(
                fetchConnectionByIdUseCase,
                addConnectionUseCase,
                updateConnectionUseCase,
                barcodeScanner,
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
    fun `WHEN OnOpenBarcodeScanner event is received THEN uiState should update dialogType to ApiTokenScanner`() =
        runTest {
            // Given
            savedStateHandleRule.setRoute(Screens.ModifyConnection(null))
            viewModel = ModifyConnectionViewModel(
                fetchConnectionByIdUseCase,
                addConnectionUseCase,
                updateConnectionUseCase,
                barcodeScanner,
                savedStateHandleRule.savedStateHandleMock
            )

            // When
            viewModel.onEvent(ModifyConnectionsContract.Event.OnOpenBarcodeScanner)

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.dialogType).isEqualTo(
                    ModifyConnectionDialogType.ApiTokenScanner(
                        barcodeScanner
                    )
                )
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnDismissDialog event is received THEN uiState should update dialogType to None`() =
        runTest {
            // Given
            savedStateHandleRule.setRoute(Screens.ModifyConnection(null))
            viewModel = ModifyConnectionViewModel(
                fetchConnectionByIdUseCase,
                addConnectionUseCase,
                updateConnectionUseCase,
                barcodeScanner,
                savedStateHandleRule.savedStateHandleMock
            )
            viewModel.onEvent(ModifyConnectionsContract.Event.OnOpenBarcodeScanner)

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
            barcodeScanner,
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
    fun `WHEN OnApiTokenChanged event is received THEN uiState should update apiToken and dialogType to None`() =
        runTest {
            // Given
            savedStateHandleRule.setRoute(Screens.ModifyConnection(null))
            viewModel = ModifyConnectionViewModel(
                fetchConnectionByIdUseCase,
                addConnectionUseCase,
                updateConnectionUseCase,
                barcodeScanner,
                savedStateHandleRule.savedStateHandleMock
            )
            val newApiToken = "new_api_token"

            // When
            viewModel.onEvent(ModifyConnectionsContract.Event.OnApiTokenChanged(newApiToken))

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.apiToken).isEqualTo(newApiToken)
                assertThat(result.dialogType).isEqualTo(ModifyConnectionDialogType.None)
                cancelAndConsumeRemainingEvents()
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
                barcodeScanner,
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
                barcodeScanner,
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
                barcodeScanner,
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
                barcodeScanner,
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
                barcodeScanner,
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
            barcodeScanner,
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
            barcodeScanner,
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
            barcodeScanner,
            savedStateHandleRule.savedStateHandleMock
        )
        val newPort = 8080

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
                barcodeScanner,
                savedStateHandleRule.savedStateHandleMock
            )
            val newProtocol = URLProtocol.HTTPS

            // When
            viewModel.onEvent(ModifyConnectionsContract.Event.OnProtocolChanged(newProtocol))

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.protocol).isEqualTo(newProtocol)
                assertThat(result.port).isEqualTo(newProtocol.defaultPort)
                cancelAndConsumeRemainingEvents()
            }
        }
}
