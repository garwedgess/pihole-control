package eu.wedgess.piholecontrol.data.repository

import TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.DashboardApiService
import eu.wedgess.piholecontrol.data.model.responses.PiHoleClientsOverTimeResponseData
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.QueriesOverTimeEntity
import eu.wedgess.piholecontrol.domain.model.SummaryEntity
import eu.wedgess.piholecontrol.domain.repository.DashboardRepository
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DashboardRepositoryImplTest {

    @MockK
    private lateinit var apiServiceV6: DashboardApiService
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var target: DashboardRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dispatcherProvider = TestDispatcherProvider()
        target = DashboardRepositoryImpl(apiServiceV6, dispatcherProvider)
    }

    @Test
    fun `fetchStatusSummary with connection - should invoke API and return success`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity>(relaxed = true)
            coEvery { apiServiceV6.fetchStatusSummary(activeConnection) } returns Result.success(
                mockk(relaxed = true)
            )

            val result = target.fetchStatusSummary(activeConnection)

            assertThat(result.isSuccess).isTrue()
            assertThat(result.getOrNull()).isNotNull()
            assertThat(result.getOrNull()).isInstanceOf(SummaryEntity::class.java)
            coVerify { apiServiceV6.fetchStatusSummary(activeConnection) }
        }

    @Test
    fun `fetchStatusSummary with connection - should invoke API and return failure on API error`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity>(relaxed = true)
            val exception = RuntimeException("API error")
            coEvery { apiServiceV6.fetchStatusSummary(activeConnection) } returns Result.failure(
                exception
            )

            val result = target.fetchStatusSummary(activeConnection)

            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isEqualTo(exception)
            coVerify { apiServiceV6.fetchStatusSummary(activeConnection) }
        }

    @Test
    fun `fetchOverTimeDataClients with connection - should invoke API and return success`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity>(relaxed = true)
            coEvery { apiServiceV6.fetchOverTimeDataClients(activeConnection) } returns Result.success(
                mockk(relaxed = true)
            )

            val result = target.fetchOverTimeDataClients(activeConnection)

            assertThat(result.isSuccess).isTrue()
            assertThat(result.getOrNull()).isNotNull()
            assertThat(result.getOrNull()).isInstanceOf(List::class.java)
            coVerify { apiServiceV6.fetchOverTimeDataClients(activeConnection) }
        }

    @Test
    fun `fetchOverTimeDataClients with connection - should invoke API and return failure on API error`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity>(relaxed = true)
            val exception = RuntimeException("API error")
            coEvery { apiServiceV6.fetchOverTimeDataClients(activeConnection) } returns Result.failure(
                exception
            )

            val result = target.fetchOverTimeDataClients(activeConnection)

            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isEqualTo(exception)
            coVerify { apiServiceV6.fetchOverTimeDataClients(activeConnection) }
        }

    @Test
    fun `fetchOverTimeData10Minutes with connection - should invoke API and return success`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity>(relaxed = true)
            coEvery { apiServiceV6.fetchOverTimeData10Minutes(activeConnection) } returns Result.success(
                mockk(relaxed = true)
            )

            val result = target.fetchOverTimeData10Minutes(activeConnection)

            assertThat(result.isSuccess).isTrue()
            assertThat(result.getOrNull()).isNotNull()
            assertThat(result.getOrNull()).isInstanceOf(QueriesOverTimeEntity::class.java)
            coVerify { apiServiceV6.fetchOverTimeData10Minutes(activeConnection) }
        }

    @Test
    fun `fetchOverTimeData10Minutes with connection - should invoke API and return failure on API error`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity>(relaxed = true)
            val exception = RuntimeException("API error")
            coEvery { apiServiceV6.fetchOverTimeData10Minutes(activeConnection) } returns Result.failure(
                exception
            )

            val result = target.fetchOverTimeData10Minutes(activeConnection)

            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isEqualTo(exception)
            coVerify { apiServiceV6.fetchOverTimeData10Minutes(activeConnection) }
        }

    @Test
    fun `mapV6ClientsOverTime - should correctly map and sort client data`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        val responseData = PiHoleClientsOverTimeResponseData(
            clients = mapOf(
                "192.168.1.1" to PiHoleClientsOverTimeResponseData.PiHoleClientsOverTimeClientData(
                    name = "Client1",
                    total = 100
                ),
                "192.168.1.2" to PiHoleClientsOverTimeResponseData.PiHoleClientsOverTimeClientData(
                    name = "Client2",
                    total = 80
                )
            ),
            history = listOf(
                PiHoleClientsOverTimeResponseData.PiHoleClientsOverTimeHistoryData(
                    timestamp = 1000,
                    data = mapOf("192.168.1.1" to 20, "192.168.1.2" to 30)
                ),
                PiHoleClientsOverTimeResponseData.PiHoleClientsOverTimeHistoryData(
                    timestamp = 3000,
                    data = mapOf("192.168.1.1" to 80, "192.168.1.2" to 50)
                )
            )
        )
        coEvery { apiServiceV6.fetchOverTimeDataClients(activeConnection) } returns Result.success(
            responseData
        )

        val result = target.fetchOverTimeDataClients(activeConnection)

        assertThat(result.isSuccess).isTrue()
        val clients = result.getOrNull()!!
        assertThat(clients).hasSize(2)
        assertThat(clients[0].clientName).isEqualTo("Client1")
        assertThat(clients[0].clientActivity).hasSize(2)
        assertThat(clients[0].clientActivity.sumOf { it.hits }).isEqualTo(100)
        assertThat(clients[1].clientName).isEqualTo("Client2")
        assertThat(clients[1].clientActivity).hasSize(2)
        assertThat(clients[1].clientActivity.sumOf { it.hits }).isEqualTo(80)
    }

    @Test
    fun `mapV6ClientsOverTime - should handle null client name and missing data`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        val responseData = mockk<PiHoleClientsOverTimeResponseData> {
            every { clients } returns mapOf(
                "192.168.1.1" to mockk {
                    every { name } returns null
                }
            )
            every { history } returns listOf(
                mockk {
                    every { timestamp } returns null
                    every { data } returns emptyMap()
                }
            )
        }
        coEvery { apiServiceV6.fetchOverTimeDataClients(activeConnection) } returns Result.success(
            responseData
        )

        val result = target.fetchOverTimeDataClients(activeConnection)

        assertThat(result.isSuccess).isTrue()
        val clients = result.getOrNull()!!
        assertThat(clients).hasSize(1)
        assertThat(clients[0].clientName).isEmpty()
        assertThat(clients[0].clientActivity).hasSize(1)
        assertThat(clients[0].clientActivity[0].timestamp).isEqualTo(0)
        assertThat(clients[0].clientActivity[0].hits).isEqualTo(0)
    }
}
