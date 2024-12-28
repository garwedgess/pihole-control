package eu.wedgess.piholecontrol.domain.usecase.statistics

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.ForwardDestinationEntity
import eu.wedgess.piholecontrol.domain.repository.StatisticsRepository
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.domain.usecases.statistics.FetchForwardDestinationsUseCase
import eu.wedgess.piholecontrol.presentation.statistics.view.donutchart.model.ForwardDestinationsChartData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FetchForwardDestinationsUseCaseTest {

    @MockK
    private lateinit var repository: StatisticsRepository

    @MockK
    private lateinit var periodicRefreshUseCase: PeriodicRefreshUseCase

    private lateinit var target: FetchForwardDestinationsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = FetchForwardDestinationsUseCase(repository, periodicRefreshUseCase)
    }

    @Test
    fun `invoke - emits forward destinations successfully`() = runTest {
        val connection = ConnectionEntity.default
        val destinations = listOf(
            ForwardDestinationEntity("destination1", 123f),
            ForwardDestinationEntity("destination2", 456f)
        )
        val expectedResult = Result.success(
            listOf(
                ForwardDestinationsChartData("destination1", 123f),
                ForwardDestinationsChartData("destination2", 456f)
            )
        )

        coEvery { repository.fetchForwardDestinations(connection) } returns Result.success(
            destinations
        )
        coEvery { periodicRefreshUseCase<List<ForwardDestinationsChartData>>(any()) } returns
                flowOf(expectedResult)

        target().test {
            assertThat(awaitItem()).isEqualTo(expectedResult)
            awaitComplete()
        }
    }

    @Test
    fun `invoke - emits error when repository returns failure`() = runTest {
        val connection = ConnectionEntity.default
        val expectedError = Result.failure<List<ForwardDestinationEntity>>(
            Exception("Error fetching forward destinations")
        )

        coEvery { repository.fetchForwardDestinations(connection) } returns expectedError
        coEvery { periodicRefreshUseCase<List<ForwardDestinationEntity>>(any()) } answers {
            flow {
                val fetchData =
                    arg<suspend (ConnectionEntity) -> Result<List<ForwardDestinationEntity>>>(0)
                emit(fetchData(connection))
            }
        }

        target().test {
            assertThat(awaitItem()).isEqualTo(expectedError)
            awaitComplete()
        }
    }
}
