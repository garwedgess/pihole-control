package eu.wedgess.piholecontrol.presentation.logs.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.MainDispatcherRule
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.model.ModifyFilterRuleResponseEntity
import eu.wedgess.piholecontrol.domain.model.PiHoleLogsEntity
import eu.wedgess.piholecontrol.domain.model.RefreshMode
import eu.wedgess.piholecontrol.domain.usecases.filters.AddFilterRuleUseCase
import eu.wedgess.piholecontrol.domain.usecases.logs.FetchLogFilterSuggestionsUseCase
import eu.wedgess.piholecontrol.domain.usecases.logs.FetchLogsUseCase
import eu.wedgess.piholecontrol.initThreeTen
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.logs.LogsContract
import eu.wedgess.piholecontrol.presentation.logs.extensions.toInfo
import eu.wedgess.piholecontrol.presentation.logs.model.LogEntryInfo
import eu.wedgess.piholecontrol.presentation.logs.model.LogEntryStatus
import eu.wedgess.piholecontrol.presentation.logs.model.LogSorting
import eu.wedgess.piholecontrol.presentation.logs.model.LogsDialogType
import eu.wedgess.piholecontrol.presentation.logs.model.PickerType
import eu.wedgess.piholecontrol.utils.UiText
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
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
    private lateinit var fetchLogFilterSuggestionsUseCase: FetchLogFilterSuggestionsUseCase

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
            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
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
            val logs = listOf(
                PiHoleLogsEntity.Version5(
                    timestamp = 1672531200,
                    time = "23:53:25",
                    queryType = PiHoleLogsEntity.LogEntryQueryTypeEntity.AAAA,
                    domain = "test.com",
                    answerType = PiHoleLogsEntity.LogsAnswerTypeEntity.LOCAL_CACHE,
                    client = "192.168.1.1",
                    replyTime = 1.0
                )
            )
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(logs))
            coEvery { fetchLogFilterSuggestionsUseCase() } returns mockk(relaxed = true)

            // When
            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )

            // Then
            viewModel.uiResult.test {
                val loadedResult = awaitItem()
                assertThat(loadedResult).isInstanceOf(UIResult.Loaded::class.java)
                assertThat((loadedResult as UIResult.Loaded).data.logs)
                    .isEqualTo(logs.map { it.toInfo() })
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `GIVEN usecase returns failure WHEN viewmodel is initialized THEN uiResult should emit Error state`() =
        runTest {
            // Given
            val exception = Exception("Test Exception")
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.failure(exception))

            // When
            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )

            // Then
            viewModel.uiResult.test {
                val errorResult = awaitItem()
                assertThat(errorResult).isInstanceOf(UIResult.Error::class.java)
                assertThat((errorResult as UIResult.Error).errorType)
                    .isInstanceOf(ResultType.Error.WithTitleAndSubTitleAndRetry::class.java)
                assertThat((errorResult.errorType as ResultType.Error.WithTitleAndSubTitleAndRetry).title)
                    .isEqualTo(UiText.DynamicString("Failed to fetch logs"))
                assertThat((errorResult.errorType as ResultType.Error.WithTitleAndSubTitleAndRetry).subTitle)
                    .isEqualTo(UiText.DynamicString(exception.message ?: "Unknown error"))
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `GIVEN usecase returns empty list WHEN viewmodel is initialized THEN uiResult should emit Empty state`() =
        runTest {
            // Given
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(emptyList()))

            // When
            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )

            // Then
            viewModel.uiResult.test {
                val emptyResult = awaitItem()
                assertThat(emptyResult).isInstanceOf(UIResult.Empty::class.java)
                assertThat((emptyResult as UIResult.Empty).emptyType)
                    .isInstanceOf(ResultType.Empty.WithTitle::class.java)
                assertThat((emptyResult.emptyType as ResultType.Empty.WithTitle).title).isEqualTo(
                    UiText.StringResource(R.string.logs_msg_empty)
                )
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN AddToAllowList event is received THEN addFilterRuleUseCase should be called with correct parameters`() =
        runTest {
            // Given
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(emptyList()))
            val domain = "test.com"
            coEvery {
                addFilterRuleUseCase(
                    domain,
                    FilterRuleTypeEntity.ALLOW
                )
            } returns Result.success(
                ModifyFilterRuleResponseEntity(success = true, message = null)
            )
            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )

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
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(emptyList()))
            val domain = "test.com"
            coEvery {
                addFilterRuleUseCase(
                    domain,
                    FilterRuleTypeEntity.DENY
                )
            } returns Result.success(
                ModifyFilterRuleResponseEntity(success = true, message = null)
            )
            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )

            // When
            viewModel.onEvent(LogsContract.Event.AddToBlockList(domain))
            advanceUntilIdle()

            // Then
            coVerify { addFilterRuleUseCase(domain, FilterRuleTypeEntity.DENY) }
        }

    @Test
    fun `WHEN OnLogSelected event is received THEN uiState should be updated with ShowDetailsDialog`() =
        runTest {
            // Given
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(mockk(relaxed = true)))
            val log = LogEntryInfo.Version5(
                timestamp = 1672531200,
                time = "23:53:25",
                queryType = PiHoleLogsEntity.LogEntryQueryTypeEntity.A,
                domain = "test.com",
                answerType = PiHoleLogsEntity.LogsAnswerTypeEntity.LOCAL_CACHE,
                client = "192.168.1.1",
                replyTime = 1.0
            )
            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )

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
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(mockk(relaxed = true)))
            val date = LocalDate.now()
            val type = PickerType.FromTime
            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )

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
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(mockk(relaxed = true)))
            val type = PickerType.FromTime
            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )

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
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(mockk(relaxed = true)))
            val time = LocalDateTime.now()
            val type = PickerType.FromTime
            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )

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
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(mockk(relaxed = true)))
            val sorting = LogSorting.DATE_ASC
            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )

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
    fun `WHEN OnClearSearchQuery event is received THEN uiState should be updated`() =
        runTest {
            // Given
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(mockk(relaxed = true)))
            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )
            val query = "test"

            val emittedStates = mutableListOf<LogsContract.SearchUiState>()
            val job = backgroundScope.launch {
                viewModel.searchUiState.collect { emittedStates.add(it) }
            }

            // When
            viewModel.onEvent(LogsContract.Event.OnSearchQueryChanged(query))
            runCurrent()
            viewModel.onEvent(LogsContract.Event.OnClearSearchQuery(query))
            runCurrent()
            job.cancel()

            assertThat(emittedStates[0].searchQuery).isEqualTo(query)
            assertThat(emittedStates[1].searchQuery).isEqualTo("")
        }

    @Test
    fun `WHEN OnClearSearchQuery event is received and query is empty THEN uiState should be updated`() =
        runTest {
            // Given
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(mockk(relaxed = true)))
            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )
            val query = ""

            val emittedStates = mutableListOf<LogsContract.SearchUiState>()
            val job = backgroundScope.launch {
                viewModel.searchUiState.collect { emittedStates.add(it) }
            }

            // When
            viewModel.onEvent(LogsContract.Event.OnSearchQueryChanged(query))
            runCurrent()
            viewModel.onEvent(LogsContract.Event.OnClearSearchQuery(query))
            runCurrent()
            job.cancel()

            assertThat(emittedStates[0].searchQuery).isEqualTo(query)
            assertThat(emittedStates[0].showSearchView).isFalse()
        }

    @Test
    fun `WHEN OnSearchExpandedChanged event is received THEN uiState should be updated`() =
        runTest {
            // Given
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(mockk(relaxed = true)))
            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )
            val expanded = true

            val emittedStates = mutableListOf<LogsContract.SearchUiState>()
            val job = backgroundScope.launch {
                viewModel.searchUiState.collect { emittedStates.add(it) }
            }

            // When
            viewModel.onEvent(LogsContract.Event.OnSearchExpandedChanged(expanded))
            runCurrent()
            viewModel.onEvent(LogsContract.Event.OnSearchExpandedChanged(!expanded))
            runCurrent()
            job.cancel()

            // Then
            assertThat(emittedStates).hasSize(2)
            assertThat(emittedStates.first().showSearchView).isTrue()
            assertThat(emittedStates.last().showSearchView).isFalse()
        }

    @Test
    fun `WHEN OnFromTimeCleared event is received THEN bottomSheetUiState should be updated`() =
        runTest {
            // Given
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(mockk(relaxed = true)))
            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )
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
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(mockk(relaxed = true)))
            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )
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
    fun `WHEN OnShowSearchView event is received THEN searchUiState should be updated`() =
        runTest {
            // Given
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(mockk(relaxed = true)))
            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )

            // When
            viewModel.onEvent(LogsContract.Event.OnShowSearchView)
            advanceUntilIdle()

            // Then
            viewModel.uiResult.test {
                val loadedResult = awaitItem()
                assertThat(loadedResult).isInstanceOf(UIResult.Loaded::class.java)
                assertThat(viewModel.searchUiState.value.showSearchView).isTrue()
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnSearchQueryChanged event is received THEN uiState should be updated`() =
        runTest {
            // Given
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(mockk(relaxed = true)))
            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )
            val query = "test"

            // When
            viewModel.onEvent(LogsContract.Event.OnSearchQueryChanged(query))
            advanceUntilIdle()

            // Then
            viewModel.uiResult.test {
                val loadedResult = awaitItem()
                assertThat(loadedResult).isInstanceOf(UIResult.Loaded::class.java)
                assertThat(viewModel.searchUiState.value.searchQuery).isEqualTo(query)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnSortingDismissed event is received THEN uiState should be updated`() =
        runTest {
            // Given
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(mockk(relaxed = true)))
            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )
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
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(mockk(relaxed = true)))
            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )

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
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(mockk(relaxed = true)))
            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )
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

    @Test
    fun `WHEN OnLiveLoggingChanged event is received THEN uiState should be updated and refresh mode should be set`() =
        runTest {
            // Given
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(mockk(relaxed = true)))

            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )

            val isLive = true

            // When
            viewModel.onEvent(LogsContract.Event.OnLiveLoggingChanged(isLive))
            advanceUntilIdle()

            // Then
            viewModel.uiResult.test {
                val loadedResult = awaitItem()
                assertThat(loadedResult).isInstanceOf(UIResult.Loaded::class.java)
                assertThat((loadedResult as UIResult.Loaded).data.liveLogging).isEqualTo(isLive)
                cancelAndConsumeRemainingEvents()
            }

            coVerify { fetchLogsUseCase.setRefreshMode(RefreshMode.Automatic(1_000)) }
            coVerify { fetchLogsUseCase.refresh() }
        }

    @Test
    fun `WHEN OnLiveLoggingChanged event is received with false THEN refresh mode should be set to null`() =
        runTest {
            // Given
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(mockk(relaxed = true)))

            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )

            val isLive = false

            // When
            viewModel.onEvent(LogsContract.Event.OnLiveLoggingChanged(isLive))
            advanceUntilIdle()

            // Then
            viewModel.uiResult.test {
                val loadedResult = awaitItem()
                assertThat(loadedResult).isInstanceOf(UIResult.Loaded::class.java)
                assertThat((loadedResult as UIResult.Loaded).data.liveLogging).isEqualTo(isLive)
                cancelAndConsumeRemainingEvents()
            }

            coVerify { fetchLogsUseCase.setRefreshMode(RefreshMode.Automatic(null)) }
            coVerify { fetchLogsUseCase.refresh() }
        }

    @Test
    fun `WHEN OnToggleFiltersBottomSheet event is received THEN bottomSheetUiState should be updated`() =
        runTest {
            // Given
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(mockk(relaxed = true)))

            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )

            // When
            viewModel.onEvent(LogsContract.Event.OnToggleFiltersBottomSheet(true))
            advanceUntilIdle()

            // Then
            viewModel.bottomSheetUiState.test {
                val bottomSheetUiState = awaitItem()
                assertThat(bottomSheetUiState.showFilterBottomSheet).isTrue()
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnToggleAdvancedFilteringOptions event is received THEN bottomSheetUiState should be updated`() =
        runTest {
            // Given
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(mockk(relaxed = true)))

            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )

            // When
            viewModel.onEvent(LogsContract.Event.OnToggleAdvancedFilteringOptions(true))
            advanceUntilIdle()

            // Then
            viewModel.bottomSheetUiState.test {
                val bottomSheetUiState = awaitItem()
                assertThat(bottomSheetUiState.showAdvancedFiltering).isTrue()
                assertThat(bottomSheetUiState.showBasicFiltering).isFalse()
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnToggleBasicFilteringOptions event is received THEN bottomSheetUiState should be updated`() =
        runTest {
            // Given
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(mockk(relaxed = true)))

            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )

            // When
            viewModel.onEvent(LogsContract.Event.OnToggleBasicFilteringOptions(true))
            advanceUntilIdle()

            // Then
            viewModel.bottomSheetUiState.test {
                val bottomSheetUiState = awaitItem()
                assertThat(bottomSheetUiState.showBasicFiltering).isTrue()
                assertThat(bottomSheetUiState.showAdvancedFiltering).isFalse()
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN AddToAllowList event fails THEN side effect should be emitted`() = runTest {
        // Given
        coEvery {
            fetchLogsUseCase(
                limit = any(),
                status = any(),
                query = any(),
                clientName = any(),
                clientIp = any(),
                queryType = any(),
                advancedStatus = any(),
                from = any(),
                until = any()
            )
        } returns flowOf(Result.success(emptyList()))

        val domain = "test.com"
        val errorMessage = "Add to allow list failed"
        coEvery {
            addFilterRuleUseCase(domain, FilterRuleTypeEntity.ALLOW)
        } returns Result.failure(Exception(errorMessage))

        viewModel =
            LogsViewModel(fetchLogsUseCase, fetchLogFilterSuggestionsUseCase, addFilterRuleUseCase)

        val sideEffects = mutableListOf<LogsContract.Effect>()
        val job = backgroundScope.launch {
            viewModel.sideEffect.collect { sideEffects.add(it) }
        }

        // When
        viewModel.onEvent(LogsContract.Event.AddToAllowList(domain))
        advanceUntilIdle()
        job.cancel()

        // Then
        assertThat(sideEffects).hasSize(1)
        assertThat(sideEffects[0]).isInstanceOf(LogsContract.Effect.Snackbar.AddDomainToAllowListFailed::class.java)

        val failedEffect = sideEffects[0] as LogsContract.Effect.Snackbar.AddDomainToAllowListFailed
        assertThat(failedEffect.domain).isEqualTo(domain)
    }

    @Test
    fun `WHEN AddToBlockList event fails THEN side effect should be emitted`() = runTest {
        // Given
        coEvery {
            fetchLogsUseCase(
                limit = any(),
                status = any(),
                query = any(),
                clientName = any(),
                clientIp = any(),
                queryType = any(),
                advancedStatus = any(),
                from = any(),
                until = any()
            )
        } returns flowOf(Result.success(emptyList()))

        val domain = "test.com"
        val errorMessage = "Add to block list failed"
        coEvery {
            addFilterRuleUseCase(domain, FilterRuleTypeEntity.DENY)
        } returns Result.failure(Exception(errorMessage))

        viewModel =
            LogsViewModel(fetchLogsUseCase, fetchLogFilterSuggestionsUseCase, addFilterRuleUseCase)

        val sideEffects = mutableListOf<LogsContract.Effect>()
        val job = backgroundScope.launch {
            viewModel.sideEffect.collect { sideEffects.add(it) }
        }

        // When
        viewModel.onEvent(LogsContract.Event.AddToBlockList(domain))
        advanceUntilIdle()
        job.cancel()

        // Then
        assertThat(sideEffects).hasSize(1)
        assertThat(sideEffects[0]).isInstanceOf(LogsContract.Effect.Snackbar.AddDomainToDenyListFailed::class.java)

        val failedEffect = sideEffects[0] as LogsContract.Effect.Snackbar.AddDomainToDenyListFailed
        assertThat(failedEffect.domain).isEqualTo(domain)
    }

    @Test
    fun `WHEN OnStatusChanged event is received THEN bottomSheetUiState should be updated`() =
        runTest {
            // Given
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(mockk(relaxed = true)))

            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )

            val status = LogEntryStatus.BLOCKED

            // When
            viewModel.onEvent(LogsContract.Event.OnStatusChanged(status))
            advanceUntilIdle()

            // Then
            viewModel.bottomSheetUiState.test {
                val bottomSheetUiState = awaitItem()
                assertThat(bottomSheetUiState.selectedLogEntryStatus).isEqualTo(status)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnLogLimitChanged event is received THEN bottomSheetUiState should be updated`() =
        runTest {
            // Given
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(mockk(relaxed = true)))

            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )

            val limit = 100

            // When
            viewModel.onEvent(LogsContract.Event.OnLogLimitChanged(limit))
            advanceUntilIdle()

            // Then
            viewModel.bottomSheetUiState.test {
                val bottomSheetUiState = awaitItem()
                assertThat(bottomSheetUiState.logsLimit).isEqualTo(limit)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnAdvancedStatusFilterChanged event is received THEN bottomSheetUiState should be updated`() =
        runTest {
            // Given
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(mockk(relaxed = true)))

            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )

            val advancedStatus = "example_status"

            // When
            viewModel.onEvent(LogsContract.Event.OnAdvancedStatusFilterChanged(advancedStatus))
            advanceUntilIdle()

            // Then
            viewModel.bottomSheetUiState.test {
                val bottomSheetUiState = awaitItem()
                assertThat(bottomSheetUiState.selectedAdvancedStatus).isEqualTo(advancedStatus)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnClientIpFilterChanged event is received THEN bottomSheetUiState should be updated`() =
        runTest {
            // Given
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(mockk(relaxed = true)))

            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )

            val clientIp = "192.168.1.1"

            // When
            viewModel.onEvent(LogsContract.Event.OnClientIpFilterChanged(clientIp))
            advanceUntilIdle()

            // Then
            viewModel.bottomSheetUiState.test {
                val bottomSheetUiState = awaitItem()
                assertThat(bottomSheetUiState.selectedClientIp).isEqualTo(clientIp)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnClientNameFilterChanged event is received THEN bottomSheetUiState should be updated`() =
        runTest {
            // Given
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(mockk(relaxed = true)))

            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )

            val clientName = "example_client"

            // When
            viewModel.onEvent(LogsContract.Event.OnClientNameFilterChanged(clientName))
            advanceUntilIdle()

            // Then
            viewModel.bottomSheetUiState.test {
                val bottomSheetUiState = awaitItem()
                assertThat(bottomSheetUiState.selectedClientName).isEqualTo(clientName)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnQueryTypeFilterChanged event is received THEN bottomSheetUiState should be updated`() =
        runTest {
            // Given
            coEvery {
                fetchLogsUseCase(
                    limit = any(),
                    status = any(),
                    query = any(),
                    clientName = any(),
                    clientIp = any(),
                    queryType = any(),
                    advancedStatus = any(),
                    from = any(),
                    until = any()
                )
            } returns flowOf(Result.success(mockk(relaxed = true)))

            viewModel = LogsViewModel(
                fetchLogsUseCase,
                fetchLogFilterSuggestionsUseCase,
                addFilterRuleUseCase
            )

            val queryType = "example_query_type"

            // When
            viewModel.onEvent(LogsContract.Event.OnQueryTypeFilterChanged(queryType))
            advanceUntilIdle()

            // Then
            viewModel.bottomSheetUiState.test {
                val bottomSheetUiState = awaitItem()
                assertThat(bottomSheetUiState.selectedQueryType).isEqualTo(queryType)
                cancelAndConsumeRemainingEvents()
            }
        }

    // Continuing from the previous tests...

    @Test
    fun `WHEN OnDismissDialog event is received THEN uiState should be updated`() = runTest {
        // Given
        coEvery {
            fetchLogsUseCase(
                limit = any(),
                status = any(),
                query = any(),
                clientName = any(),
                clientIp = any(),
                queryType = any(),
                advancedStatus = any(),
                from = any(),
                until = any()
            )
        } returns flowOf(Result.success(mockk(relaxed = true)))

        viewModel =
            LogsViewModel(fetchLogsUseCase, fetchLogFilterSuggestionsUseCase, addFilterRuleUseCase)

        // When
        viewModel.onEvent(LogsContract.Event.OnShowDatePicker(PickerType.FromTime))
        advanceUntilIdle()

        viewModel.onEvent(LogsContract.Event.OnDismissDialog)
        advanceUntilIdle()

        // Then
        viewModel.uiResult.test {
            val loadedResult = awaitItem()
            assertThat(loadedResult).isInstanceOf(UIResult.Loaded::class.java)
            assertThat((loadedResult as UIResult.Loaded).data.dialogType)
                .isEqualTo(LogsDialogType.None)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `WHEN AddToAllowList event is successful THEN side effect should be emitted`() = runTest {
        // Given
        coEvery {
            fetchLogsUseCase(
                limit = any(),
                status = any(),
                query = any(),
                clientName = any(),
                clientIp = any(),
                queryType = any(),
                advancedStatus = any(),
                from = any(),
                until = any()
            )
        } returns flowOf(Result.success(emptyList()))

        val domain = "test.com"
        coEvery {
            addFilterRuleUseCase(domain, FilterRuleTypeEntity.ALLOW)
        } returns Result.success(
            ModifyFilterRuleResponseEntity(success = true, message = null)
        )

        viewModel =
            LogsViewModel(fetchLogsUseCase, fetchLogFilterSuggestionsUseCase, addFilterRuleUseCase)

        val sideEffects = mutableListOf<LogsContract.Effect>()
        val job = backgroundScope.launch {
            viewModel.sideEffect.collect { sideEffects.add(it) }
        }

        // When
        viewModel.onEvent(LogsContract.Event.AddToAllowList(domain))
        advanceUntilIdle()
        job.cancel()

        // Then
        assertThat(sideEffects).hasSize(1)
        assertThat(sideEffects[0]).isInstanceOf(LogsContract.Effect.Snackbar.DomainAddedToAllowList::class.java)
    }

    @Test
    fun `WHEN AddToBlockList event is successful THEN side effect should be emitted`() = runTest {
        // Given
        coEvery {
            fetchLogsUseCase(
                limit = any(),
                status = any(),
                query = any(),
                clientName = any(),
                clientIp = any(),
                queryType = any(),
                advancedStatus = any(),
                from = any(),
                until = any()
            )
        } returns flowOf(Result.success(emptyList()))

        val domain = "test.com"
        coEvery {
            addFilterRuleUseCase(domain, FilterRuleTypeEntity.DENY)
        } returns Result.success(
            ModifyFilterRuleResponseEntity(success = true, message = null)
        )

        viewModel =
            LogsViewModel(fetchLogsUseCase, fetchLogFilterSuggestionsUseCase, addFilterRuleUseCase)

        val sideEffects = mutableListOf<LogsContract.Effect>()
        val job = backgroundScope.launch {
            viewModel.sideEffect.collect { sideEffects.add(it) }
        }

        // When
        viewModel.onEvent(LogsContract.Event.AddToBlockList(domain))
        advanceUntilIdle()
        job.cancel()

        // Then
        assertThat(sideEffects).hasSize(1)
        assertThat(sideEffects[0]).isInstanceOf(LogsContract.Effect.Snackbar.DomainAddedToBlockList::class.java)
    }
}
