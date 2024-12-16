package eu.wedgess.piholecontrol.domain.usecase.statistics

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
import kotlinx.coroutines.flow.toList
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
            periodicRefreshUseCase<Result<TopQueriesEntity>>(any())
        } answers {
            flow {
                val fetchData = arg<suspend (ConnectionEntity) -> Result<TopQueriesEntity>>(0)
                emit(fetchData(connection))
            }
        }
        coEvery {
            repository.fetchTopQueries(connection)
        } returns Result.success(topQueries)

        val result = target().toList()

        assertThat(result).hasSize(1)
        assertThat(result.first().isSuccess).isTrue()
        assertThat(result.first().getOrNull()).isEqualTo(topQueries)
    }

    @Test
    fun `invoke - emits error when repository returns failure`() = runTest {
        val connection = ConnectionEntity.default
        val exception = Exception("Failed to fetch top queries")

        coEvery {
            periodicRefreshUseCase<Result<TopQueriesEntity>>(any())
        } answers {
            flow {
                val fetchData = arg<suspend (ConnectionEntity) -> Result<TopQueriesEntity>>(0)
                emit(fetchData(connection))
            }
        }
        coEvery {
            repository.fetchTopQueries(connection)
        } returns Result.failure(exception)

        val result = target().toList()

        assertThat(result).hasSize(1)
        assertThat(result.first().isFailure).isTrue()
        assertThat(result.first().exceptionOrNull()).isEqualTo(exception)
    }
}
