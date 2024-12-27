package eu.wedgess.piholecontrol.presentation.statistics.tabs.topclients.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.TopClientEntity
import eu.wedgess.piholecontrol.domain.usecases.statistics.FetchTopClientsUseCase
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.statistics.tabs.topclients.model.TopClientsInfo
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
class TopClientsStatsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    @RelaxedMockK
    private lateinit var fetchTopClientsUseCase: FetchTopClientsUseCase

    private lateinit var viewModel: TopClientsStatsViewModel

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
            coEvery { fetchTopClientsUseCase() } returns flowOf(Result.success(emptyList()))

            // When
            viewModel = TopClientsStatsViewModel(fetchTopClientsUseCase)

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
            val topClients = listOf(
                mockk<TopClientEntity>(relaxed = true) {
                    coEvery { client } returns "Client 1"
                    coEvery { hits } returns 100
                },
                mockk<TopClientEntity>(relaxed = true) {
                    coEvery { client } returns "Client 2"
                    coEvery { hits } returns 50
                }
            )
            coEvery { fetchTopClientsUseCase() } returns flowOf(Result.success(topClients))

            // When
            viewModel = TopClientsStatsViewModel(fetchTopClientsUseCase)

            // Then
            viewModel.uiResult.test {
                awaitItem() // Loading
                val result = awaitItem()
                assertThat(result).isInstanceOf(UIResult.Loaded::class.java)
                val uiState = result as UIResult.Loaded
                assertThat(uiState.data).isInstanceOf(TopClientsInfo::class.java)
                assertThat(uiState.data.topClients).isEqualTo(topClients)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `GIVEN usecase returns failure WHEN viewmodel is initialized THEN uiResult should emit Error`() =
        runTest {
            // Given
            val exception = Exception("Test Exception")
            coEvery { fetchTopClientsUseCase() } returns flowOf(Result.failure(exception))

            // When
            viewModel = TopClientsStatsViewModel(fetchTopClientsUseCase)

            // Then
            viewModel.uiResult.test {
                awaitItem() // Loading
                val result = awaitItem()
                assertThat(result).isInstanceOf(UIResult.Error::class.java)
                assertThat((result as UIResult.Error).errorType)
                    .isInstanceOf(ResultType.Error.WithTitleAndSubTitle::class.java)
                assertThat((result.errorType as ResultType.Error.WithTitleAndSubTitle).title)
                    .isEqualTo(UiText.StringResource(R.string.top_clients_error))
                assertThat((result.errorType as ResultType.Error.WithTitleAndSubTitle).subTitle)
                    .isEqualTo(UiText.DynamicString(exception.message ?: "Unknown error"))
                cancelAndConsumeRemainingEvents()
            }
        }
}
