package eu.wedgess.piholecontrol.presentation.statistics.tabs.upstreams.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.usecases.statistics.FetchUpstreamDestinationsUseCase
import eu.wedgess.piholecontrol.presentation.common.model.LegendData
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.statistics.common.components.donutchart.model.DonutChartDataCollection
import eu.wedgess.piholecontrol.presentation.statistics.common.components.donutchart.model.UpstreamDestinationsChartData
import eu.wedgess.piholecontrol.presentation.statistics.tabs.upstreams.UpstreamDestinationsContract
import eu.wedgess.piholecontrol.utils.UiText
import io.mockk.MockKAnnotations
import io.mockk.coEvery
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
class UpstreamDestinationsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    @RelaxedMockK
    private lateinit var fetchForwardDestinationsUseCase: FetchUpstreamDestinationsUseCase

    private lateinit var viewModel: UpstreamDestinationsViewModel

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
            coEvery { fetchForwardDestinationsUseCase() } returns flowOf(
                Result.success(emptyList())
            )

            // When
            viewModel = UpstreamDestinationsViewModel(fetchForwardDestinationsUseCase)

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
    fun `GIVEN usecase returns success WHEN viewmodel is initialized THEN uiResult should emit Loaded state`() =
        runTest {
            // Given
            val serversChartDataList = listOf(
                UpstreamDestinationsChartData(
                    title = "Server 1",
                    percentage = 50f
                ),
                UpstreamDestinationsChartData(
                    title = "Server 2",
                    percentage = 30.233f
                )
            )
            coEvery { fetchForwardDestinationsUseCase() } returns flowOf(
                Result.success(
                    serversChartDataList
                )
            )

            // When
            viewModel = UpstreamDestinationsViewModel(fetchForwardDestinationsUseCase)

            // Then
            viewModel.uiResult.test {
                skipItems(1)
                val result = awaitItem()
                assertThat(result).isInstanceOf(UIResult.Loaded::class.java)
                assertThat((result as UIResult.Loaded).data)
                    .isInstanceOf(UpstreamDestinationsContract.UiState::class.java)
                assertThat(result.data.donutChartDataCollection)
                    .isInstanceOf(DonutChartDataCollection::class.java)
                assertThat(result.data.legendData).hasSize(serversChartDataList.size)
                assertThat(result.data.legendData[0])
                    .isEqualTo(LegendData("Server 1", "50%", false))
                assertThat(result.data.legendData[1])
                    .isEqualTo(LegendData("Server 2", "30.2%", false))
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `GIVEN usecase returns failure WHEN viewmodel is initialized THEN uiResult should emit Error`() =
        runTest {
            // Given
            val exception = Exception("Test Exception")
            coEvery { fetchForwardDestinationsUseCase() } returns flowOf(Result.failure(exception))

            // When
            viewModel = UpstreamDestinationsViewModel(fetchForwardDestinationsUseCase)

            // Then
            viewModel.uiResult.test {
                skipItems(1)
                val result = awaitItem()
                assertThat(result).isInstanceOf(UIResult.Error::class.java)
                assertThat((result as UIResult.Error).errorType)
                    .isInstanceOf(ResultType.Error.WithTitleAndSubTitleAndRetry::class.java)
                assertThat((result.errorType as ResultType.Error.WithTitleAndSubTitleAndRetry).title)
                    .isEqualTo(UiText.StringResource(R.string.upstream_destinations_error))
                assertThat((result.errorType as ResultType.Error.WithTitleAndSubTitleAndRetry).subTitle)
                    .isEqualTo(UiText.DynamicString(exception.message ?: "Unknown error"))
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnLegendItemSelected event is received THEN legendData should update selected index`() =
        runTest {
            // Given
            val serversChartDataList = listOf(
                mockk<UpstreamDestinationsChartData>(relaxed = true) {
                    coEvery { title } returns "Server 1"
                    coEvery { percentage } returns 50f
                },
                mockk<UpstreamDestinationsChartData>(relaxed = true) {
                    coEvery { title } returns "Server 2"
                    coEvery { percentage } returns 30f
                }
            )
            coEvery { fetchForwardDestinationsUseCase() } returns flowOf(
                Result.success(
                    serversChartDataList
                )
            )
            viewModel = UpstreamDestinationsViewModel(fetchForwardDestinationsUseCase)
            viewModel.uiResult.first { it is UIResult.Loaded }

            // When
            viewModel.onEvent(UpstreamDestinationsContract.Event.OnLegendItemSelected(1))
            advanceUntilIdle()

            // Then
            viewModel.uiResult.test {
                val result = awaitItem()
                assertThat((result as UIResult.Loaded).data.legendData[0].isSelected).isFalse()
                assertThat(result.data.legendData[1].isSelected).isTrue()
                cancelAndConsumeRemainingEvents()
            }
        }
}
