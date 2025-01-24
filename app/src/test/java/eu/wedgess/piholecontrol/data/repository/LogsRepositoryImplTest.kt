package eu.wedgess.piholecontrol.data.repository

import TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.v5.LogsApiServiceV5
import eu.wedgess.piholecontrol.data.api.v6.LogsApiServiceV6
import eu.wedgess.piholecontrol.data.model.enums.LogsAnswerType
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleLogSuggestionsResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleLogsResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleLogEntryDnssecV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleLogEntryReplyTypeV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleLogEntryStatusV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleLogEntryTypeV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleLogSuggestionsResponseDataV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleLogsResponseDataV6
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.LogsRepository
import eu.wedgess.piholecontrol.initThreeTen
import eu.wedgess.piholecontrol.presentation.logs.model.LogEntryStatus
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LogsRepositoryImplTest {

    @MockK
    private lateinit var apiServiceV5: LogsApiServiceV5

    @MockK
    private lateinit var apiServiceV6: LogsApiServiceV6
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var target: LogsRepository

    @Before
    fun setUp() {
        initThreeTen()
        MockKAnnotations.init(this)
        dispatcherProvider = TestDispatcherProvider()
        target = LogsRepositoryImpl(apiServiceV5, apiServiceV6, dispatcherProvider)
    }

    @Test
    fun `fetchLogs - with V5 connection - invokes V5 API and returns success`() = runTest {
        // Given
        val activeConnection = mockk<ConnectionEntity.Version5>(relaxed = true)
        val limit = 10
        val responseData = PiHoleLogsResponseDataV5(
            data = listOf(
                PiHoleLogsResponseDataV5.PiHoleLogEntryData(
                    timestamp = 1000L,
                    queryType = "A",
                    requestedDomain = "example.com",
                    client = "1.2.3.4",
                    answerType = LogsAnswerType.EXACT_BLOCK,
                    responseTime = 0
                )
            )
        )
        coEvery { apiServiceV5.fetchLogs(activeConnection, limit) } returns Result.success(
            responseData
        )

        // When
        val result = target.fetchLogs(
            connection = activeConnection,
            limit = limit,
            status = LogEntryStatus.ALL,
            query = "",
            clientIp = null,
            clientName = null,
            queryType = null,
            advancedStatus = null,
            from = null,
            until = null
        )

        // Then
        assertThat(result.isSuccess).isTrue()
        val resultList = result.getOrNull()
        assertThat(resultList).hasSize(1)
        assertThat(resultList?.first()?.domain).isEqualTo("example.com")
        coVerify { apiServiceV5.fetchLogs(activeConnection, limit) }
        coVerify(exactly = 0) {
            apiServiceV6.fetchLogs(
                connection = any(),
                limit = any(),
                domain = any(),
                clientIp = any(),
                clientName = any(),
                queryType = any(),
                advancedStatus = any(),
                from = any(),
                until = any()
            )
        }
    }

    @Test
    fun `fetchLogs - with V5 connection - invokes V5 API and returns failure`() = runTest {
        // Given
        val activeConnection = mockk<ConnectionEntity.Version5>(relaxed = true)
        val limit = 10
        val exception = RuntimeException("API error")
        coEvery { apiServiceV5.fetchLogs(activeConnection, limit) } returns Result.failure(
            exception
        )

        // When
        val result = target.fetchLogs(
            connection = activeConnection,
            limit = limit,
            status = LogEntryStatus.ALL,
            query = "",
            clientIp = null,
            clientName = null,
            queryType = null,
            advancedStatus = null,
            from = null,
            until = null
        )

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { apiServiceV5.fetchLogs(activeConnection, limit) }
        coVerify(exactly = 0) {
            apiServiceV6.fetchLogs(
                connection = any(),
                limit = any(),
                domain = any(),
                clientIp = any(),
                clientName = any(),
                queryType = any(),
                advancedStatus = any(),
                from = any(),
                until = any()
            )
        }
    }

    @Test
    fun `fetchLogs - with V6 connection - invokes V6 API and returns success`() = runTest {
        // Given
        val activeConnection = mockk<ConnectionEntity.Version6>(relaxed = true)
        val limit = 10
        val responseData = PiHoleLogsResponseDataV6(
            queries = listOf(
                PiHoleLogsResponseDataV6.PiHoleLogEntryData(
                    id = 1,
                    time = 0.1,
                    type = PiHoleLogEntryTypeV6.A,
                    status = PiHoleLogEntryStatusV6.GRAVITY,
                    dnssec = PiHoleLogEntryDnssecV6.UNKNOWN,
                    domain = "example.com",
                    upstream = null,
                    reply = PiHoleLogsResponseDataV6.PiHoleLogEntryData.PiHoleLogEntryReply(
                        type = PiHoleLogEntryReplyTypeV6.IP,
                        time = 0.1
                    ),
                    client = PiHoleLogsResponseDataV6.PiHoleLogEntryData.PiHoleLogEntryClient(
                        ip = "1.2.3.4",
                        name = null
                    ),
                    listId = null,
                    ede = PiHoleLogsResponseDataV6.PiHoleLogEntryData.PiHoleLogEntryEde(0, null),
                    cname = null
                )
            ),
            cursor = 0,
            recordsTotal = 1,
            recordsFiltered = 1,
            draw = 1,
            took = 0.1
        )
        coEvery {
            apiServiceV6.fetchLogs(
                connection = activeConnection,
                limit = limit,
                domain = null,
                clientIp = null,
                clientName = null,
                queryType = null,
                advancedStatus = null,
                from = null,
                until = null
            )
        } returns Result.success(responseData)

        // When
        val result = target.fetchLogs(
            connection = activeConnection,
            limit = limit,
            status = LogEntryStatus.ALL,
            query = "",
            clientIp = null,
            clientName = null,
            queryType = null,
            advancedStatus = null,
            from = null,
            until = null
        )

        // Then
        assertThat(result.isSuccess).isTrue()
        val resultList = result.getOrNull()
        assertThat(resultList).hasSize(1)
        assertThat(resultList?.first()?.domain).isEqualTo("example.com")
        coVerify {
            apiServiceV6.fetchLogs(
                connection = activeConnection,
                limit = limit,
                domain = null,
                clientIp = null,
                clientName = null,
                queryType = null,
                advancedStatus = null,
                from = null,
                until = null
            )
        }
        coVerify(exactly = 0) { apiServiceV5.fetchLogs(any(), any()) }
    }

    @Test
    fun `fetchLogs - with V5 and status filter - returns only blocked entries`() = runTest {
        // Given
        val activeConnection = mockk<ConnectionEntity.Version5>(relaxed = true)
        val responseData = PiHoleLogsResponseDataV5(
            data = listOf(
                PiHoleLogsResponseDataV5.PiHoleLogEntryData(
                    timestamp = 1000L,
                    queryType = "A",
                    requestedDomain = "blocked.example.com",
                    client = "1.2.3.4",
                    answerType = LogsAnswerType.EXACT_BLOCK,
                    responseTime = 0
                ),
                PiHoleLogsResponseDataV5.PiHoleLogEntryData(
                    timestamp = 2000L,
                    queryType = "A",
                    requestedDomain = "allowed.example.com",
                    client = "1.2.3.4",
                    answerType = LogsAnswerType.UPSTREAM,
                    responseTime = 0
                ),
                PiHoleLogsResponseDataV5.PiHoleLogEntryData(
                    timestamp = 3000L,
                    queryType = "A",
                    requestedDomain = "blocked.test.com",
                    client = "1.2.3.4",
                    answerType = LogsAnswerType.EXACT_BLOCK,
                    responseTime = 0
                )
            )
        )
        coEvery { apiServiceV5.fetchLogs(any(), any()) } returns Result.success(responseData)

        // When
        val result = target.fetchLogs(
            connection = activeConnection,
            limit = 10,
            status = LogEntryStatus.BLOCKED,
            query = "",
            clientIp = null,
            clientName = null,
            queryType = null,
            advancedStatus = null,
            from = null,
            until = null
        )

        // Then
        assertThat(result.getOrNull()?.map { it.domain }).containsExactly(
            "blocked.example.com",
            "blocked.test.com"
        )
    }

    @Test
    fun `fetchLogs - with V5 and query filter - returns entries containing query string`() =
        runTest {
            // Given
            val activeConnection = mockk<ConnectionEntity.Version5>(relaxed = true)
            val responseData = PiHoleLogsResponseDataV5(
                data = listOf(
                    PiHoleLogsResponseDataV5.PiHoleLogEntryData(
                        timestamp = 1000L,
                        queryType = "A",
                        requestedDomain = "blocked.example.com",
                        client = "1.2.3.4",
                        answerType = LogsAnswerType.EXACT_BLOCK,
                        responseTime = 0
                    ),
                    PiHoleLogsResponseDataV5.PiHoleLogEntryData(
                        timestamp = 2000L,
                        queryType = "A",
                        requestedDomain = "allowed.example.com",
                        client = "1.2.3.4",
                        answerType = LogsAnswerType.UPSTREAM,
                        responseTime = 0
                    ),
                    PiHoleLogsResponseDataV5.PiHoleLogEntryData(
                        timestamp = 3000L,
                        queryType = "A",
                        requestedDomain = "blocked.test.com",
                        client = "1.2.3.4",
                        answerType = LogsAnswerType.EXACT_BLOCK,
                        responseTime = 0
                    )
                )
            )
            coEvery { apiServiceV5.fetchLogs(any(), any()) } returns Result.success(responseData)

            // When
            val result = target.fetchLogs(
                connection = activeConnection,
                limit = 10,
                status = LogEntryStatus.ALL,
                query = "example",
                clientIp = null,
                clientName = null,
                queryType = null,
                advancedStatus = null,
                from = null,
                until = null
            )

            // Then
            assertThat(result.getOrNull()?.map { it.domain }).containsExactly(
                "blocked.example.com",
                "allowed.example.com"
            )
        }

    @Test
    fun `fetchLogs - with V5 and date range filter - returns entries within time range`() =
        runTest {
            // Given
            val activeConnection = mockk<ConnectionEntity.Version5>(relaxed = true)
            val responseData = PiHoleLogsResponseDataV5(
                data = listOf(
                    PiHoleLogsResponseDataV5.PiHoleLogEntryData(
                        timestamp = 1000L,
                        queryType = "A",
                        requestedDomain = "blocked.example.com",
                        client = "1.2.3.4",
                        answerType = LogsAnswerType.EXACT_BLOCK,
                        responseTime = 0
                    ),
                    PiHoleLogsResponseDataV5.PiHoleLogEntryData(
                        timestamp = 2000L,
                        queryType = "A",
                        requestedDomain = "allowed.example.com",
                        client = "1.2.3.4",
                        answerType = LogsAnswerType.UPSTREAM,
                        responseTime = 0
                    ),
                    PiHoleLogsResponseDataV5.PiHoleLogEntryData(
                        timestamp = 3000L,
                        queryType = "A",
                        requestedDomain = "blocked.test.com",
                        client = "1.2.3.4",
                        answerType = LogsAnswerType.EXACT_BLOCK,
                        responseTime = 0
                    )
                )
            )
            coEvery { apiServiceV5.fetchLogs(any(), any()) } returns Result.success(responseData)

            // When
            val result = target.fetchLogs(
                connection = activeConnection,
                limit = 10,
                status = LogEntryStatus.ALL,
                query = "",
                clientIp = null,
                clientName = null,
                queryType = null,
                advancedStatus = null,
                from = 1500L,
                until = 2500L
            )

            // Then
            assertThat(result.getOrNull()?.map { it.domain }).containsExactly("allowed.example.com")
        }

    @Test
    fun `fetchLogs - with V6 and status filter returns only blocked entries`() = runTest {
        // Given
        val activeConnection = mockk<ConnectionEntity.Version6>(relaxed = true)
        val responseData = PiHoleLogsResponseDataV6(
            queries = listOf(
                PiHoleLogsResponseDataV6.PiHoleLogEntryData(
                    id = 1,
                    time = 0.1,
                    type = PiHoleLogEntryTypeV6.A,
                    status = PiHoleLogEntryStatusV6.GRAVITY,
                    dnssec = PiHoleLogEntryDnssecV6.UNKNOWN,
                    domain = "blocked.example.com",
                    upstream = null,
                    reply = PiHoleLogsResponseDataV6.PiHoleLogEntryData.PiHoleLogEntryReply(
                        type = PiHoleLogEntryReplyTypeV6.IP,
                        time = 0.1
                    ),
                    client = PiHoleLogsResponseDataV6.PiHoleLogEntryData.PiHoleLogEntryClient(
                        ip = "1.2.3.4",
                        name = null
                    ),
                    listId = null,
                    ede = PiHoleLogsResponseDataV6.PiHoleLogEntryData.PiHoleLogEntryEde(0, null),
                    cname = null
                ),
                PiHoleLogsResponseDataV6.PiHoleLogEntryData(
                    id = 2,
                    time = 0.1,
                    type = PiHoleLogEntryTypeV6.A,
                    status = PiHoleLogEntryStatusV6.FORWARDED,
                    dnssec = PiHoleLogEntryDnssecV6.UNKNOWN,
                    domain = "allowed.example.com",
                    upstream = "8.8.8.8",
                    reply = PiHoleLogsResponseDataV6.PiHoleLogEntryData.PiHoleLogEntryReply(
                        type = PiHoleLogEntryReplyTypeV6.IP,
                        time = 0.1
                    ),
                    client = PiHoleLogsResponseDataV6.PiHoleLogEntryData.PiHoleLogEntryClient(
                        ip = "1.2.3.4",
                        name = null
                    ),
                    listId = null,
                    ede = PiHoleLogsResponseDataV6.PiHoleLogEntryData.PiHoleLogEntryEde(0, null),
                    cname = null
                )
            ),
            cursor = 0,
            recordsTotal = 2,
            recordsFiltered = 2,
            draw = 1,
            took = 0.1
        )
        coEvery {
            apiServiceV6.fetchLogs(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Result.success(
            responseData
        )

        // When
        val result = target.fetchLogs(
            connection = activeConnection,
            limit = 10,
            status = LogEntryStatus.BLOCKED,
            query = "",
            clientIp = null,
            clientName = null,
            queryType = null,
            advancedStatus = null,
            from = null,
            until = null
        )

        // Then
        assertThat(result.getOrNull()?.map { it.domain }).containsExactly("blocked.example.com")
    }

    @Test
    fun `fetchLogs - with V6 and blank query - returns all entries`() = runTest {
        // Given
        val activeConnection = mockk<ConnectionEntity.Version6>(relaxed = true)
        val responseData = PiHoleLogsResponseDataV6(
            queries = listOf(
                PiHoleLogsResponseDataV6.PiHoleLogEntryData(
                    id = 1,
                    time = 0.1,
                    type = PiHoleLogEntryTypeV6.A,
                    status = PiHoleLogEntryStatusV6.GRAVITY,
                    dnssec = PiHoleLogEntryDnssecV6.UNKNOWN,
                    domain = "blocked.example.com",
                    upstream = null,
                    reply = PiHoleLogsResponseDataV6.PiHoleLogEntryData.PiHoleLogEntryReply(
                        type = PiHoleLogEntryReplyTypeV6.IP,
                        time = 0.1
                    ),
                    client = PiHoleLogsResponseDataV6.PiHoleLogEntryData.PiHoleLogEntryClient(
                        ip = "1.2.3.4",
                        name = null
                    ),
                    listId = null,
                    ede = PiHoleLogsResponseDataV6.PiHoleLogEntryData.PiHoleLogEntryEde(0, null),
                    cname = null
                ),
                PiHoleLogsResponseDataV6.PiHoleLogEntryData(
                    id = 2,
                    time = 0.1,
                    type = PiHoleLogEntryTypeV6.A,
                    status = PiHoleLogEntryStatusV6.FORWARDED,
                    dnssec = PiHoleLogEntryDnssecV6.UNKNOWN,
                    domain = "allowed.example.com",
                    upstream = "8.8.8.8",
                    reply = PiHoleLogsResponseDataV6.PiHoleLogEntryData.PiHoleLogEntryReply(
                        type = PiHoleLogEntryReplyTypeV6.IP,
                        time = 0.1
                    ),
                    client = PiHoleLogsResponseDataV6.PiHoleLogEntryData.PiHoleLogEntryClient(
                        ip = "1.2.3.4",
                        name = null
                    ),
                    listId = null,
                    ede = PiHoleLogsResponseDataV6.PiHoleLogEntryData.PiHoleLogEntryEde(0, null),
                    cname = null
                )
            ),
            cursor = 0,
            recordsTotal = 2,
            recordsFiltered = 2,
            draw = 1,
            took = 0.1
        )
        coEvery {
            apiServiceV6.fetchLogs(
                connection = any(),
                limit = any(),
                domain = any(),
                clientIp = any(),
                clientName = any(),
                queryType = any(),
                advancedStatus = any(),
                from = any(),
                until = any()
            )
        } returns Result.success(
            responseData
        )

        // When
        val result = target.fetchLogs(
            connection = activeConnection,
            limit = 10,
            status = LogEntryStatus.ALL,
            query = "   ",
            clientIp = null,
            clientName = null,
            queryType = null,
            advancedStatus = null,
            from = null,
            until = null
        )

        // Then
        assertThat(result.getOrNull()?.map { it.domain }).containsExactly(
            "blocked.example.com",
            "allowed.example.com"
        )
    }

    @Test
    fun `fetchLogFilterSuggestions - with V5 connection returns success`() = runTest {
        // Given
        val activeConnection = mockk<ConnectionEntity.Version5>(relaxed = true)
        val responseData = mockk<PiHoleLogSuggestionsResponseDataV5>(relaxed = true)
        coEvery { apiServiceV5.fetchLogFilterSuggestions(activeConnection) } returns Result.success(
            responseData
        )

        // When
        val result = target.fetchLogFilterSuggestions(activeConnection)

        // Then
        assertThat(result.isSuccess).isTrue()
        coVerify { apiServiceV5.fetchLogFilterSuggestions(activeConnection) }
    }

    @Test
    fun `fetchLogFilterSuggestions - with V6 connection returns success`() = runTest {
        // Given
        val activeConnection = mockk<ConnectionEntity.Version6>(relaxed = true)
        val responseData = mockk<PiHoleLogSuggestionsResponseDataV6>(relaxed = true)
        coEvery { apiServiceV6.fetchLogFilterSuggestions(activeConnection) } returns Result.success(
            responseData
        )

        // When
        val result = target.fetchLogFilterSuggestions(activeConnection)

        // Then
        assertThat(result.isSuccess).isTrue()
        coVerify { apiServiceV6.fetchLogFilterSuggestions(activeConnection) }
    }
}
