package eu.wedgess.piholecontrol.domain.usecase.dashboard

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ClientOverTimeEntity
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.QueriesOverTimeEntity
import eu.wedgess.piholecontrol.domain.model.SummaryEntity
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.domain.usecases.dashboard.FetchClientsOverallTimeDataUseCase
import eu.wedgess.piholecontrol.domain.usecases.dashboard.FetchDashboardInfoUseCase
import eu.wedgess.piholecontrol.domain.usecases.dashboard.FetchOverallTimeDataUseCase
import eu.wedgess.piholecontrol.domain.usecases.dashboard.FetchStatusSummaryUseCase
import eu.wedgess.piholecontrol.presentation.dashboard.model.DashboardInfo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FetchDashboardInfoUseCaseTest {

    @MockK
    private lateinit var fetchStatusSummaryUseCase: FetchStatusSummaryUseCase

    @MockK
    private lateinit var fetchOverallTimeDataUseCase: FetchOverallTimeDataUseCase

    @MockK
    private lateinit var fetchClientsOverallTimeDataUseCase: FetchClientsOverallTimeDataUseCase

    @MockK
    private lateinit var periodicRefreshUseCase: PeriodicRefreshUseCase

    private lateinit var target: FetchDashboardInfoUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = FetchDashboardInfoUseCase(
            fetchStatusSummaryUseCase,
            fetchOverallTimeDataUseCase,
            fetchClientsOverallTimeDataUseCase,
            periodicRefreshUseCase
        )
    }

    @Test
    fun `invoke - successfully fetches dashboard info`() = runTest {
        val connection = ConnectionEntity.default
        val summary = Result.success(mockk<SummaryEntity>(relaxed = true))
        val overTimeData = Result.success(mockk<QueriesOverTimeEntity>(relaxed = true))
        val clientsOverTimeData =
            Result.success(listOf(mockk<ClientOverTimeEntity>(relaxed = true)))

        coEvery { fetchStatusSummaryUseCase(connection) } returns summary
        coEvery { fetchOverallTimeDataUseCase(connection) } returns overTimeData
        coEvery { fetchClientsOverallTimeDataUseCase(connection) } returns clientsOverTimeData
        coEvery { periodicRefreshUseCase<Result<DashboardInfo>>(any()) } answers {
            flow {
                val fetchBlock = arg<suspend (ConnectionEntity) -> Result<DashboardInfo>>(0)
                emit(fetchBlock(connection))
            }
        }

        val result = target().toList()

        assertThat(result).hasSize(1)
        assertThat(result.first().isSuccess).isTrue()
        val dashboardInfo = result.first().getOrNull()
        assertThat(dashboardInfo?.summaryResult).isEqualTo(summary)
        assertThat(dashboardInfo?.queriesOverTimeResult).isEqualTo(overTimeData)
        assertThat(dashboardInfo?.clientQueriesOverTimeResult).isEqualTo(clientsOverTimeData)
    }

    @Test
    fun `invoke - returns success when one of the results fails`() = runTest {
        val connection = ConnectionEntity.default
        val summary = Result.success(mockk<SummaryEntity>(relaxed = true))
        val overTimeData =
            Result.failure<QueriesOverTimeEntity>(Exception("Failed to fetch overtime data"))
        val clientsOverTimeData =
            Result.success(listOf(mockk<ClientOverTimeEntity>(relaxed = true)))

        coEvery { fetchStatusSummaryUseCase(connection) } returns summary
        coEvery { fetchOverallTimeDataUseCase(connection) } returns overTimeData
        coEvery { fetchClientsOverallTimeDataUseCase(connection) } returns clientsOverTimeData
        coEvery { periodicRefreshUseCase<Result<DashboardInfo>>(any()) } answers {
            flow {
                val fetchBlock = arg<suspend (ConnectionEntity) -> Result<DashboardInfo>>(0)
                emit(fetchBlock(connection))
            }
        }

        val result = target().toList()

        assertThat(result).hasSize(1)
        assertThat(result.first().isSuccess).isTrue()
    }
}
