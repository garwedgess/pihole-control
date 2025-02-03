package eu.wedgess.piholecontrol.domain.usecases.dashboard

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.QueriesOverTimeEntity
import eu.wedgess.piholecontrol.domain.repository.DashboardRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FetchOverallTimeDataUseCaseTest {

    @MockK(relaxed = true)
    private lateinit var dashboardRepository: DashboardRepository

    private lateinit var target: FetchOverallTimeDataUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = FetchOverallTimeDataUseCase(dashboardRepository)
    }

    @Test
    fun `invoke - successfully fetches overall time data`() = runTest {
        val connection = ConnectionEntity.Version5.default
        val queriesOverTime =
            QueriesOverTimeEntity(permitted = emptyList(), blocked = emptyList())
        coEvery { dashboardRepository.fetchOverTimeData10Minutes(connection) } returns Result.success(
            queriesOverTime
        )

        val result = target(connection)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(queriesOverTime)
        coVerify { dashboardRepository.fetchOverTimeData10Minutes(connection) }
    }

    @Test
    fun `invoke - returns failure when fetching overall time data fails`() = runTest {
        val connection = ConnectionEntity.Version5.default
        val exception = Exception("Failed to fetch overall time data")
        coEvery { dashboardRepository.fetchOverTimeData10Minutes(connection) } returns Result.failure(
            exception
        )

        val result = target(connection)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { dashboardRepository.fetchOverTimeData10Minutes(connection) }
    }
}
