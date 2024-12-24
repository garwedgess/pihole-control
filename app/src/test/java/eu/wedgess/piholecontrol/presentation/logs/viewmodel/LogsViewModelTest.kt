package eu.wedgess.piholecontrol.presentation.logs.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
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
            val result = viewModel.uiResult.first()
            assertThat(result).isInstanceOf(UIResult.Loading::class.java)
            assertThat((result as UIResult.Loading).loadingType)
                .isEqualTo(ResultType.Loading.WithTitle())
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
            val uiResults = mutableListOf<UIResult<LogsContract.UiState>>()
            backgroundScope.launch {
                viewModel.uiResult.toList(uiResults)
            }
            advanceUntilIdle()

            // Then
            assertThat(uiResults.any { it is UIResult.Loaded }).isTrue()
            val loadedResult = uiResults.first { it is UIResult.Loaded } as UIResult.Loaded
            assertThat(loadedResult.data.logs).isEqualTo(logs)
        }

    @Test
    fun `GIVEN usecase returns failure WHEN viewmodel is initialized THEN uiResult should emit Error state`() =
        runTest {
            // Given
            val exception = Exception("Test Exception")
            coEvery { fetchLogsUseCase() } returns flowOf(Result.failure(exception))

            // When
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)
            val uiResults = mutableListOf<UIResult<LogsContract.UiState>>()
            backgroundScope.launch {
                viewModel.uiResult.toList(uiResults)
            }
            advanceUntilIdle()

            // Then
            assertThat(uiResults.any { it is UIResult.Error }).isTrue()
            val errorResult = uiResults.first { it is UIResult.Error } as UIResult.Error
            assertThat(errorResult.errorType)
                .isInstanceOf(ResultType.Error.WithTitleAndSubTitle::class.java)
            assertThat((errorResult.errorType as ResultType.Error.WithTitleAndSubTitle).title)
                .isEqualTo(UiText.DynamicString("Failed to fetch logs"))
            assertThat((errorResult.errorType as ResultType.Error.WithTitleAndSubTitle).subTitle)
                .isEqualTo(UiText.DynamicString(exception.message ?: "Unknown error"))
        }

    @Test
    fun `GIVEN usecase returns empty list WHEN viewmodel is initialized THEN uiResult should emit Empty state`() =
        runTest {
            // Given
            coEvery { fetchLogsUseCase() } returns flowOf(Result.success(emptyList()))

            // When
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)
            val uiResults = mutableListOf<UIResult<LogsContract.UiState>>()
            backgroundScope.launch {
                viewModel.uiResult.toList(uiResults)
            }
            advanceUntilIdle()

            // Then
            assertThat(uiResults.any { it is UIResult.Empty }).isTrue()
            val emptyResult = uiResults.first { it is UIResult.Empty } as UIResult.Empty
            assertThat(emptyResult.emptyType).isInstanceOf(ResultType.Empty.WithTitle::class.java)
            assertThat((emptyResult.emptyType as ResultType.Empty.WithTitle).title).isEqualTo(
                UiText.DynamicString("No logs found")
            )
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
            val bottomSheetUiState = viewModel.bottomSheetUiState.first()
            assertThat(bottomSheetUiState.logsLimit).isEqualTo(limit)
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
            val loadedResult = viewModel.uiResult.first { it is UIResult.Loaded } as UIResult.Loaded
            assertThat(loadedResult.data.dialogType)
                .isEqualTo(LogsDialogType.ShowDetailsDialog(log))
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
            val loadedResult = viewModel.uiResult.first { it is UIResult.Loaded } as UIResult.Loaded
            assertThat(loadedResult.data.dialogType).isEqualTo(
                LogsDialogType.ShowTimePickerDialog(
                    type,
                    date
                )
            )
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
            val loadedResult = viewModel.uiResult.first { it is UIResult.Loaded } as UIResult.Loaded
            assertThat(loadedResult.data.dialogType).isEqualTo(
                LogsDialogType.ShowDatePickerDialog(
                    type
                )
            )
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
            val loadedResult = viewModel.uiResult.first { it is UIResult.Loaded } as UIResult.Loaded
            assertThat(loadedResult.data.dialogType).isEqualTo(LogsDialogType.None)
            val bottomSheetUiState = viewModel.bottomSheetUiState.first()
            assertThat(bottomSheetUiState.filterFromTime).isNotNull()
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
            val loadedResult = viewModel.uiResult.first { it is UIResult.Loaded } as UIResult.Loaded
            assertThat(loadedResult.data.sorting).isEqualTo(sorting)
            assertThat(loadedResult.data.showSortingDropdownMenu).isFalse()
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
            val bottomSheetUiState = viewModel.bottomSheetUiState.first()
            assertThat(bottomSheetUiState.selectedLogEntryStatus).isEqualTo(status)
        }

    @Test
    fun `WHEN OnClearSearchQuery event is received THEN uiState should be updated`() =
        runTest {
            // Given
            coEvery { fetchLogsUseCase() } returns flowOf(Result.success(mockk(relaxed = true)))
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)
            val query = "test"
            viewModel.onEvent(LogsContract.Event.OnSearchQueryChanged(query))
            advanceUntilIdle()

            val initialResult =
                viewModel.uiResult.first { it is UIResult.Loaded } as UIResult.Loaded
            assertThat(initialResult.data.searchQuery).isEqualTo(query)

            // When
            viewModel.onEvent(LogsContract.Event.OnClearSearchQuery(query))
            advanceUntilIdle()

            // Then
            val loadedResult = viewModel.uiResult.first { it is UIResult.Loaded } as UIResult.Loaded
            assertThat(loadedResult.data.searchQuery).isEqualTo("")
        }

    @Test
    fun `WHEN OnClearSearchQuery event is received and query is empty THEN uiState should be updated`() =
        runTest {
            // Given
            coEvery { fetchLogsUseCase() } returns flowOf(Result.success(mockk(relaxed = true)))
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)
            val query = ""
            viewModel.onEvent(LogsContract.Event.OnSearchQueryChanged(query))
            advanceUntilIdle()

            // When
            viewModel.onEvent(LogsContract.Event.OnClearSearchQuery(query))
            advanceUntilIdle()

            // Then
            val loadedResult = viewModel.uiResult.first { it is UIResult.Loaded } as UIResult.Loaded
            assertThat(loadedResult.data.showSearchView).isFalse()
        }

    @Test
    fun `WHEN OnSearchClick event is received THEN uiState should be updated`() =
        runTest {
            // Given
            coEvery { fetchLogsUseCase() } returns flowOf(Result.success(mockk(relaxed = true)))
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)
            viewModel.onEvent(LogsContract.Event.OnShowSearchView)
            advanceUntilIdle()

            // When
            viewModel.onEvent(LogsContract.Event.OnSearchClick)
            advanceUntilIdle()

            // Then
            val loadedResult = viewModel.uiResult.first { it is UIResult.Loaded } as UIResult.Loaded
            assertThat(loadedResult.data.showSearchView).isFalse()
        }

    @Test
    fun `WHEN OnSearchExpandedChanged event is received THEN uiState should be updated`() =
        runTest {
            // Given
            coEvery { fetchLogsUseCase() } returns flowOf(Result.success(mockk(relaxed = true)))
            viewModel = LogsViewModel(fetchLogsUseCase, addFilterRuleUseCase)
            val expanded = true

            // When
            viewModel.onEvent(LogsContract.Event.OnSearchExpandedChanged(expanded))
            advanceUntilIdle()

            // Then
            val loadedResult = viewModel.uiResult.first { it is UIResult.Loaded } as UIResult.Loaded
            assertThat(loadedResult.data.showSearchView).isEqualTo(expanded)
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
            val bottomSheetUiState = viewModel.bottomSheetUiState.first()
            assertThat(bottomSheetUiState.filterFromTime).isNull()
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
            val bottomSheetUiState = viewModel.bottomSheetUiState.first()
            assertThat(bottomSheetUiState.filterToTime).isNull()
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
            val loadedResult = viewModel.uiResult.first { it is UIResult.Loaded } as UIResult.Loaded
            assertThat(loadedResult.data.searchQuery).isEqualTo(query)
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
            advanceUntilIdle()
            val loadedResult = viewModel.uiResult.first { it is UIResult.Loaded } as UIResult.Loaded
            assertThat(loadedResult.data.showSortingDropdownMenu).isFalse()
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
            val loadedResult = viewModel.uiResult.first { it is UIResult.Loaded } as UIResult.Loaded
            assertThat(loadedResult.data.showSortingDropdownMenu).isTrue()
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
            val loadedResult = viewModel.uiResult.first { it is UIResult.Loaded } as UIResult.Loaded
            assertThat(loadedResult.data.showSortingDropdownMenu).isFalse()
        }
}
