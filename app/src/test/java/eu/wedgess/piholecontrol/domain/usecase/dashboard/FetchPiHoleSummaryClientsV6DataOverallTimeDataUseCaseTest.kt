package eu.wedgess.piholecontrol.domain.usecase.dashboard

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ClientOverTimeEntity
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.DashboardRepository
import eu.wedgess.piholecontrol.domain.usecases.dashboard.FetchClientsOverallTimeDataUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FetchPiHoleSummaryClientsV6DataOverallTimeDataUseCaseTest {

    @MockK(relaxed = true)
    private lateinit var dashboardRepository: DashboardRepository

    private lateinit var target: FetchClientsOverallTimeDataUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = FetchClientsOverallTimeDataUseCase(dashboardRepository)
    }

    @Test
    fun `invoke - successfully fetches clients overall time data`() = runTest {
        val connection = ConnectionEntity.default
        val clientData = listOf(
            ClientOverTimeEntity(
                clientName = "client",
                clientIp = "192.168.1.1",
                clientActivity = emptyList()
            ),
            ClientOverTimeEntity(
                clientName = "client2",
                clientIp = "192.168.1.2",
                clientActivity = emptyList()
            )
        )
        coEvery { dashboardRepository.fetchOverTimeDataClients(connection) } returns Result.success(
            clientData
        )

        val result = target(connection)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(clientData)
        coVerify { dashboardRepository.fetchOverTimeDataClients(connection) }
    }

    @Test
    fun `invoke - returns failure when fetching clients overall time data fails`() = runTest {
        val connection = ConnectionEntity.default
        val exception = Exception("Failed to fetch clients overall time data")
        coEvery { dashboardRepository.fetchOverTimeDataClients(connection) } returns Result.failure(
            exception
        )

        val result = target(connection)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { dashboardRepository.fetchOverTimeDataClients(connection) }
    }
}
