package eu.wedgess.piholecontrol.data.repository

import TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.StatisticsApiService
import eu.wedgess.piholecontrol.data.model.responses.PiHoleQueryTypesResponseData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopClientData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopClientsCombinedResponseData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopQueriesCombinedResponseData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopQueryData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleUpstreamsResponseData
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.TopClientQueriesEntity
import eu.wedgess.piholecontrol.domain.model.TopQueriesEntity
import eu.wedgess.piholecontrol.domain.repository.StatisticsRepository
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class StatisticsRepositoryImplTest {

    @MockK
    private lateinit var apiServiceV6: StatisticsApiService
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var target: StatisticsRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dispatcherProvider = TestDispatcherProvider()
        target = StatisticsRepositoryImpl(apiServiceV6, dispatcherProvider)
    }

    @Test
    fun `fetchQueryTypes - api fetchQueryTypes is invoked AND result is success`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        val mockResponse = mockk<PiHoleQueryTypesResponseData>(relaxed = true)
        coEvery { apiServiceV6.fetchQueryTypes(activeConnection) } returns Result.success(
            mockResponse
        )

        val result = target.fetchQueryTypes(activeConnection)

        assertThat(result.isSuccess).isTrue()
        val resultList = result.getOrNull()
        assertThat(resultList).isNotNull()
        assertThat(resultList).isInstanceOf(List::class.java)
        coVerify { apiServiceV6.fetchQueryTypes(activeConnection) }
    }

    @Test
    fun `fetchQueryTypes - api fetchQueryTypes is invoked AND result is failure`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        val exception = RuntimeException("API error")
        coEvery { apiServiceV6.fetchQueryTypes(activeConnection) } returns Result.failure(exception)

        val result = target.fetchQueryTypes(activeConnection)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { apiServiceV6.fetchQueryTypes(activeConnection) }
    }

    @Test
    fun `fetchForwardDestinations - api fetchForwardDestinations is invoked AND result is success`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity>(relaxed = true)
            val mockResponse = mockk<PiHoleUpstreamsResponseData>(relaxed = true)
            coEvery { apiServiceV6.fetchUpstreams(activeConnection) } returns Result.success(
                mockResponse
            )

            val result = target.fetchUpstreamDestinations(activeConnection)

            assertThat(result.isSuccess).isTrue()
            val resultList = result.getOrNull()
            assertThat(resultList).isNotNull()
            assertThat(resultList).isInstanceOf(List::class.java)
            coVerify { apiServiceV6.fetchUpstreams(activeConnection) }
        }

    @Test
    fun `fetchForwardDestinations - api fetchForwardDestinations is invoked AND result is failure`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity>(relaxed = true)
            val exception = RuntimeException("API error")
            coEvery { apiServiceV6.fetchUpstreams(activeConnection) } returns Result.failure(
                exception
            )

            val result = target.fetchUpstreamDestinations(activeConnection)

            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isEqualTo(exception)
            coVerify { apiServiceV6.fetchUpstreams(activeConnection) }
        }

    @Test
    fun `fetchTopCombinedQueries - api fetchTopCombinedQueries is invoked AND result is success`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity>(relaxed = true)
            val mockResponse = PiHoleTopQueriesCombinedResponseData(
                permitted = listOf(
                    PiHoleTopQueryData(domain = "test.com", count = 10),
                    PiHoleTopQueryData(domain = "example.com", count = 5)
                ),
                blocked = emptyList()
            )
            coEvery { apiServiceV6.fetchTopCombinedQueries(activeConnection) } returns Result.success(
                mockResponse
            )

            val result = target.fetchTopQueries(activeConnection)

            assertThat(result.isSuccess).isTrue()
            val resultEntity = result.getOrNull()
            assertThat(resultEntity).isNotNull()
            assertThat(resultEntity).isInstanceOf(TopQueriesEntity::class.java)
            coVerify { apiServiceV6.fetchTopCombinedQueries(activeConnection) }
        }

    @Test
    fun `fetchTopCombinedQueries - api fetchTopCombinedQueries is invoked AND result is failure`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity>(relaxed = true)
            val exception = RuntimeException("API error")
            coEvery { apiServiceV6.fetchTopCombinedQueries(activeConnection) } returns Result.failure(
                exception
            )

            val result = target.fetchTopQueries(activeConnection)

            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isEqualTo(exception)
            coVerify { apiServiceV6.fetchTopCombinedQueries(activeConnection) }
        }

    @Test
    fun `fetchTopCombinedClients - api fetchTopCombinedClients is invoked AND result is success`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity>(relaxed = true)
            val mockResponse = PiHoleTopClientsCombinedResponseData(
                all = listOf(
                    PiHoleTopClientData(name = "test", ip = "1.2.3.4", count = 5),
                    PiHoleTopClientData(name = "other", ip = "1.2.3.5", count = 3)
                ),
                blocked = emptyList()
            )
            coEvery { apiServiceV6.fetchTopCombinedClients(activeConnection) } returns Result.success(
                mockResponse
            )

            val result = target.fetchTopClients(activeConnection)

            assertThat(result.isSuccess).isTrue()
            val resultList = result.getOrNull()
            assertThat(resultList).isNotNull()
            assertThat(resultList).isInstanceOf(TopClientQueriesEntity::class.java)
            coVerify { apiServiceV6.fetchTopCombinedClients(activeConnection) }
        }

    @Test
    fun `fetchTopCombinedClients - api fetchTopCombinedClients is invoked AND result is failure`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity>(relaxed = true)
            val exception = RuntimeException("API error")
            coEvery { apiServiceV6.fetchTopCombinedClients(activeConnection) } returns Result.failure(
                exception
            )

            val result = target.fetchTopClients(activeConnection)

            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isEqualTo(exception)
            coVerify { apiServiceV6.fetchTopCombinedClients(activeConnection) }
        }
}
