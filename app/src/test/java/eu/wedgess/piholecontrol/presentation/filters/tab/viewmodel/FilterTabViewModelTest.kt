package eu.wedgess.piholecontrol.presentation.filters.tab.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.MainDispatcherRule
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.model.FilterRulesResultEntity
import eu.wedgess.piholecontrol.domain.usecases.filters.FetchFilterRulesUseCase
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.filters.extensions.toInfo
import eu.wedgess.piholecontrol.presentation.filters.model.FilterScreenTabType
import eu.wedgess.piholecontrol.presentation.filters.tab.FilterTabContract
import eu.wedgess.piholecontrol.utils.UiText
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
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
                FilterScreenTabType.ALLOW
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
            val filterRulesResultEntity = FilterRulesResultEntity(
                rules = Result.success(filterRules),
                regexRules = Result.success(emptyList())
            )
            coEvery { fetchFilterRulesUseCase(FilterRuleTypeEntity.ALLOW) } returns flowOf(
                Result.success(
                    filterRulesResultEntity
                )
            )

            // When
            viewModel = FilterTabViewModel(
                fetchFilterRulesUseCase,
                FilterScreenTabType.ALLOW
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
                FilterScreenTabType.ALLOW
            )

            // Then
            viewModel.uiResult.test {
                val result = awaitItem()
                assertThat(result).isInstanceOf(UIResult.Error::class.java)
                assertThat((result as UIResult.Error).errorType)
                    .isInstanceOf(ResultType.Error.WithTitleAndSubTitle::class.java)
                assertThat((result.errorType as ResultType.Error.WithTitleAndSubTitle).title).isEqualTo(
                    UiText.StringResource(R.string.filter_rules_fetch_error)
                )
                assertThat((result.errorType as ResultType.Error.WithTitleAndSubTitle).subTitle)
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
            // Given
            val filterRulesResultEntity = FilterRulesResultEntity(
                rules = Result.success(
                    listOf(
                        expectedResult,
                        FilterRuleEntity(
                            id = 1,
                            enabled = true,
                            comment = null,
                            dateModified = "12-01-2023",
                            dateAdded = "11-01-2023",
                            domain = "dummy.com",
                            groups = emptyList(),
                            type = FilterRuleTypeEntity.ALLOW
                        )
                    )
                ),
                regexRules = Result.success(emptyList())
            )
            coEvery { fetchFilterRulesUseCase(FilterRuleTypeEntity.ALLOW) } returns flowOf(
                Result.success(
                    filterRulesResultEntity
                )
            )
            viewModel = FilterTabViewModel(
                fetchFilterRulesUseCase,
                FilterScreenTabType.ALLOW
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
    fun `GIVEN rules and regexRules returns failure WHEN viewmodel is initialized THEN ShowErrorSnackbar side effect should be emitted`() =
        runTest {
            // Given
            val rulesException = Exception("Rules Test Exception")
            val regexRulesException = Exception("Regex Rules Test Exception")
            val filterRulesResultEntity = FilterRulesResultEntity(
                rules = Result.failure(rulesException),
                regexRules = Result.failure(regexRulesException)
            )
            coEvery { fetchFilterRulesUseCase(FilterRuleTypeEntity.ALLOW) } returns flowOf(
                Result.success(
                    filterRulesResultEntity
                )
            )
            viewModel = FilterTabViewModel(
                fetchFilterRulesUseCase,
                FilterScreenTabType.ALLOW
            )

            // Then
            viewModel.uiResult.test {
                val errorResult = awaitItem()
                assertThat(errorResult).isInstanceOf(UIResult.Error::class.java)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `GIVEN rules returns success and regexRules returns failure WHEN viewmodel is initialized THEN ShowErrorSnackbar side effect should be emitted`() =
        runTest {
            // Given
            val regexRulesException = Exception("Regex Rules Test Exception")
            val filterRulesResultEntity = FilterRulesResultEntity(
                rules = Result.success(listOf(mockk(relaxed = true))),
                regexRules = Result.failure(regexRulesException)
            )
            coEvery { fetchFilterRulesUseCase(FilterRuleTypeEntity.ALLOW) } returns flowOf(
                Result.success(
                    filterRulesResultEntity
                )
            )
            viewModel = FilterTabViewModel(
                fetchFilterRulesUseCase,
                FilterScreenTabType.ALLOW
            )

            // Then
            viewModel.uiResult.test {
                val loadedResult = awaitItem()
                assertThat(loadedResult).isInstanceOf(UIResult.Loaded::class.java)
                cancelAndConsumeRemainingEvents()
            }
            viewModel.sideEffect.test {
                val sideEffect = awaitItem()
                assertThat(sideEffect)
                    .isInstanceOf(FilterTabContract.Effect.ShowErrorSnackbar::class.java)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `GIVEN rules returns success with empty list and regexRules returns failure WHEN viewmodel is initialized THEN ShowErrorSnackbar side effect should be emitted`() =
        runTest {
            // Given
            val regexRulesException = Exception("Regex Rules Test Exception")
            val filterRulesResultEntity = FilterRulesResultEntity(
                rules = Result.success(emptyList()),
                regexRules = Result.failure(regexRulesException)
            )
            coEvery { fetchFilterRulesUseCase(FilterRuleTypeEntity.ALLOW) } returns flowOf(
                Result.success(
                    filterRulesResultEntity
                )
            )
            viewModel = FilterTabViewModel(
                fetchFilterRulesUseCase,
                FilterScreenTabType.ALLOW
            )

            // Then
            viewModel.uiResult.test {
                val emptyResult = awaitItem()
                assertThat(emptyResult).isInstanceOf(UIResult.Empty::class.java)
                cancelAndConsumeRemainingEvents()
            }
            viewModel.sideEffect.test {
                val sideEffect = awaitItem()
                assertThat(sideEffect)
                    .isInstanceOf(FilterTabContract.Effect.ShowErrorSnackbar::class.java)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `GIVEN rules returns failure and regexRules returns success WHEN viewmodel is initialized THEN ShowErrorSnackbar side effect should be emitted`() =
        runTest {
            // Given
            val rulesException = Exception("Rules Test Exception")
            val filterRulesResultEntity = FilterRulesResultEntity(
                rules = Result.failure(rulesException),
                regexRules = Result.success(listOf(mockk(relaxed = true)))
            )
            coEvery { fetchFilterRulesUseCase(FilterRuleTypeEntity.ALLOW) } returns flowOf(
                Result.success(
                    filterRulesResultEntity
                )
            )
            viewModel = FilterTabViewModel(
                fetchFilterRulesUseCase,
                FilterScreenTabType.ALLOW
            )

            // Then
            viewModel.uiResult.test {
                val loadedResult = awaitItem()
                assertThat(loadedResult).isInstanceOf(UIResult.Loaded::class.java)
                cancelAndConsumeRemainingEvents()
            }
            viewModel.sideEffect.test {
                val sideEffect = awaitItem()
                assertThat(sideEffect).isInstanceOf(
                    FilterTabContract.Effect.ShowErrorSnackbar::class.java
                )
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `GIVEN rules and regexRules returns success WHEN viewmodel is initialized THEN ShowErrorSnackbar side effect should not be emitted`() =
        runTest {
            // Given
            val filterRulesResultEntity = FilterRulesResultEntity(
                rules = Result.success(emptyList()),
                regexRules = Result.success(emptyList())
            )
            coEvery { fetchFilterRulesUseCase(FilterRuleTypeEntity.ALLOW) } returns flowOf(
                Result.success(
                    filterRulesResultEntity
                )
            )
            viewModel = FilterTabViewModel(
                fetchFilterRulesUseCase,
                FilterScreenTabType.ALLOW
            )

            // Then
            viewModel.sideEffect.test {
                expectNoEvents()
            }
        }
}
