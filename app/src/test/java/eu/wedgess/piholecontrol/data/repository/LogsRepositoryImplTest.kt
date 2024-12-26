package eu.wedgess.piholecontrol.data.repository

import TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.LogsApiService
import eu.wedgess.piholecontrol.data.model.responses.PiHoleLogsResponse
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.LogsRepository
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
    private lateinit var apiService: LogsApiService
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var target: LogsRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dispatcherProvider = TestDispatcherProvider()
        target = LogsRepositoryImpl(apiService, dispatcherProvider)
    }

    @Test
    fun `fetchLogs - api fetchLogs is invoked AND result is success`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        val limit = 10
        val mockResponse = mockk<PiHoleLogsResponse>(relaxed = true)
        coEvery { apiService.fetchLogs(activeConnection, limit) } returns Result.success(
            mockResponse
        )

        val result = target.fetchLogs(activeConnection, limit)

        assertThat(result.isSuccess).isTrue()
        val resultList = result.getOrNull()
        assertThat(resultList).isNotNull()
        assertThat(resultList).isInstanceOf(List::class.java)
        coVerify { apiService.fetchLogs(activeConnection, limit) }
    }

    @Test
    fun `fetchLogs - api fetchLogs is invoked AND result is failure`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        val limit = 10
        val exception = RuntimeException("API error")
        coEvery { apiService.fetchLogs(activeConnection, limit) } returns Result.failure(
            exception
        )

        val result = target.fetchLogs(activeConnection, limit)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { apiService.fetchLogs(activeConnection, limit) }
    }
}
