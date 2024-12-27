package eu.wedgess.piholecontrol.presentation.statistics.tabs.topdomains.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.TopDomainEntity
import eu.wedgess.piholecontrol.domain.model.TopQueriesEntity
import eu.wedgess.piholecontrol.domain.usecases.statistics.FetchTopQueriesUseCase
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.statistics.tabs.topdomains.TopDomainsStatsContract
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
class TopDomainsStatsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    @RelaxedMockK
    private lateinit var fetchTopQueriesUseCase: FetchTopQueriesUseCase

    private lateinit var viewModel: TopDomainsStatsViewModel

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
            coEvery { fetchTopQueriesUseCase() } returns flowOf(Result.success(mockk(relaxed = true)))

            // When
            viewModel = TopDomainsStatsViewModel(fetchTopQueriesUseCase)

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
            val allowedQueries = listOf(
                mockk<TopDomainEntity>(relaxed = true) {
                    coEvery { domain } returns "domain1.com"
                    coEvery { hits } returns 100
                },
                mockk<TopDomainEntity>(relaxed = true) {
                    coEvery { domain } returns "domain2.com"
                    coEvery { hits } returns 50
                }
            )
            val blockedQueries = listOf(
                mockk<TopDomainEntity>(relaxed = true) {
                    coEvery { domain } returns "blocked1.com"
                    coEvery { hits } returns 20
                },
                mockk<TopDomainEntity>(relaxed = true) {
                    coEvery { domain } returns "blocked2.com"
                    coEvery { hits } returns 10
                }
            )
            val topQueries = mockk<TopQueriesEntity>(relaxed = true) {
                coEvery { allowed } returns allowedQueries
                coEvery { blocked } returns blockedQueries
            }
            coEvery { fetchTopQueriesUseCase() } returns flowOf(Result.success(topQueries))

            // When
            viewModel = TopDomainsStatsViewModel(fetchTopQueriesUseCase)

            // Then
            viewModel.uiResult.test {
                skipItems(1) // Loading
                val result = awaitItem()
                assertThat(result).isInstanceOf(UIResult.Loaded::class.java)
                val uiState = result as UIResult.Loaded
                assertThat(uiState.data).isInstanceOf(TopDomainsStatsContract.UiState::class.java)
                assertThat(uiState.data.topPermitted.topDomains).isEqualTo(
                    allowedQueries
                )
                assertThat(uiState.data.topBlocked.topDomains).isEqualTo(
                    blockedQueries
                )
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `GIVEN usecase returns failure WHEN viewmodel is initialized THEN uiResult should emit Error`() =
        runTest {
            // Given
            val exception = Exception("Test Exception")
            coEvery { fetchTopQueriesUseCase() } returns flowOf(Result.failure(exception))

            // When
            viewModel = TopDomainsStatsViewModel(fetchTopQueriesUseCase)

            // Then
            viewModel.uiResult.test {
                skipItems(1) // Loading
                val result = awaitItem()
                assertThat(result).isInstanceOf(UIResult.Error::class.java)
                assertThat((result as UIResult.Error).errorType)
                    .isInstanceOf(ResultType.Error.WithTitleAndSubTitle::class.java)
                assertThat((result.errorType as ResultType.Error.WithTitleAndSubTitle).title)
                    .isEqualTo(UiText.StringResource(R.string.top_domains_error))
                assertThat((result.errorType as ResultType.Error.WithTitleAndSubTitle).subTitle)
                    .isEqualTo(
                        UiText.DynamicString(exception.message ?: "Unknown error")
                    )
                cancelAndConsumeRemainingEvents()
            }
        }
}
