package eu.wedgess.piholecontrol.data.repository

import TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.LogsApiService
import eu.wedgess.piholecontrol.data.model.responses.PiHoleLogEntryDnssec
import eu.wedgess.piholecontrol.data.model.responses.PiHoleLogEntryReplyType
import eu.wedgess.piholecontrol.data.model.responses.PiHoleLogEntryStatus
import eu.wedgess.piholecontrol.data.model.responses.PiHoleLogEntryType
import eu.wedgess.piholecontrol.data.model.responses.PiHoleLogSuggestionsResponseData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleLogsResponseData
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
    private lateinit var apiServiceV6: LogsApiService
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var target: LogsRepository

    @Before
    fun setUp() {
        initThreeTen()
        MockKAnnotations.init(this)
        dispatcherProvider = TestDispatcherProvider()
        target = LogsRepositoryImpl(apiServiceV6, dispatcherProvider)
    }

    @Test
    fun `fetchLogs - with connection - invokes API and returns success`() = runTest {
        // Given
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        val limit = 10
        val responseData = PiHoleLogsResponseData(
            queries = listOf(
                PiHoleLogsResponseData.PiHoleLogEntryData(
                    id = 1,
                    time = 0.1,
                    type = PiHoleLogEntryType.A,
                    status = PiHoleLogEntryStatus.GRAVITY,
                    dnssec = PiHoleLogEntryDnssec.UNKNOWN,
                    domain = "example.com",
                    upstream = null,
                    reply = PiHoleLogsResponseData.PiHoleLogEntryData.PiHoleLogEntryReply(
                        type = PiHoleLogEntryReplyType.IP,
                        time = 0.1
                    ),
                    client = PiHoleLogsResponseData.PiHoleLogEntryData.PiHoleLogEntryClient(
                        ip = "1.2.3.4",
                        name = null
                    ),
                    listId = null,
                    ede = PiHoleLogsResponseData.PiHoleLogEntryData.PiHoleLogEntryEde(0, null),
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
    }

    @Test
    fun `fetchLogs - with and status filter returns only blocked entries`() = runTest {
        // Given
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        val responseData = PiHoleLogsResponseData(
            queries = listOf(
                PiHoleLogsResponseData.PiHoleLogEntryData(
                    id = 1,
                    time = 0.1,
                    type = PiHoleLogEntryType.A,
                    status = PiHoleLogEntryStatus.GRAVITY,
                    dnssec = PiHoleLogEntryDnssec.UNKNOWN,
                    domain = "blocked.example.com",
                    upstream = null,
                    reply = PiHoleLogsResponseData.PiHoleLogEntryData.PiHoleLogEntryReply(
                        type = PiHoleLogEntryReplyType.IP,
                        time = 0.1
                    ),
                    client = PiHoleLogsResponseData.PiHoleLogEntryData.PiHoleLogEntryClient(
                        ip = "1.2.3.4",
                        name = null
                    ),
                    listId = null,
                    ede = PiHoleLogsResponseData.PiHoleLogEntryData.PiHoleLogEntryEde(0, null),
                    cname = null
                ),
                PiHoleLogsResponseData.PiHoleLogEntryData(
                    id = 2,
                    time = 0.1,
                    type = PiHoleLogEntryType.A,
                    status = PiHoleLogEntryStatus.FORWARDED,
                    dnssec = PiHoleLogEntryDnssec.UNKNOWN,
                    domain = "allowed.example.com",
                    upstream = "8.8.8.8",
                    reply = PiHoleLogsResponseData.PiHoleLogEntryData.PiHoleLogEntryReply(
                        type = PiHoleLogEntryReplyType.IP,
                        time = 0.1
                    ),
                    client = PiHoleLogsResponseData.PiHoleLogEntryData.PiHoleLogEntryClient(
                        ip = "1.2.3.4",
                        name = null
                    ),
                    listId = null,
                    ede = PiHoleLogsResponseData.PiHoleLogEntryData.PiHoleLogEntryEde(0, null),
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
    fun `fetchLogs - with and blank query - returns all entries`() = runTest {
        // Given
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        val responseData = PiHoleLogsResponseData(
            queries = listOf(
                PiHoleLogsResponseData.PiHoleLogEntryData(
                    id = 1,
                    time = 0.1,
                    type = PiHoleLogEntryType.A,
                    status = PiHoleLogEntryStatus.GRAVITY,
                    dnssec = PiHoleLogEntryDnssec.UNKNOWN,
                    domain = "blocked.example.com",
                    upstream = null,
                    reply = PiHoleLogsResponseData.PiHoleLogEntryData.PiHoleLogEntryReply(
                        type = PiHoleLogEntryReplyType.IP,
                        time = 0.1
                    ),
                    client = PiHoleLogsResponseData.PiHoleLogEntryData.PiHoleLogEntryClient(
                        ip = "1.2.3.4",
                        name = null
                    ),
                    listId = null,
                    ede = PiHoleLogsResponseData.PiHoleLogEntryData.PiHoleLogEntryEde(0, null),
                    cname = null
                ),
                PiHoleLogsResponseData.PiHoleLogEntryData(
                    id = 2,
                    time = 0.1,
                    type = PiHoleLogEntryType.A,
                    status = PiHoleLogEntryStatus.FORWARDED,
                    dnssec = PiHoleLogEntryDnssec.UNKNOWN,
                    domain = "allowed.example.com",
                    upstream = "8.8.8.8",
                    reply = PiHoleLogsResponseData.PiHoleLogEntryData.PiHoleLogEntryReply(
                        type = PiHoleLogEntryReplyType.IP,
                        time = 0.1
                    ),
                    client = PiHoleLogsResponseData.PiHoleLogEntryData.PiHoleLogEntryClient(
                        ip = "1.2.3.4",
                        name = null
                    ),
                    listId = null,
                    ede = PiHoleLogsResponseData.PiHoleLogEntryData.PiHoleLogEntryEde(0, null),
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
    fun `fetchLogFilterSuggestions - with connection returns success`() = runTest {
        // Given
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        val responseData = mockk<PiHoleLogSuggestionsResponseData>(relaxed = true)
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
