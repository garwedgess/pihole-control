package eu.wedgess.piholecontrol.presentation.dashboard.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.DashboardInfoEntity
import eu.wedgess.piholecontrol.domain.usecases.dashboard.FetchDashboardInfoUseCase
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.dashboard.DashboardContract
import eu.wedgess.piholecontrol.utils.UiText
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DashboardViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    @RelaxedMockK
    private lateinit var fetchDashboardInfoUseCase: FetchDashboardInfoUseCase

    private lateinit var viewModel: DashboardViewModel

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
    fun `WHEN viewmodel is initialized THEN uiResult should emit Loading state initially`() =
        runTest {
            // Given
            coEvery { fetchDashboardInfoUseCase() } returns flowOf(
                Result.success(mockk(relaxed = true))
            )

            // When
            viewModel = DashboardViewModel(fetchDashboardInfoUseCase)

            // Then
            viewModel.uiResult.test {
                val result = awaitItem()
                assertThat(result).isInstanceOf(UIResult.Loading::class.java)
                assertThat((result as UIResult.Loading).loadingType)
                    .isEqualTo(ResultType.Loading.WithTitle())
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `GIVEN usecase returns successful WHEN viewmodel is initialized THEN uiResult should emit Loaded state`() =
        runTest {
            // Given
            coEvery { fetchDashboardInfoUseCase() } returns flowOf(
                Result.success(
                    DashboardInfoEntity(
                        summaryResult = Result.success(mockk(relaxed = true)),
                        queriesOverTimeResult = Result.success(mockk(relaxed = true)),
                        clientQueriesOverTimeResult = Result.success(listOf(mockk(relaxed = true)))
                    )
                )
            )

            // When
            viewModel = DashboardViewModel(fetchDashboardInfoUseCase)

            // Then
            viewModel.uiResult.test {
                val result = awaitItem()
                assertThat(result).isInstanceOf(UIResult.Loading::class.java)
                val loadedResult = awaitItem()
                assertThat(loadedResult).isInstanceOf(UIResult.Loaded::class.java)
                assertThat((loadedResult as UIResult.Loaded).data)
                    .isInstanceOf(DashboardContract.UiState::class.java)
                assertThat(loadedResult.data.summary).isNotNull()
                assertThat(loadedResult.data.overtimeLineChart).isNotNull()
                assertThat(loadedResult.data.clientQueriesOverTime).isNotNull()
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `GIVEN usecase returns failure WHEN viewmodel is iniatlized THEN uiResult emits Error `() =
        runTest {
            // Given
            val exception = Exception("Test Exception")
            coEvery { fetchDashboardInfoUseCase() } returns flowOf(Result.failure(exception))

            // When
            viewModel = DashboardViewModel(fetchDashboardInfoUseCase)

            // Then
            viewModel.uiResult.test {
                val result = awaitItem()
                assertThat(result).isInstanceOf(UIResult.Loading::class.java)
                val errorResult = awaitItem()
                assertThat(errorResult).isInstanceOf(UIResult.Error::class.java)
                assertThat((errorResult as UIResult.Error).errorType)
                    .isInstanceOf(ResultType.Error.WithTitleAndSubTitle::class.java)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `GIVEN usecase return success but properties are failure THEN uiResult emits Error`() =
        runTest {
            // Given
            val summaryException = Exception("Summary Error")
            val queriesOverTimeException = Exception("Queries Over Time Error")
            val clientQueriesOverTimeException = Exception("Client Queries Over Time Error")
            coEvery { fetchDashboardInfoUseCase() } returns flowOf(
                Result.success(
                    DashboardInfoEntity(
                        summaryResult = Result.failure(summaryException),
                        queriesOverTimeResult = Result.failure(queriesOverTimeException),
                        clientQueriesOverTimeResult = Result.failure(clientQueriesOverTimeException)
                    )
                )
            )

            // When
            viewModel = DashboardViewModel(fetchDashboardInfoUseCase)

            // Then
            viewModel.uiResult.test {
                val result = awaitItem()
                assertThat(result).isInstanceOf(UIResult.Loading::class.java)
                val errorResult = awaitItem()
                assertThat(errorResult).isInstanceOf(UIResult.Error::class.java)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN at least one result is successful THEN result is Loaded AND emitSideEffect should emit ShowErrorSnackbar with 2 messages`() =
        runTest {
            // Given
            val queriesOverTimeException = Exception("Queries Over Time Error")
            val clientQueriesOverTimeException = Exception("Client Queries Over Time Error")
            coEvery { fetchDashboardInfoUseCase() } returns flowOf(
                Result.success(
                    DashboardInfoEntity(
                        summaryResult = Result.success(mockk(relaxed = true)),
                        queriesOverTimeResult = Result.failure(queriesOverTimeException),
                        clientQueriesOverTimeResult = Result.failure(clientQueriesOverTimeException)
                    )
                )
            )
            val expectedFailures = listOf(
                UiText.StringResourceWithArgs(
                    R.string.dashboard_queries_over_time_error,
                    queriesOverTimeException.message ?: ""
                ),
                UiText.StringResourceWithArgs(
                    R.string.dashboard_client_queries_over_time_error,
                    clientQueriesOverTimeException.message ?: ""
                )
            )

            // When
            viewModel = DashboardViewModel(fetchDashboardInfoUseCase)

            // Then
            viewModel.uiResult.test {
                val result = awaitItem()
                assertThat(result).isInstanceOf(UIResult.Loading::class.java)
                val loadedResult = awaitItem()
                assertThat(loadedResult).isInstanceOf(UIResult.Loaded::class.java)
                cancelAndConsumeRemainingEvents()
            }
            viewModel.sideEffect.test {
                val sideEffect = awaitItem()
                assertThat(sideEffect)
                    .isInstanceOf(DashboardContract.Effect.ShowErrorSnackbar::class.java)
                assertThat(
                    (sideEffect as DashboardContract.Effect.ShowErrorSnackbar).errorMessages.size
                ).isEqualTo(expectedFailures.size)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN one result is failure THEN result is Loaded AND emitSideEffect should emit ShowErrorSnackbar with 1 message`() =
        runTest {
            // Given
            val summaryException = Exception("Summary Error")
            coEvery { fetchDashboardInfoUseCase() } returns flowOf(
                Result.success(
                    DashboardInfoEntity(
                        summaryResult = Result.failure(summaryException),
                        queriesOverTimeResult = Result.success(mockk(relaxed = true)),
                        clientQueriesOverTimeResult = Result.success(listOf(mockk(relaxed = true)))
                    )
                )
            )
            val expectedFailures = listOf(
                UiText.StringResourceWithArgs(
                    R.string.dashboard_summary_error,
                    summaryException.message ?: ""
                )
            )

            // When
            viewModel = DashboardViewModel(fetchDashboardInfoUseCase)

            // Then
            viewModel.uiResult.test {
                val result = awaitItem()
                assertThat(result).isInstanceOf(UIResult.Loading::class.java)
                val loadedResult = awaitItem()
                assertThat(loadedResult).isInstanceOf(UIResult.Loaded::class.java)
                cancelAndConsumeRemainingEvents()
            }
            viewModel.sideEffect.test {
                val sideEffect = awaitItem()
                assertThat(sideEffect)
                    .isInstanceOf(DashboardContract.Effect.ShowErrorSnackbar::class.java)
                assertThat(
                    (sideEffect as DashboardContract.Effect.ShowErrorSnackbar).errorMessages.size
                ).isEqualTo(expectedFailures.size)
                cancelAndConsumeRemainingEvents()
            }
        }
}
