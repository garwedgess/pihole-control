package eu.wedgess.piholecontrol.presentation.settings.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.MainDispatcherRule
import eu.wedgess.piholecontrol.domain.model.AppPreferencesEntity
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.usecases.ObserveActiveUserUseCase
import eu.wedgess.piholecontrol.domain.usecases.settings.FetchAppPreferencesUseCase
import eu.wedgess.piholecontrol.domain.usecases.settings.UpdateDynamicThemeUseCase
import eu.wedgess.piholecontrol.domain.usecases.settings.UpdateRefreshIntervalUseCase
import eu.wedgess.piholecontrol.domain.usecases.settings.UpdateSelectedThemeUseCase
import eu.wedgess.piholecontrol.domain.usecases.settings.UpdateStatusChangeOnAllConnectionsUseCase
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.settings.SettingsContract
import eu.wedgess.piholecontrol.presentation.settings.model.AppThemePres
import eu.wedgess.piholecontrol.presentation.settings.model.SettingsDialogType
import eu.wedgess.piholecontrol.presentation.settings.model.mapToEntity
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SettingsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(UnconfinedTestDispatcher())

    @RelaxedMockK
    private lateinit var fetchAppPreferencesUseCase: FetchAppPreferencesUseCase

    @RelaxedMockK
    private lateinit var observeActiveUserUseCase: ObserveActiveUserUseCase

    @RelaxedMockK
    private lateinit var updateRefreshIntervalUseCase: UpdateRefreshIntervalUseCase

    @RelaxedMockK
    private lateinit var updateDynamicThemeUseCase: UpdateDynamicThemeUseCase

    @RelaxedMockK
    private lateinit var updateSelectedThemeUseCase: UpdateSelectedThemeUseCase

    @RelaxedMockK
    private lateinit var updateStatusChangeOnAllConnectionsUseCase: UpdateStatusChangeOnAllConnectionsUseCase

    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `WHEN viewmodel is initialized THEN uiResult should emit Loading state initially`() =
        runTest {
            // When
            viewModel = SettingsViewModel(
                fetchAppPreferencesUseCase,
                observeActiveUserUseCase,
                updateRefreshIntervalUseCase,
                updateDynamicThemeUseCase,
                updateSelectedThemeUseCase,
                updateStatusChangeOnAllConnectionsUseCase
            )

            // Then
            val result = viewModel.uiResult.first()
            assertThat(result).isInstanceOf(UIResult.Loading::class.java)
            assertThat((result as UIResult.Loading).loadingType)
                .isEqualTo(ResultType.Loading.WithTitle())
        }

    @Test
    fun `WHEN viewmodel is initialized THEN uiResult should emit Loaded state`() =
        runTest {
            // Given
            val connection =
                ConnectionEntity.default.copy(
                    name = "Test",
                    host = "test.com",
                    port = 80,
                    token = "test"
                )
            val preferences = AppPreferencesEntity.default
            every { observeActiveUserUseCase() } returns flowOf(Result.success(connection))
            every { fetchAppPreferencesUseCase() } returns flowOf((preferences))

            // When
            viewModel = SettingsViewModel(
                fetchAppPreferencesUseCase,
                observeActiveUserUseCase,
                updateRefreshIntervalUseCase,
                updateDynamicThemeUseCase,
                updateSelectedThemeUseCase,
                updateStatusChangeOnAllConnectionsUseCase
            )
            val uiResults = mutableListOf<UIResult<SettingsContract.UiState>>()
            backgroundScope.launch {
                viewModel.uiResult.toList(uiResults)
            }
            advanceUntilIdle()

            // Then
            assertThat(uiResults.any { it is UIResult.Loaded }).isTrue()
            val loadedResult = uiResults.first { it is UIResult.Loaded } as UIResult.Loaded
            assertThat(loadedResult.data.currentConnection).isEqualTo(connection)
            assertThat(loadedResult.data.currentTheme).isEqualTo(AppThemePres.Dark)
            assertThat(loadedResult.data.refreshInterval).isEqualTo(preferences.refreshInterval)
            assertThat(loadedResult.data.useDynamicThemeColors)
                .isEqualTo(preferences.useDynamicColors)
            assertThat(loadedResult.data.changeStatusOnAllConnections)
                .isEqualTo(preferences.multiStatusChange)
        }

    @Test
    fun `WHEN OnRefreshIntervalClicked event is received THEN uiState should be updated`() =
        runTest {
            every { observeActiveUserUseCase() } returns flowOf(
                Result.success(ConnectionEntity.default)
            )
            every { fetchAppPreferencesUseCase() } returns flowOf(AppPreferencesEntity.default)

            // Given
            viewModel = SettingsViewModel(
                fetchAppPreferencesUseCase,
                observeActiveUserUseCase,
                updateRefreshIntervalUseCase,
                updateDynamicThemeUseCase,
                updateSelectedThemeUseCase,
                updateStatusChangeOnAllConnectionsUseCase
            )

            // When
            viewModel.onEvent(SettingsContract.Event.OnRefreshIntervalClicked)
            advanceUntilIdle()

            // Then
            val loadedResult = viewModel.uiResult.first { it is UIResult.Loaded } as UIResult.Loaded
            assertThat(loadedResult.data.dialogType)
                .isInstanceOf(SettingsDialogType.RefreshInterval::class.java)
        }

    @Test
    fun `WHEN OnServerClicked event is received THEN side effect should be emitted`() =
        runTest {
            every { observeActiveUserUseCase() } returns flowOf(
                Result.success(ConnectionEntity.default)
            )
            every { fetchAppPreferencesUseCase() } returns flowOf(AppPreferencesEntity.default)
            // Given
            viewModel = SettingsViewModel(
                fetchAppPreferencesUseCase,
                observeActiveUserUseCase,
                updateRefreshIntervalUseCase,
                updateDynamicThemeUseCase,
                updateSelectedThemeUseCase,
                updateStatusChangeOnAllConnectionsUseCase
            )

            // When
            viewModel.onEvent(SettingsContract.Event.OnServerClicked)
            advanceUntilIdle()

            // Then
            assertThat(viewModel.sideEffect.first())
                .isInstanceOf(SettingsContract.Effect.Navigation.Connections::class.java)
        }

    @Test
    fun `WHEN OnDismissDialog event is received THEN uiState should be updated`() =
        runTest {
            // Given
            every { observeActiveUserUseCase() } returns flowOf(
                Result.success(ConnectionEntity.default)
            )
            every { fetchAppPreferencesUseCase() } returns flowOf(AppPreferencesEntity.default)

            viewModel = SettingsViewModel(
                fetchAppPreferencesUseCase,
                observeActiveUserUseCase,
                updateRefreshIntervalUseCase,
                updateDynamicThemeUseCase,
                updateSelectedThemeUseCase,
                updateStatusChangeOnAllConnectionsUseCase
            )
            viewModel.onEvent(SettingsContract.Event.OnRefreshIntervalClicked)
            advanceUntilIdle()
            val loadedResult = viewModel.uiResult.first { it is UIResult.Loaded } as UIResult.Loaded
            assertThat(loadedResult.data.dialogType).isEqualTo(
                SettingsDialogType.RefreshInterval(
                    10000L
                )
            )

            // When
            viewModel.onEvent(SettingsContract.Event.OnDismissDialog)
            advanceUntilIdle()

            // Then
            val lastLoadedResult =
                viewModel.uiResult.first { it is UIResult.Loaded } as UIResult.Loaded
            assertThat(lastLoadedResult.data.dialogType).isEqualTo(SettingsDialogType.None)
        }

    @Test
    fun `WHEN OnThemeChanged event is received THEN updateSelectedThemeUseCase should be called`() =
        runTest {
            // Given
            viewModel = SettingsViewModel(
                fetchAppPreferencesUseCase,
                observeActiveUserUseCase,
                updateRefreshIntervalUseCase,
                updateDynamicThemeUseCase,
                updateSelectedThemeUseCase,
                updateStatusChangeOnAllConnectionsUseCase
            )
            val theme = AppThemePres.Light

            // When
            viewModel.onEvent(SettingsContract.Event.OnThemeChanged(theme))
            advanceUntilIdle()

            // Then
            coVerify { updateSelectedThemeUseCase(theme.mapToEntity()) }
        }

    @Test
    fun `WHEN OnDynamicThemeColorsChanged event is received THEN updateDynamicThemeUseCase should be called`() =
        runTest {
            // Given
            viewModel = SettingsViewModel(
                fetchAppPreferencesUseCase,
                observeActiveUserUseCase,
                updateRefreshIntervalUseCase,
                updateDynamicThemeUseCase,
                updateSelectedThemeUseCase,
                updateStatusChangeOnAllConnectionsUseCase
            )
            val useDynamicTheme = true

            // When
            viewModel.onEvent(SettingsContract.Event.OnDynamicThemeColorsChanged(useDynamicTheme))
            advanceUntilIdle()

            // Then
            coVerify { updateDynamicThemeUseCase(useDynamicTheme) }
        }

    @Test
    fun `WHEN OnRefreshIntervalChanged event is received THEN updateRefreshIntervalUseCase should be called`() =
        runTest {
            // Given
            viewModel = SettingsViewModel(
                fetchAppPreferencesUseCase,
                observeActiveUserUseCase,
                updateRefreshIntervalUseCase,
                updateDynamicThemeUseCase,
                updateSelectedThemeUseCase,
                updateStatusChangeOnAllConnectionsUseCase
            )
            val refreshInterval = 5000L

            // When
            viewModel.onEvent(SettingsContract.Event.OnRefreshIntervalChanged(refreshInterval))
            advanceUntilIdle()

            // Then
            coVerify { updateRefreshIntervalUseCase(refreshInterval) }
        }

    @Test
    fun `WHEN OnChangeStatusOnAllConnectionsChanged event is received THEN updateStatusChangeOnAllConnectionsUseCase should be called`() =
        runTest {
            // Given
            viewModel = SettingsViewModel(
                fetchAppPreferencesUseCase,
                observeActiveUserUseCase,
                updateRefreshIntervalUseCase,
                updateDynamicThemeUseCase,
                updateSelectedThemeUseCase,
                updateStatusChangeOnAllConnectionsUseCase
            )
            val changeOnAll = true

            // When
            viewModel.onEvent(
                SettingsContract.Event.OnChangeStatusOnAllConnectionsChanged(
                    changeOnAll
                )
            )
            advanceUntilIdle()

            // Then
            coVerify { updateStatusChangeOnAllConnectionsUseCase(changeOnAll) }
        }
}
