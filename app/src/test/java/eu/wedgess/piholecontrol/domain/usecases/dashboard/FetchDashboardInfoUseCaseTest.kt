package eu.wedgess.piholecontrol.domain.usecases.dashboard

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ClientOverTimeEntity
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.QueriesOverTimeEntity
import eu.wedgess.piholecontrol.domain.model.SummaryEntity
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.presentation.dashboard.model.DashboardInfo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
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
        val summary = Result.success(
            SummaryEntity(
                dnsQueries = 1234,
                domainsBlocked = 1232,
                adsPercentage = 30f,
                adsBlocked = 623,
                uniqueClients = 21
            )
        )
        val overTimeData =
            Result.success(QueriesOverTimeEntity(permitted = emptyList(), blocked = emptyList()))
        val clientsOverTimeData =
            Result.success(
                listOf(
                    ClientOverTimeEntity(
                        clientName = "client",
                        clientIp = "192.168.1.1",
                        clientActivity = emptyList()
                    )
                )
            )

        coEvery { fetchStatusSummaryUseCase(connection) } returns summary
        coEvery { fetchOverallTimeDataUseCase(connection) } returns overTimeData
        coEvery { fetchClientsOverallTimeDataUseCase(connection) } returns clientsOverTimeData
        coEvery { periodicRefreshUseCase<DashboardInfo>(any()) } answers {
            flow {
                val fetchBlock = arg<suspend (ConnectionEntity) -> Result<DashboardInfo>>(0)
                emit(fetchBlock(connection))
            }
        }

        target().test {
            val result = awaitItem()
            assertThat(result.isSuccess).isTrue()
            val dashboardInfo = result.getOrNull()!!
            assertThat(dashboardInfo.summaryResult).isEqualTo(summary)
            assertThat(dashboardInfo.queriesOverTimeResult).isEqualTo(overTimeData)
            assertThat(dashboardInfo.clientQueriesOverTimeResult).isEqualTo(clientsOverTimeData)
            awaitComplete()
        }
    }

    @Test
    fun `invoke - returns success when one of the results fails`() = runTest {
        val connection = ConnectionEntity.default
        val summary = Result.success(
            SummaryEntity(
                dnsQueries = 1234,
                domainsBlocked = 1232,
                adsPercentage = 30f,
                adsBlocked = 623,
                uniqueClients = 21
            )
        )
        val overTimeData =
            Result.failure<QueriesOverTimeEntity>(Exception("Failed to fetch overtime data"))
        val clientsOverTimeData =
            Result.success(
                listOf(
                    ClientOverTimeEntity(
                        clientName = "client",
                        clientIp = "192.168.1.1",
                        clientActivity = emptyList()
                    )
                )
            )

        coEvery { fetchStatusSummaryUseCase(connection) } returns summary
        coEvery { fetchOverallTimeDataUseCase(connection) } returns overTimeData
        coEvery { fetchClientsOverallTimeDataUseCase(connection) } returns clientsOverTimeData
        coEvery { periodicRefreshUseCase<DashboardInfo>(any()) } answers {
            flow {
                val fetchBlock = arg<suspend (ConnectionEntity) -> Result<DashboardInfo>>(0)
                emit(fetchBlock(connection))
            }
        }

        target().test {
            val result = awaitItem()
            assertThat(result.isSuccess).isTrue()
            awaitComplete()
        }
    }
}
