package eu.wedgess.piholecontrol.data.repository

import TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.v5.DashboardApiServiceV5
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.QueriesOverTimeEntity
import eu.wedgess.piholecontrol.domain.model.SummaryEntity
import eu.wedgess.piholecontrol.domain.repository.DashboardRepository
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DashboardRepositoryImplTest {

    @MockK
    private lateinit var api: DashboardApiServiceV5
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var target: DashboardRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dispatcherProvider = TestDispatcherProvider()
        target = DashboardRepositoryImpl(api, dispatcherProvider)
    }

    @Test
    fun `fetchStatusSummary - api fetchStatusSummary is invoked AND result is success`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        coEvery { api.fetchStatusSummary(activeConnection) } returns Result.success(
            mockk(relaxed = true)
        )

        val result = target.fetchStatusSummary(activeConnection)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isNotNull()
        assertThat(result.getOrNull()).isInstanceOf(SummaryEntity::class.java)
        coVerify { api.fetchStatusSummary(activeConnection) }
    }

    @Test
    fun `fetchStatusSummary - should return failure on API error`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        val exception = RuntimeException("API error")
        coEvery { api.fetchStatusSummary(activeConnection) } returns Result.failure(exception)

        val result = target.fetchStatusSummary(activeConnection)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { api.fetchStatusSummary(activeConnection) }
    }

    @Test
    fun `fetchOverTimeDataClients - api fetchOverTimeDataClients is invoked AND result is success`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity>(relaxed = true)
            coEvery { api.fetchOverTimeDataClients(activeConnection) } returns Result.success(
                mockk(relaxed = true)
            )

            val result = target.fetchOverTimeDataClients(activeConnection)

            assertThat(result.isSuccess).isTrue()
            assertThat(result.getOrNull()).isNotNull()
            assertThat(result.getOrNull()).isInstanceOf(List::class.java)
            coVerify { api.fetchOverTimeDataClients(activeConnection) }
        }

    @Test
    fun `fetchOverTimeDataClients - should return failure on API error`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        val exception = RuntimeException("API error")
        coEvery { api.fetchOverTimeDataClients(activeConnection) } returns Result.failure(exception)

        val result = target.fetchOverTimeDataClients(activeConnection)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { api.fetchOverTimeDataClients(activeConnection) }
    }

    @Test
    fun `fetchOverTimeData10Minutes - api fetchOverTimeData10Minutes is invoked AND result is success`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity>(relaxed = true)
            coEvery { api.fetchOverTimeData10Minutes(activeConnection) } returns Result.success(
                mockk(relaxed = true)
            )

            val result = target.fetchOverTimeData10Minutes(activeConnection)

            assertThat(result.isSuccess).isTrue()
            assertThat(result.getOrNull()).isNotNull()
            assertThat(result.getOrNull()).isInstanceOf(QueriesOverTimeEntity::class.java)
            coVerify { api.fetchOverTimeData10Minutes(activeConnection) }
        }

    @Test
    fun `fetchOverTimeData10Minutes - should return failure on API error`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        val exception = RuntimeException("API error")
        coEvery { api.fetchOverTimeData10Minutes(activeConnection) } returns Result.failure(
            exception
        )

        val result = target.fetchOverTimeData10Minutes(activeConnection)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { api.fetchOverTimeData10Minutes(activeConnection) }
    }
}
