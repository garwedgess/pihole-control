package eu.wedgess.piholecontrol.presentation.splash.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.MainDispatcherRule
import eu.wedgess.piholecontrol.domain.model.AppPreferencesEntity
import eu.wedgess.piholecontrol.domain.usecases.app.CheckHasConnectionsUseCase
import eu.wedgess.piholecontrol.domain.usecases.settings.FetchAppPreferencesUseCase
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.settings.model.AppThemePres
import eu.wedgess.piholecontrol.presentation.splash.model.SplashInfo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class SplashViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(UnconfinedTestDispatcher())

    @RelaxedMockK
    private lateinit var fetchAppPreferencesUseCase: FetchAppPreferencesUseCase

    @RelaxedMockK
    private lateinit var checkHasConnectionsUseCase: CheckHasConnectionsUseCase

    private lateinit var viewModel: SplashViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `WHEN viewmodel is initialized THEN splashInfo should emit Loading state initially`() =
        runTest {
            // When
            viewModel = SplashViewModel(fetchAppPreferencesUseCase, checkHasConnectionsUseCase)

            // Then
            viewModel.splashInfo.test {
                val result = awaitItem()
                assertThat(result).isInstanceOf(UIResult.Loading::class.java)
                assertThat((result as UIResult.Loading).loadingType)
                    .isEqualTo(ResultType.Loading.WithTitle())
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN viewmodel is initialized THEN splashInfo should emit Loaded state with correct data`() =
        runTest {
            // Given
            val preferences = AppPreferencesEntity.default
            val hasConnections = true
            every { fetchAppPreferencesUseCase() } returns flowOf(preferences)
            coEvery { checkHasConnectionsUseCase() } returns Result.success(hasConnections)

            // When
            viewModel = SplashViewModel(fetchAppPreferencesUseCase, checkHasConnectionsUseCase)
            val splashInfos = mutableListOf<UIResult<SplashInfo>>()
            backgroundScope.launch {
                viewModel.splashInfo.toList(splashInfos)
            }
            advanceUntilIdle()

            viewModel.splashInfo.test {
                val result = awaitItem()
                assertThat(result).isInstanceOf(UIResult.Loaded::class.java)
                assertThat((result as UIResult.Loaded).data.theme).isEqualTo(AppThemePres.Dark)
                assertThat(result.data.useDynamicColors).isEqualTo(preferences.useDynamicColors)
                assertThat(result.data.hasConnections).isEqualTo(hasConnections)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `GIVEN checkHasConnectionsUseCase returns failure WHEN viewmodel is initialized THEN splashInfo should emit Loaded state with hasConnections false`() =
        runTest {
            // Given
            val preferences = AppPreferencesEntity.default
            every { fetchAppPreferencesUseCase() } returns flowOf(preferences)
            coEvery { checkHasConnectionsUseCase() } returns Result.failure(
                Exception("Test Exception")
            )

            // When
            viewModel = SplashViewModel(fetchAppPreferencesUseCase, checkHasConnectionsUseCase)
            val splashInfos = mutableListOf<UIResult<SplashInfo>>()
            backgroundScope.launch {
                viewModel.splashInfo.toList(splashInfos)
            }
            advanceUntilIdle()

            // Then
            viewModel.splashInfo.test {
                val loadedResult = awaitItem()
                assertThat(loadedResult).isInstanceOf(UIResult.Loaded::class.java)
                assertThat((loadedResult as UIResult.Loaded).data.hasConnections).isFalse()
                cancelAndConsumeRemainingEvents()
            }
        }
}
