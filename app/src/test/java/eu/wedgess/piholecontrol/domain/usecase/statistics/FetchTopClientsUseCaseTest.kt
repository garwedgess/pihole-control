package eu.wedgess.piholecontrol.domain.usecase.statistics

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.TopClientEntity
import eu.wedgess.piholecontrol.domain.repository.StatisticsRepository
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.domain.usecases.statistics.FetchTopClientsUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FetchTopClientsUseCaseTest {

    @MockK
    private lateinit var repository: StatisticsRepository

    @MockK
    private lateinit var periodicRefreshUseCase: PeriodicRefreshUseCase

    private lateinit var target: FetchTopClientsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = FetchTopClientsUseCase(repository, periodicRefreshUseCase)
    }

    @Test
    fun `invoke - emits success when repository returns data`() = runTest {
        val topClients = listOf(
            TopClientEntity("192.168.1.2", 120),
            TopClientEntity("192.168.1.3", 95),
            TopClientEntity("192.168.1.4", 78)
        )
        val connection = ConnectionEntity.default

        coEvery {
            periodicRefreshUseCase<List<TopClientEntity>>(any())
        } answers {
            flow {
                val fetchData = arg<suspend (ConnectionEntity) -> Result<List<TopClientEntity>>>(0)
                emit(fetchData(connection))
            }
        }
        coEvery {
            repository.fetchTopClients(connection)
        } returns Result.success(topClients)

        target().test {
            val result = awaitItem()
            assertThat(result.isSuccess).isTrue()
            assertThat(result.getOrNull()).isEqualTo(topClients)
            awaitComplete()
        }
    }

    @Test
    fun `invoke - emits error when repository returns failure`() = runTest {
        val connection = ConnectionEntity.default
        val exception = Exception("Failed to fetch top clients")

        coEvery {
            periodicRefreshUseCase<List<TopClientEntity>>(any())
        } answers {
            flow {
                val fetchData = arg<suspend (ConnectionEntity) -> Result<List<TopClientEntity>>>(0)
                emit(fetchData(connection))
            }
        }
        coEvery {
            repository.fetchTopClients(connection)
        } returns Result.failure(exception)

        target().test {
            val result = awaitItem()
            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isEqualTo(exception)
            awaitComplete()
        }
    }
}
