package eu.wedgess.piholecontrol.domain.usecases.statistics

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.UpstreamDestinationEntity
import eu.wedgess.piholecontrol.domain.repository.StatisticsRepository
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.presentation.statistics.common.components.donutchart.model.UpstreamDestinationsChartData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FetchUpstreamDestinationsUseCaseTest {

    @MockK
    private lateinit var repository: StatisticsRepository

    @MockK
    private lateinit var periodicRefreshUseCase: PeriodicRefreshUseCase

    private lateinit var target: FetchUpstreamDestinationsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = FetchUpstreamDestinationsUseCase(repository, periodicRefreshUseCase)
    }

    @Test
    fun `invoke - emits forward destinations successfully`() = runTest {
        val connection = ConnectionEntity.default
        val destinations = listOf(
            UpstreamDestinationEntity("destination1", 123f),
            UpstreamDestinationEntity("destination2", 456f)
        )
        val expectedResult = Result.success(
            listOf(
                UpstreamDestinationsChartData("destination1", 123f),
                UpstreamDestinationsChartData("destination2", 456f)
            )
        )

        coEvery { repository.fetchUpstreamDestinations(connection) } returns Result.success(
            destinations
        )
        coEvery { periodicRefreshUseCase<List<UpstreamDestinationsChartData>>(any()) } returns
                flowOf(expectedResult)

        target().test {
            assertThat(awaitItem()).isEqualTo(expectedResult)
            awaitComplete()
        }
    }

    @Test
    fun `invoke - emits error when repository returns failure`() = runTest {
        val connection = ConnectionEntity.default
        val expectedError = Result.failure<List<UpstreamDestinationEntity>>(
            Exception("Error fetching forward destinations")
        )

        coEvery { repository.fetchUpstreamDestinations(connection) } returns expectedError
        coEvery { periodicRefreshUseCase<List<UpstreamDestinationEntity>>(any()) } answers {
            flow {
                val fetchData =
                    arg<suspend (ConnectionEntity) -> Result<List<UpstreamDestinationEntity>>>(0)
                emit(fetchData(connection))
            }
        }

        target().test {
            assertThat(awaitItem()).isEqualTo(expectedError)
            awaitComplete()
        }
    }
}
