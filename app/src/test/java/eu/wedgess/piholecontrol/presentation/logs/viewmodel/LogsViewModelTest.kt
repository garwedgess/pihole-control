package eu.wedgess.piholecontrol.presentation.logs.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.MainDispatcherRule
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.model.LogAnswerTypeEntity
import eu.wedgess.piholecontrol.domain.model.LogEntryEntity
import eu.wedgess.piholecontrol.domain.model.ModifyFilterRuleResponseEntity
import eu.wedgess.piholecontrol.domain.usecases.filters.AddFilterRuleUseCase
import eu.wedgess.piholecontrol.domain.usecases.logs.FetchLogsUseCase
import eu.wedgess.piholecontrol.initThreeTen
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.logs.LogsContract
import eu.wedgess.piholecontrol.presentation.logs.model.LogEntryStatus
import eu.wedgess.piholecontrol.presentation.logs.model.LogSorting
import eu.wedgess.piholecontrol.presentation.logs.model.LogsDialogType
import eu.wedgess.piholecontrol.presentation.logs.model.PickerType
import eu.wedgess.piholecontrol.utils.UiText
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

@ExperimentalCoroutinesApi
class LogsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @RelaxedMockK
    private lateinit var fetchLogsUseCase: FetchLogsUseCase

    @RelaxedMockK
    private lateinit var addFilterRuleUseCase: AddFilterRuleUseCase

    private lateinit var viewModel: LogsViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        initThreeTen()
    }

    @Test
    fun `WHEN viewmodel is initialized THEN uiResult should emit Loading state initially`() =
        runTest {
            // When
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)

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
            val logs = listOf(
                LogEntryEntity(
                    timestamp = 1672531200,
                    time = "23:53:25",
                    queryType = "query",
                    requestedDomain = "test.com",
                    answerType = LogAnswerTypeEntity.LOCAL_CACHE,
                    client = "192.168.1.1",
                    responseTime = 10
                )
            )
            coEvery { fetchLogsUseCase() } returns flowOf(Result.success(logs))

            // When
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)

            // Then
            viewModel.uiResult.test {
                val loadedResult = awaitItem()
                assertThat(loadedResult).isInstanceOf(UIResult.Loaded::class.java)
                assertThat((loadedResult as UIResult.Loaded).data.logs).isEqualTo(logs)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `GIVEN usecase returns failure WHEN viewmodel is initialized THEN uiResult should emit Error state`() =
        runTest {
            // Given
            val exception = Exception("Test Exception")
            coEvery { fetchLogsUseCase() } returns flowOf(Result.failure(exception))

            // When
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)

            // Then
            viewModel.uiResult.test {
                val errorResult = awaitItem()
                assertThat(errorResult).isInstanceOf(UIResult.Error::class.java)
                assertThat((errorResult as UIResult.Error).errorType)
                    .isInstanceOf(ResultType.Error.WithTitleAndSubTitle::class.java)
                assertThat((errorResult.errorType as ResultType.Error.WithTitleAndSubTitle).title)
                    .isEqualTo(UiText.DynamicString("Failed to fetch logs"))
                assertThat((errorResult.errorType as ResultType.Error.WithTitleAndSubTitle).subTitle)
                    .isEqualTo(UiText.DynamicString(exception.message ?: "Unknown error"))
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `GIVEN usecase returns empty list WHEN viewmodel is initialized THEN uiResult should emit Empty state`() =
        runTest {
            // Given
            coEvery { fetchLogsUseCase() } returns flowOf(Result.success(emptyList()))

            // When
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)

            // Then
            viewModel.uiResult.test {
                val emptyResult = awaitItem()
                assertThat(emptyResult).isInstanceOf(UIResult.Empty::class.java)
                assertThat((emptyResult as UIResult.Empty).emptyType)
                    .isInstanceOf(ResultType.Empty.WithTitle::class.java)
                assertThat((emptyResult.emptyType as ResultType.Empty.WithTitle).title).isEqualTo(
                    UiText.DynamicString("No logs found")
                )
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN AddToAllowList event is received THEN addFilterRuleUseCase should be called with correct parameters`() =
        runTest {
            // Given
            coEvery { fetchLogsUseCase() } returns flowOf(Result.success(emptyList()))
            val domain = "test.com"
            coEvery {
                addFilterRuleUseCase(
                    domain,
                    FilterRuleTypeEntity.ALLOW
                )
            } returns Result.success(
                ModifyFilterRuleResponseEntity(success = true, message = null)
            )
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)

            // When
            viewModel.onEvent(LogsContract.Event.AddToAllowList(domain))
            advanceUntilIdle()

            // Then
            coVerify { addFilterRuleUseCase(domain, FilterRuleTypeEntity.ALLOW) }
        }

    @Test
    fun `WHEN AddToBlockList event is received THEN addFilterRuleUseCase should be called with correct parameters`() =
        runTest {
            // Given
            coEvery { fetchLogsUseCase() } returns flowOf(Result.success(emptyList()))
            val domain = "test.com"
            coEvery {
                addFilterRuleUseCase(
                    domain,
                    FilterRuleTypeEntity.BLOCK
                )
            } returns Result.success(
                ModifyFilterRuleResponseEntity(success = true, message = null)
            )
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)

            // When
            viewModel.onEvent(LogsContract.Event.AddToBlockList(domain))
            advanceUntilIdle()

            // Then
            coVerify { addFilterRuleUseCase(domain, FilterRuleTypeEntity.BLOCK) }
        }

    @Test
    fun `WHEN OnLogLimitChanged event is received THEN bottomSheetUiState should be updated and setLogLimit should be called`() =
        runTest {
            // Given
            coEvery { fetchLogsUseCase() } returns flowOf(Result.success(emptyList()))
            val limit = 100
            every { fetchLogsUseCase.refresh() } returns true
            coEvery { fetchLogsUseCase.setLogLimit(limit) } returns true
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)

            // When
            viewModel.onEvent(LogsContract.Event.OnLogLimitChanged(limit))
            advanceUntilIdle()

            // Then
            viewModel.bottomSheetUiState.test {
                val bottomSheetUiState = awaitItem()
                assertThat(bottomSheetUiState.logsLimit).isEqualTo(limit)
                cancelAndConsumeRemainingEvents()
            }
            coVerify { fetchLogsUseCase.setLogLimit(limit) }
        }

    @Test
    fun `WHEN OnLogSelected event is received THEN uiState should be updated with ShowDetailsDialog`() =
        runTest {
            // Given
            coEvery { fetchLogsUseCase() } returns flowOf(Result.success(mockk(relaxed = true)))
            val log = LogEntryEntity(
                timestamp = 1672531200,
                time = "23:53:25",
                queryType = "query",
                requestedDomain = "test.com",
                answerType = LogAnswerTypeEntity.LOCAL_CACHE,
                client = "192.168.1.1",
                responseTime = 10
            )
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)

            // When
            viewModel.onEvent(LogsContract.Event.OnLogSelected(log))
            advanceUntilIdle()

            // Then
            // Then
            viewModel.uiResult.test {
                val loadedResult = awaitItem()
                assertThat(loadedResult).isInstanceOf(UIResult.Loaded::class.java)
                assertThat((loadedResult as UIResult.Loaded).data.dialogType)
                    .isEqualTo(LogsDialogType.ShowDetailsDialog(log))
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnDateConfirmed event is received THEN uiState should be updated with ShowTimePickerDialog`() =
        runTest {
            // Given
            coEvery { fetchLogsUseCase() } returns flowOf(Result.success(mockk(relaxed = true)))
            val date = LocalDate.now()
            val type = PickerType.FromTime
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)

            // When
            viewModel.onEvent(LogsContract.Event.OnDateConfirmed(type, date))
            advanceUntilIdle()

            // Then
            viewModel.uiResult.test {
                val loadedResult = awaitItem()
                assertThat(loadedResult).isInstanceOf(UIResult.Loaded::class.java)
                assertThat((loadedResult as UIResult.Loaded).data.dialogType).isEqualTo(
                    LogsDialogType.ShowTimePickerDialog(
                        type,
                        date
                    )
                )
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnShowDatePicker event is received THEN uiState should be updated with ShowDatePickerDialog`() =
        runTest {
            // Given
            coEvery { fetchLogsUseCase() } returns flowOf(Result.success(mockk(relaxed = true)))
            val type = PickerType.FromTime
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)

            // When
            viewModel.onEvent(LogsContract.Event.OnShowDatePicker(type))
            advanceUntilIdle()

            // Then
            viewModel.uiResult.test {
                val loadedResult = awaitItem()
                assertThat(loadedResult).isInstanceOf(UIResult.Loaded::class.java)
                assertThat((loadedResult as UIResult.Loaded).data.dialogType).isEqualTo(
                    LogsDialogType.ShowDatePickerDialog(
                        type
                    )
                )
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnTimeConfirmed event is received THEN uiState should be updated and bottomSheetUiState should be updated`() =
        runTest {
            // Given
            coEvery { fetchLogsUseCase() } returns flowOf(Result.success(mockk(relaxed = true)))
            val time = LocalDateTime.now()
            val type = PickerType.FromTime
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)

            // When
            viewModel.onEvent(LogsContract.Event.OnTimeConfirmed(type, time))
            advanceUntilIdle()

            // Then
            viewModel.uiResult.test {
                val loadedResult = awaitItem()
                assertThat(loadedResult).isInstanceOf(UIResult.Loaded::class.java)
                assertThat((loadedResult as UIResult.Loaded).data.dialogType)
                    .isEqualTo(LogsDialogType.None)
                cancelAndConsumeRemainingEvents()
            }
            viewModel.bottomSheetUiState.test {
                val bottomSheetUiState = awaitItem()
                assertThat(bottomSheetUiState.filterFromTime).isNotNull()
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnSortTypeSelected event is received THEN uiState should be updated`() =
        runTest {
            // Given
            coEvery { fetchLogsUseCase() } returns flowOf(Result.success(mockk(relaxed = true)))
            val sorting = LogSorting.DATE_ASC
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)

            // When
            viewModel.onEvent(LogsContract.Event.OnSortTypeSelected(sorting))
            advanceUntilIdle()

            // Then
            viewModel.uiResult.test {
                val loadedResult = awaitItem()
                assertThat(loadedResult).isInstanceOf(UIResult.Loaded::class.java)
                assertThat((loadedResult as UIResult.Loaded).data.sorting).isEqualTo(sorting)
                assertThat(loadedResult.data.showSortingDropdownMenu).isFalse()
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnStatusChanged event is received THEN fetchLogsUseCase should be called and bottomSheetUiState should be updated`() =
        runTest {
            // Given
            coEvery { fetchLogsUseCase() } returns flowOf(Result.success(emptyList()))
            val status = LogEntryStatus.BLOCKED
            every { fetchLogsUseCase.refresh() } returns true
            coEvery { fetchLogsUseCase.setLogStatusFilter(status) } returns true
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)

            // When
            viewModel.onEvent(LogsContract.Event.OnStatusChanged(status))
            advanceUntilIdle()

            // Then
            coVerify { fetchLogsUseCase.setLogStatusFilter(status) }
            viewModel.bottomSheetUiState.test {
                val bottomSheetUiState = awaitItem()
                assertThat(bottomSheetUiState.selectedLogEntryStatus).isEqualTo(status)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnClearSearchQuery event is received THEN uiState should be updated`() =
        runTest {
            // Given
            coEvery { fetchLogsUseCase() } returns flowOf(Result.success(mockk(relaxed = true)))
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)
            val query = "test"

            val emittedStates = mutableListOf<UIResult<LogsContract.UiState>>()
            val job = backgroundScope.launch {
                viewModel.uiResult.collect { emittedStates.add(it) }
            }

            // When
            viewModel.onEvent(LogsContract.Event.OnSearchQueryChanged(query))
            runCurrent()
            viewModel.onEvent(LogsContract.Event.OnClearSearchQuery(query))
            runCurrent()
            job.cancel()

            assertThat((emittedStates[0] as UIResult.Loaded).data.searchQuery).isEqualTo(query)
            assertThat((emittedStates[1] as UIResult.Loaded).data.searchQuery).isEqualTo("")
        }

    @Test
    fun `WHEN OnClearSearchQuery event is received and query is empty THEN uiState should be updated`() =
        runTest {
            // Given
            coEvery { fetchLogsUseCase() } returns flowOf(Result.success(mockk(relaxed = true)))
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)
            val query = ""

            val emittedStates = mutableListOf<UIResult<LogsContract.UiState>>()
            val job = backgroundScope.launch {
                viewModel.uiResult.collect { emittedStates.add(it) }
            }

            // When
            viewModel.onEvent(LogsContract.Event.OnSearchQueryChanged(query))
            runCurrent()
            viewModel.onEvent(LogsContract.Event.OnClearSearchQuery(query))
            runCurrent()
            job.cancel()

            assertThat((emittedStates[0] as UIResult.Loaded).data.searchQuery).isEqualTo(query)
            assertThat((emittedStates[0] as UIResult.Loaded).data.showSearchView).isFalse()
        }

    @Test
    fun `WHEN OnSearchClick event is received THEN uiState should be updated`() =
        runTest {
            // Given
            coEvery { fetchLogsUseCase() } returns flowOf(Result.success(mockk(relaxed = true)))
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)

            val emittedStates = mutableListOf<UIResult<LogsContract.UiState>>()
            val job = backgroundScope.launch {
                viewModel.uiResult.collect { emittedStates.add(it) }
            }

            // When
            viewModel.onEvent(LogsContract.Event.OnShowSearchView)
            runCurrent()
            viewModel.onEvent(LogsContract.Event.OnSearchClick)
            runCurrent()
            job.cancel()

            // Then
            assertThat((emittedStates[0] as UIResult.Loaded).data.showSearchView).isTrue()
            assertThat((emittedStates[1] as UIResult.Loaded).data.showSearchView).isFalse()
        }

    @Test
    fun `WHEN OnSearchExpandedChanged event is received THEN uiState should be updated`() =
        runTest {
            // Given
            coEvery { fetchLogsUseCase() } returns flowOf(Result.success(mockk(relaxed = true)))
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)
            val expanded = true

            val emittedStates = mutableListOf<UIResult<LogsContract.UiState>>()
            val job = backgroundScope.launch {
                viewModel.uiResult.collect { emittedStates.add(it) }
            }

            // When
            viewModel.onEvent(LogsContract.Event.OnSearchExpandedChanged(expanded))
            runCurrent()
            viewModel.onEvent(LogsContract.Event.OnSearchExpandedChanged(!expanded))
            runCurrent()
            job.cancel()

            // Then
            assertThat((emittedStates[0] as UIResult.Loaded).data.showSearchView).isTrue()
            assertThat((emittedStates[1] as UIResult.Loaded).data.showSearchView).isFalse()
        }

    @Test
    fun `WHEN OnFromTimeCleared event is received THEN bottomSheetUiState should be updated`() =
        runTest {
            // Given
            coEvery { fetchLogsUseCase() } returns flowOf(Result.success(mockk(relaxed = true)))
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)
            viewModel.onEvent(
                LogsContract.Event.OnTimeConfirmed(
                    PickerType.FromTime,
                    LocalDateTime.now()
                )
            )
            advanceUntilIdle()

            // When
            viewModel.onEvent(LogsContract.Event.OnFromTimeCleared)
            advanceUntilIdle()

            // Then
            viewModel.bottomSheetUiState.test {
                val bottomSheetUiState = awaitItem()
                assertThat(bottomSheetUiState.filterFromTime).isNull()
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnToTimeCleared event is received THEN bottomSheetUiState should be updated`() =
        runTest {
            // Given
            coEvery { fetchLogsUseCase() } returns flowOf(Result.success(mockk(relaxed = true)))
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)
            viewModel.onEvent(
                LogsContract.Event.OnTimeConfirmed(
                    PickerType.ToTime,
                    LocalDateTime.now()
                )
            )
            advanceUntilIdle()

            // When
            viewModel.onEvent(LogsContract.Event.OnToTimeCleared)
            advanceUntilIdle()

            // Then
            viewModel.bottomSheetUiState.test {
                val bottomSheetUiState = awaitItem()
                assertThat(bottomSheetUiState.filterToTime).isNull()
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnSearchQueryChanged event is received THEN uiState should be updated`() =
        runTest {
            // Given
            coEvery { fetchLogsUseCase() } returns flowOf(Result.success(mockk(relaxed = true)))
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)
            val query = "test"

            // When
            viewModel.onEvent(LogsContract.Event.OnSearchQueryChanged(query))
            advanceUntilIdle()

            // Then
            viewModel.uiResult.test {
                val loadedResult = awaitItem()
                assertThat(loadedResult).isInstanceOf(UIResult.Loaded::class.java)
                assertThat((loadedResult as UIResult.Loaded).data.searchQuery).isEqualTo(query)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnSortingDismissed event is received THEN uiState should be updated`() =
        runTest {
            // Given
            coEvery { fetchLogsUseCase() } returns flowOf(Result.success(mockk(relaxed = true)))
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)
            viewModel.onEvent(LogsContract.Event.OnShowSortingMenu)
            advanceUntilIdle()

            // When
            viewModel.onEvent(LogsContract.Event.OnSortingDismissed)
            advanceUntilIdle()

            // Then
            viewModel.uiResult.test {
                val loadedResult = awaitItem()
                assertThat(loadedResult).isInstanceOf(UIResult.Loaded::class.java)
                assertThat((loadedResult as UIResult.Loaded).data.showSortingDropdownMenu).isFalse()
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnShowSortingMenu event is received THEN uiState should be updated`() =
        runTest {
            // Given
            coEvery { fetchLogsUseCase() } returns flowOf(Result.success(mockk(relaxed = true)))
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)

            // When
            viewModel.onEvent(LogsContract.Event.OnShowSortingMenu)
            advanceUntilIdle()

            // Then
            viewModel.uiResult.test {
                val loadedResult = awaitItem()
                assertThat(loadedResult).isInstanceOf(UIResult.Loaded::class.java)
                assertThat((loadedResult as UIResult.Loaded).data.showSortingDropdownMenu).isTrue()
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnHideSortingMenu event is received THEN uiState should be updated`() =
        runTest {
            // Given
            coEvery { fetchLogsUseCase() } returns flowOf(Result.success(mockk(relaxed = true)))
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)
            viewModel.onEvent(LogsContract.Event.OnShowSortingMenu)
            advanceUntilIdle()

            // When
            viewModel.onEvent(LogsContract.Event.OnHideSortingMenu)
            advanceUntilIdle()

            // Then
            viewModel.uiResult.test {
                val loadedResult = awaitItem()
                assertThat(loadedResult).isInstanceOf(UIResult.Loaded::class.java)
                assertThat((loadedResult as UIResult.Loaded).data.showSortingDropdownMenu).isFalse()
                cancelAndConsumeRemainingEvents()
            }
        }
}
