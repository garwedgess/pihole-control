package eu.wedgess.piholecontrol.presentation.filters.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.MainDispatcherRule
import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.model.ModifyFilterRuleResponseEntity
import eu.wedgess.piholecontrol.domain.usecases.filters.AddFilterRuleUseCase
import eu.wedgess.piholecontrol.domain.usecases.filters.RemoveFilterRuleUseCase
import eu.wedgess.piholecontrol.presentation.filters.FiltersContract
import eu.wedgess.piholecontrol.presentation.filters.extensions.toInfo
import eu.wedgess.piholecontrol.presentation.filters.model.FilterByOption
import eu.wedgess.piholecontrol.presentation.filters.model.FilterDialogType
import eu.wedgess.piholecontrol.presentation.filters.model.ModifyFilterRule
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
class FiltersViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @RelaxedMockK
    private lateinit var addFilterRuleUseCase: AddFilterRuleUseCase

    @RelaxedMockK
    private lateinit var removeFilterRuleUseCase: RemoveFilterRuleUseCase

    private lateinit var viewModel: FiltersViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `WHEN viewmodel is initialized THEN uiState should emit initial state`() = runTest {
        // When
        viewModel = FiltersViewModel(addFilterRuleUseCase, removeFilterRuleUseCase)

        // Then
        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(FiltersContract.UiState.initial())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `WHEN OnClearSearchQuery event is received with empty query THEN uiState should update showSearchView to false`() =
        runTest {
            // Given
            viewModel = FiltersViewModel(addFilterRuleUseCase, removeFilterRuleUseCase)

            // When
            viewModel.onEvent(FiltersContract.Event.OnClearSearchQuery(""))

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.showSearchView).isFalse()
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnClearSearchQuery event is received with non-empty query THEN uiState should update searchQuery to empty`() =
        runTest {
            // Given
            viewModel = FiltersViewModel(addFilterRuleUseCase, removeFilterRuleUseCase)

            // When
            viewModel.onEvent(FiltersContract.Event.OnClearSearchQuery("test"))

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.searchQuery).isEmpty()
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnSearchClick event is received THEN uiState should update showSearchView to false`() =
        runTest {
            // Given
            viewModel = FiltersViewModel(addFilterRuleUseCase, removeFilterRuleUseCase)
            viewModel.onEvent(FiltersContract.Event.OnShowSearchView)

            // When
            viewModel.onEvent(FiltersContract.Event.OnSearchClick)

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.showSearchView).isFalse()
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnSearchExpandedChanged event is received THEN uiState should update showSearchView`() =
        runTest {
            // Given
            viewModel = FiltersViewModel(addFilterRuleUseCase, removeFilterRuleUseCase)

            // When
            viewModel.onEvent(FiltersContract.Event.OnSearchExpandedChanged(true))

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.showSearchView).isTrue()
                cancelAndConsumeRemainingEvents()
            }

            // When
            viewModel.onEvent(FiltersContract.Event.OnSearchExpandedChanged(false))

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.showSearchView).isFalse()
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnSearchQueryChanged event is received THEN uiState should update searchQuery`() =
        runTest {
            // Given
            viewModel = FiltersViewModel(addFilterRuleUseCase, removeFilterRuleUseCase)
            val query = "test query"

            // When
            viewModel.onEvent(FiltersContract.Event.OnSearchQueryChanged(query))

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.searchQuery).isEqualTo(query)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnShowSearchView event is received THEN uiState should update showSearchView to true`() =
        runTest {
            // Given
            viewModel = FiltersViewModel(addFilterRuleUseCase, removeFilterRuleUseCase)

            // When
            viewModel.onEvent(FiltersContract.Event.OnShowSearchView)

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.showSearchView).isTrue()
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnAddFilterRule event is received THEN addFilterRuleUseCase should be called`() =
        runTest {
            // Given
            val rule = FilterRuleEntity(
                id = 1,
                enabled = true,
                comment = null,
                dateModified = "12-01-2023",
                dateAdded = "11-01-2023",
                domain = "test.com",
                groups = emptyList(),
                type = FilterRuleTypeEntity.ALLOW
            )
            coEvery { addFilterRuleUseCase(rule.domain, rule.type) } returns Result.success(
                ModifyFilterRuleResponseEntity(success = true, message = null)
            )
            viewModel = FiltersViewModel(addFilterRuleUseCase, removeFilterRuleUseCase)

            // When
            viewModel.onEvent(
                FiltersContract.Event.OnAddFilterRule(
                    ModifyFilterRule.Add(
                        domain = rule.domain,
                        type = rule.type
                    )
                )
            )
            advanceUntilIdle()

            // Then
            coVerify { addFilterRuleUseCase(rule.domain, rule.type) }
        }

    @Test
    fun `GIVEN usecase returns success WHEN OnAddFilterRule event is received THEN RuleAdded side effect should be emitted`() =
        runTest {
            // Given
            val rule = FilterRuleEntity(
                id = 1,
                enabled = true,
                comment = null,
                dateModified = "12-01-2023",
                dateAdded = "11-01-2023",
                domain = "test.com",
                groups = emptyList(),
                type = FilterRuleTypeEntity.ALLOW
            )
            coEvery { addFilterRuleUseCase(rule.domain, rule.type) } returns Result.success(
                ModifyFilterRuleResponseEntity(success = true, message = null)
            )
            viewModel = FiltersViewModel(addFilterRuleUseCase, removeFilterRuleUseCase)

            // When
            viewModel.onEvent(
                FiltersContract.Event.OnAddFilterRule(
                    ModifyFilterRule.Add(
                        domain = rule.domain,
                        type = rule.type
                    )
                )
            )
            advanceUntilIdle()

            // Then
            viewModel.sideEffect.test {
                assertThat(awaitItem()).isInstanceOf(FiltersContract.Effect.Toast.RuleAdded::class.java)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `GIVEN usecase returns failure WHEN OnAddFilterRule event is received THEN RuleAddFailed side effect should be emitted`() =
        runTest {
            // Given
            val rule = FilterRuleEntity(
                id = 1,
                enabled = true,
                comment = null,
                dateModified = "12-01-2023",
                dateAdded = "11-01-2023",
                domain = "test.com",
                groups = emptyList(),
                type = FilterRuleTypeEntity.REGEX_ALLOW
            )
            coEvery { addFilterRuleUseCase(rule.domain, rule.type) } returns Result.failure(
                Exception("Test")
            )
            viewModel = FiltersViewModel(addFilterRuleUseCase, removeFilterRuleUseCase)

            // When
            viewModel.onEvent(
                FiltersContract.Event.OnAddFilterRule(
                    ModifyFilterRule.Add(
                        domain = rule.domain,
                        type = rule.type
                    )
                )
            )
            advanceUntilIdle()

            // Then
            viewModel.sideEffect.test {
                assertThat(awaitItem()).isInstanceOf(FiltersContract.Effect.Toast.RuleAddFailed::class.java)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnDeleteFilterRuleConfirmed event is received THEN removeFilterRuleUseCase should be called`() =
        runTest {
            // Given
            val rule = FilterRuleEntity(
                id = 1,
                enabled = true,
                comment = null,
                dateModified = "12-01-2023",
                dateAdded = "11-01-2023",
                domain = "test.com",
                groups = emptyList(),
                type = FilterRuleTypeEntity.REGEX_ALLOW
            )
            coEvery { removeFilterRuleUseCase(rule.domain, rule.type) } returns Result.success(
                ModifyFilterRuleResponseEntity(success = true, message = null)
            )
            viewModel = FiltersViewModel(addFilterRuleUseCase, removeFilterRuleUseCase)

            // When
            viewModel.onEvent(
                FiltersContract.Event.OnDeleteFilterRuleConfirmed(
                    ModifyFilterRule.Delete(
                        domain = rule.domain,
                        type = rule.type
                    )
                )
            )
            advanceUntilIdle()

            // Then
            coVerify { removeFilterRuleUseCase(rule.domain, rule.type) }
        }

    @Test
    fun `GIVEN usecase returns success WHEN OnDeleteFilterRuleConfirmed event is received THEN RuleRemoved side effect should be emitted`() =
        runTest {
            // Given
            val rule = FilterRuleEntity(
                id = 1,
                enabled = true,
                comment = null,
                dateModified = "12-01-2023",
                dateAdded = "11-01-2023",
                domain = "test.com",
                groups = emptyList(),
                type = FilterRuleTypeEntity.DENY
            )
            coEvery { removeFilterRuleUseCase(rule.domain, rule.type) } returns Result.success(
                ModifyFilterRuleResponseEntity(success = true, message = null)
            )
            viewModel = FiltersViewModel(addFilterRuleUseCase, removeFilterRuleUseCase)

            // When
            viewModel.onEvent(
                FiltersContract.Event.OnDeleteFilterRuleConfirmed(
                    ModifyFilterRule.Delete(
                        domain = "test.com",
                        type = FilterRuleTypeEntity.DENY
                    )
                )
            )
            advanceUntilIdle()

            // Then
            viewModel.sideEffect.test {
                assertThat(awaitItem()).isInstanceOf(FiltersContract.Effect.Toast.RuleRemoved::class.java)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `GIVEN usecase returns failure WHEN OnDeleteFilterRuleConfirmed event is received THEN RuleRemovalFailed side effect should be emitted`() =
        runTest {
            // Given
            val rule = FilterRuleEntity(
                id = 1,
                enabled = true,
                comment = null,
                dateModified = "12-01-2023",
                dateAdded = "11-01-2023",
                domain = "test.com",
                groups = emptyList(),
                type = FilterRuleTypeEntity.ALLOW
            )
            coEvery { removeFilterRuleUseCase(rule.domain, rule.type) } returns Result.failure(
                Exception("Test")
            )
            viewModel = FiltersViewModel(addFilterRuleUseCase, removeFilterRuleUseCase)

            // When
            viewModel.onEvent(
                FiltersContract.Event.OnDeleteFilterRuleConfirmed(
                    ModifyFilterRule.Delete(
                        domain = rule.domain,
                        type = rule.type
                    )
                )
            )
            advanceUntilIdle()

            // Then
            viewModel.sideEffect.test {
                assertThat(awaitItem()).isInstanceOf(FiltersContract.Effect.Toast.RuleRemovalFailed::class.java)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnDismissDialog event is received THEN uiState should update dialogType to None`() =
        runTest {
            // Given
            viewModel = FiltersViewModel(addFilterRuleUseCase, removeFilterRuleUseCase)
            viewModel.onEvent(FiltersContract.Event.AddFilterRuleClick)

            // When
            viewModel.onEvent(FiltersContract.Event.OnDismissDialog)

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat(result.dialogType).isEqualTo(FilterDialogType.None)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnFilterRuleItemClick event is received THEN uiState should update dialogType to ShowFilterRuleInfo`() =
        runTest {
            // Given
            val rule = FilterRuleEntity(
                id = 1,
                enabled = true,
                comment = null,
                dateModified = "12-01-2023",
                dateAdded = "11-01-2023",
                domain = "test.com",
                groups = emptyList(),
                type = FilterRuleTypeEntity.ALLOW
            )
            viewModel = FiltersViewModel(addFilterRuleUseCase, removeFilterRuleUseCase)

            // When
            viewModel.onEvent(FiltersContract.Event.OnFilterRuleItemClick(rule.toInfo()))

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat((result.dialogType as FilterDialogType.ShowFilterRuleInfo).filterRule).isEqualTo(
                        rule.toInfo()
                    )
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN AddFilterRuleClick event is received THEN uiState should update dialogType to AddFilterRule`() =
        runTest {
            // Given
            viewModel = FiltersViewModel(addFilterRuleUseCase, removeFilterRuleUseCase)

            // When
            viewModel.onEvent(FiltersContract.Event.AddFilterRuleClick)

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat((result.dialogType as FilterDialogType.AddFilterRule).type).isEqualTo(
                    FilterRuleTypeEntity.ALLOW
                )
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnFilterTabChanged event is received THEN currentPiHoleFilterRuleType should be updated`() =
        runTest {
            // Given
            viewModel = FiltersViewModel(addFilterRuleUseCase, removeFilterRuleUseCase)

            // When
            viewModel.onEvent(
                FiltersContract.Event.OnFilterTabChanged(FilterRuleTypeEntity.REGEX_DENY)
            )

            // Then
            viewModel.uiState.test {
                awaitItem() // Consume the initial state
                viewModel.onEvent(FiltersContract.Event.AddFilterRuleClick)
                val result2 = awaitItem()
                assertThat((result2.dialogType as FilterDialogType.AddFilterRule).type).isEqualTo(
                        FilterRuleTypeEntity.REGEX_DENY
                    )
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnDeleteFilterRuleClick event is received THEN uiState should update dialogType to OnConfirmFilterDelete`() =
        runTest {
            // Given
            val rule = FilterRuleEntity(
                id = 1,
                enabled = true,
                comment = null,
                dateModified = "12-01-2023",
                dateAdded = "11-01-2023",
                domain = "test.com",
                groups = emptyList(),
                type = FilterRuleTypeEntity.ALLOW
            )
            viewModel = FiltersViewModel(addFilterRuleUseCase, removeFilterRuleUseCase)

            // When
            viewModel.onEvent(FiltersContract.Event.OnDeleteFilterRuleClick(rule.toInfo()))

            // Then
            viewModel.uiState.test {
                val result = awaitItem()
                assertThat((result.dialogType as FilterDialogType.OnConfirmFilterDelete).filterRule).isEqualTo(
                        rule.toInfo()
                    )
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnDismissFilterBy event is received THEN showFilterByMenu should be false`() = runTest {
        // Given
        viewModel = FiltersViewModel(addFilterRuleUseCase, removeFilterRuleUseCase)
        viewModel.onEvent(FiltersContract.Event.OnShowFilterByMenu)

        // When
        viewModel.onEvent(FiltersContract.Event.OnDismissFilterBy)

        // Then
        viewModel.uiState.test {
            val result = awaitItem()
            assertThat(result.showFilterByMenu).isFalse()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `WHEN OnShowFilterByMenu event is received THEN showFilterByMenu should be true`() = runTest {
        // Given
        viewModel = FiltersViewModel(addFilterRuleUseCase, removeFilterRuleUseCase)

        // When
        viewModel.onEvent(FiltersContract.Event.OnShowFilterByMenu)

        // Then
        viewModel.uiState.test {
            val result = awaitItem()
            assertThat(result.showFilterByMenu).isTrue()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `WHEN OnFilterByOptionClick event is received THEN selected filter options should be updated`() = runTest {
        // Given
        viewModel = FiltersViewModel(addFilterRuleUseCase, removeFilterRuleUseCase)
        val option = FilterByOption.ALLOW_EXACT

        // When
        viewModel.onEvent(FiltersContract.Event.OnFilterByOptionClick(option))

        // Then
        viewModel.uiState.test {
            val result = awaitItem()
            assertThat(result.selectedFilterBy).contains(option)
            assertThat(result.showFilterByMenu).isFalse()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `GIVEN only one filter option selected WHEN that option is deselected THEN other option should be automatically selected`() = runTest {
        // Given
        viewModel = FiltersViewModel(addFilterRuleUseCase, removeFilterRuleUseCase)
        val firstOption = FilterByOption.ALLOW_EXACT
        val secondOption = FilterByOption.ALLOW_REGEX

        viewModel.onEvent(FiltersContract.Event.OnFilterByOptionClick(secondOption))
        viewModel.onEvent(FiltersContract.Event.OnFilterByOptionClick(firstOption))

        viewModel.onEvent(FiltersContract.Event.OnFilterByOptionClick(firstOption))

        // Then
        viewModel.uiState.test {
            val result = awaitItem()
            assertThat(result.selectedFilterBy).containsExactly(secondOption)
            assertThat(result.showFilterByMenu).isFalse()
            cancelAndConsumeRemainingEvents()
        }
    }
}
