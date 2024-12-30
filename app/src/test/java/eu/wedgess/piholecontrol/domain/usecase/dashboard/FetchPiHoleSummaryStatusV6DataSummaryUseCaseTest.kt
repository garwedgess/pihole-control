package eu.wedgess.piholecontrol.domain.usecase.dashboard

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.SummaryEntity
import eu.wedgess.piholecontrol.domain.repository.DashboardRepository
import eu.wedgess.piholecontrol.domain.usecases.dashboard.FetchStatusSummaryUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FetchPiHoleSummaryStatusV6DataSummaryUseCaseTest {

    @MockK(relaxed = true)
    private lateinit var dashboardRepository: DashboardRepository

    private lateinit var target: FetchStatusSummaryUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = FetchStatusSummaryUseCase(dashboardRepository)
    }

    @Test
    fun `invoke - successfully fetches status summary`() = runTest {
        val connection = ConnectionEntity.default
        val summary = SummaryEntity(
            dnsQueries = 1234,
            domainsBlocked = 1232,
            adsPercentage = 30f,
            adsBlocked = 623,
            uniqueClients = 21
        )
        coEvery { dashboardRepository.fetchStatusSummary(connection) } returns Result.success(
            summary
        )

        val result = target(connection)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(summary)
        coVerify { dashboardRepository.fetchStatusSummary(connection) }
    }

    @Test
    fun `invoke - returns failure when fetching status summary fails`() = runTest {
        val connection = ConnectionEntity.default
        val exception = Exception("Failed to fetch status summary")

        coEvery { dashboardRepository.fetchStatusSummary(connection) } returns Result.failure(
            exception
        )

        val result = target(connection)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { dashboardRepository.fetchStatusSummary(connection) }
    }
}
