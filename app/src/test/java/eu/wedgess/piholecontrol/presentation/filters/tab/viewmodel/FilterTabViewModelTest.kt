package eu.wedgess.piholecontrol.presentation.filters.tab.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.MainDispatcherRule
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.usecases.filters.FetchFilterRulesUseCase
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.filters.extensions.toInfo
import eu.wedgess.piholecontrol.presentation.filters.model.FilterByOption
import eu.wedgess.piholecontrol.presentation.filters.tab.FilterTabContract
import eu.wedgess.piholecontrol.presentation.navigation.tabs.FilterTab
import eu.wedgess.piholecontrol.utils.UiText
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FilterTabViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @RelaxedMockK
    private lateinit var fetchFilterRulesUseCase: FetchFilterRulesUseCase

    private lateinit var viewModel: FilterTabViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `WHEN viewmodel is initialized THEN uiResult should emit Loading state initially`() =
        runTest {
            // When
            viewModel = FilterTabViewModel(
                fetchFilterRulesUseCase,
                FilterTab.AllowList
            )

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
            val filterRules = listOf(
                FilterRuleEntity(
                    id = 1,
                    enabled = true,
                    comment = null,
                    dateModified = "12-01-2023",
                    dateAdded = "11-01-2023",
                    domain = "test.com",
                    groups = emptyList(),
                    type = FilterRuleTypeEntity.ALLOW
                )
            )
            coEvery { fetchFilterRulesUseCase(FilterRuleTypeEntity.ALLOW) } returns flowOf(
                Result.success(
                    filterRules
                )
            )

            // When
            viewModel = FilterTabViewModel(
                fetchFilterRulesUseCase,
                FilterTab.AllowList
            )

            // Then
            viewModel.uiResult.test {
                val result = awaitItem()
                assertThat(result).isInstanceOf(UIResult.Loaded::class.java)
                assertThat((result as UIResult.Loaded).data.filterRules)
                    .isEqualTo(filterRules.map { it.toInfo() })
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `GIVEN usecase returns failure WHEN viewmodel is initialized THEN uiResult should emit Error state`() =
        runTest {
            // Given
            val exception = Exception("Test Exception")
            coEvery { fetchFilterRulesUseCase(FilterRuleTypeEntity.ALLOW) } returns flowOf(
                Result.failure(
                    exception
                )
            )

            // When
            viewModel = FilterTabViewModel(
                fetchFilterRulesUseCase,
                FilterTab.AllowList
            )

            // Then
            viewModel.uiResult.test {
                val result = awaitItem()
                assertThat(result).isInstanceOf(UIResult.Error::class.java)
                assertThat((result as UIResult.Error).errorType)
                    .isInstanceOf(ResultType.Error.WithTitleAndSubTitleAndRetry::class.java)
                assertThat((result.errorType as ResultType.Error.WithTitleAndSubTitleAndRetry).title).isEqualTo(
                    UiText.StringResource(R.string.filter_rules_fetch_error)
                )
                assertThat((result.errorType as ResultType.Error.WithTitleAndSubTitleAndRetry).subTitle)
                    .isEqualTo(
                        UiText.DynamicString(exception.message ?: "Unknown error")
                    )
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnSearchQueryChanged event is received THEN searchQuery should be updated`() =
        runTest {
            val expectedResult = FilterRuleEntity(
                id = 1,
                enabled = true,
                comment = null,
                dateModified = "12-01-2023",
                dateAdded = "11-01-2023",
                domain = "test.com",
                groups = emptyList(),
                type = FilterRuleTypeEntity.ALLOW
            )
            coEvery { fetchFilterRulesUseCase(FilterRuleTypeEntity.ALLOW) } returns flowOf(
                Result.success(
                    listOf(expectedResult)
                )
            )
            viewModel = FilterTabViewModel(
                fetchFilterRulesUseCase,
                FilterTab.AllowList
            )
            val query = "test"

            // When
            viewModel.onEvent(FilterTabContract.Event.OnSearchQueryChanged(query))

            // Then
            viewModel.uiResult.test {
                val result = awaitItem()
                assertThat(result).isInstanceOf(UIResult.Loaded::class.java)
                assertThat((result as UIResult.Loaded).data.filterRules.size).isEqualTo(1)
                assertThat(result.data.filterRules.first()).isEqualTo(
                    expectedResult.toInfo()
                )
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `GIVEN rules and regexRules returns failure WHEN viewmodel is initialized THEN UiResult is Error`() =
        runTest {
            // Given
            val rulesException = Exception("Rules Test Exception")
            coEvery { fetchFilterRulesUseCase(FilterRuleTypeEntity.ALLOW) } returns flowOf(
                Result.failure(rulesException)
            )
            viewModel = FilterTabViewModel(
                fetchFilterRulesUseCase,
                FilterTab.AllowList
            )

            // Then
            viewModel.uiResult.test {
                val errorResult = awaitItem()
                assertThat(errorResult).isInstanceOf(UIResult.Error::class.java)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnFilterByOptionsChanged event is received THEN filtered rules should be updated`() =
        runTest {
            // Given
            val allowRule = FilterRuleEntity(
                id = 1,
                enabled = true,
                comment = null,
                dateModified = "12-01-2023",
                dateAdded = "11-01-2023",
                domain = "test.com",
                groups = emptyList(),
                type = FilterRuleTypeEntity.ALLOW
            )
            val regexRule = allowRule.copy(type = FilterRuleTypeEntity.REGEX_ALLOW)

            coEvery { fetchFilterRulesUseCase(FilterRuleTypeEntity.ALLOW) } returns flowOf(
                Result.success(listOf(allowRule, regexRule))
            )

            viewModel = FilterTabViewModel(
                fetchFilterRulesUseCase,
                FilterTab.AllowList
            )

            // When
            viewModel.onEvent(
                FilterTabContract.Event.OnFilterByOptionsChanged(
                    listOf(FilterByOption.ALLOW_EXACT)
                )
            )

            // Then
            viewModel.uiResult.test {
                val result = awaitItem()
                assertThat(result).isInstanceOf(UIResult.Loaded::class.java)
                assertThat((result as UIResult.Loaded).data.filterRules)
                    .containsExactly(allowRule.toInfo())
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `GIVEN empty filter results WHEN rules are filtered THEN Empty state should be emitted`() =
        runTest {
            // Given
            val rules = listOf(
                FilterRuleEntity(
                    id = 1,
                    enabled = true,
                    comment = null,
                    dateModified = "12-01-2023",
                    dateAdded = "11-01-2023",
                    domain = "test.com",
                    groups = emptyList(),
                    type = FilterRuleTypeEntity.ALLOW
                )
            )

            coEvery { fetchFilterRulesUseCase(FilterRuleTypeEntity.ALLOW) } returns flowOf(
                Result.success(rules)
            )

            viewModel = FilterTabViewModel(
                fetchFilterRulesUseCase,
                FilterTab.AllowList
            )

            // When - search for non-existent domain
            viewModel.onEvent(FilterTabContract.Event.OnSearchQueryChanged("nonexistent"))

            // Then
            viewModel.uiResult.test {
                val result = awaitItem()
                assertThat(result).isInstanceOf(UIResult.Empty::class.java)
                assertThat((result as UIResult.Empty).emptyType)
                    .isInstanceOf(ResultType.Empty.WithTitle::class.java)
                assertThat((result.emptyType as ResultType.Empty.WithTitle).title)
                    .isEqualTo(UiText.DynamicString("No rules found"))
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnRefresh event is received THEN rules should be refreshed`() = runTest {
        // Given
        viewModel = FilterTabViewModel(
            fetchFilterRulesUseCase,
            FilterTab.AllowList
        )

        // When
        viewModel.onEvent(FilterTabContract.Event.OnRefresh)

        // Then
        coVerify { fetchFilterRulesUseCase.refreshRules() }
    }
}
