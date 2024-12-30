package eu.wedgess.piholecontrol.domain.usecase.statistics

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.TopDomainEntity
import eu.wedgess.piholecontrol.domain.model.TopQueriesEntity
import eu.wedgess.piholecontrol.domain.repository.StatisticsRepository
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.domain.usecases.statistics.FetchTopQueriesUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FetchTopQueriesUseCaseTest {

    @MockK
    private lateinit var repository: StatisticsRepository

    @MockK
    private lateinit var periodicRefreshUseCase: PeriodicRefreshUseCase

    private lateinit var target: FetchTopQueriesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = FetchTopQueriesUseCase(repository, periodicRefreshUseCase)
    }

    @Test
    fun `invoke - emits success when repository returns data`() = runTest {
        val topQueries = TopQueriesEntity(
            allowed = listOf(
                TopDomainEntity(domain = "example.com", hits = 120),
                TopDomainEntity(domain = "test.com", hits = 80)
            ),
            blocked = listOf(
                TopDomainEntity(domain = "ads.com", hits = 95),
                TopDomainEntity(domain = "trackers.com", hits = 70)
            )
        )
        val connection = ConnectionEntity.default

        coEvery {
            periodicRefreshUseCase<TopQueriesEntity>(any())
        } answers {
            flow {
                val fetchData = arg<suspend (ConnectionEntity) -> Result<TopQueriesEntity>>(0)
                emit(fetchData(connection))
            }
        }
        coEvery {
            repository.fetchTopQueries(connection)
        } returns Result.success(topQueries)

        target().test {
            val result = awaitItem()
            assertThat(result.isSuccess).isTrue()
            assertThat(result.getOrNull()).isEqualTo(topQueries)
            awaitComplete()
        }
    }

    @Test
    fun `invoke - emits error when repository returns failure`() = runTest {
        val connection = ConnectionEntity.default
        val exception = Exception("Failed to fetch top queries")

        coEvery {
            periodicRefreshUseCase<TopQueriesEntity>(any())
        } answers {
            flow {
                val fetchData = arg<suspend (ConnectionEntity) -> Result<TopQueriesEntity>>(0)
                emit(fetchData(connection))
            }
        }
        coEvery {
            repository.fetchTopQueries(connection)
        } returns Result.failure(exception)

        target().test {
            val result = awaitItem()
            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isEqualTo(exception)
            awaitComplete()
        }
    }
}
