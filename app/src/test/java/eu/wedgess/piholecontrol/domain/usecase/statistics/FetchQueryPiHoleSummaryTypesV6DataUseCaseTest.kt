package eu.wedgess.piholecontrol.domain.usecase.statistics

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.QueryTypeEntity
import eu.wedgess.piholecontrol.domain.repository.StatisticsRepository
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.domain.usecases.statistics.FetchQueryTypesUseCase
import eu.wedgess.piholecontrol.presentation.statistics.view.donutchart.model.QueryTypeChartData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FetchQueryPiHoleSummaryTypesV6DataUseCaseTest {

    @MockK
    private lateinit var repository: StatisticsRepository

    @MockK
    private lateinit var periodicRefreshUseCase: PeriodicRefreshUseCase
    private lateinit var target: FetchQueryTypesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = FetchQueryTypesUseCase(repository, periodicRefreshUseCase)
    }

    @Test
    fun `invoke - emits success when repository returns data`() = runTest {
        val queryTypes = mapOf("A" to 50f, "AAAA" to 30f, "CNAME" to 20f)
        val expectedData = queryTypes.map { QueryTypeChartData(it.key, it.value) }
        val connection = ConnectionEntity.default

        coEvery {
            periodicRefreshUseCase<List<QueryTypeChartData>>(any())
        } answers {
            flow {
                val fetchData =
                    arg<suspend (ConnectionEntity) -> Result<List<QueryTypeChartData>>>(0)
                emit(fetchData(connection))
            }
        }

        coEvery {
            repository.fetchQueryTypes(connection)
        } returns Result.success(queryTypes.map { QueryTypeEntity(it.key, it.value) })

        target().test {
            val result = awaitItem()
            assertThat(result.isSuccess).isTrue()
            assertThat(result.getOrNull()).isEqualTo(expectedData)
            awaitComplete()
        }
    }

    @Test
    fun `invoke - emits error when repository returns failure`() = runTest {
        val connection = ConnectionEntity.default
        val exception = Exception("Failed to fetch query types")

        coEvery {
            periodicRefreshUseCase<List<QueryTypeChartData>>(any())
        } answers {
            flow {
                val fetchData =
                    arg<suspend (ConnectionEntity) -> Result<List<QueryTypeChartData>>>(0)
                emit(fetchData(connection))
            }
        }
        coEvery {
            repository.fetchQueryTypes(connection)
        } returns Result.failure(exception)

        target().test {
            val result = awaitItem()
            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isEqualTo(exception)
            awaitComplete()
        }
    }
}
