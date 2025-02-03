package eu.wedgess.piholecontrol.data.repository

import TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.v5.StatisticsApiServiceV5
import eu.wedgess.piholecontrol.data.api.v6.StatisticsApiServiceV6
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleQueryTypesResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleTopClientsResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleTopQueriesResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleUpstreamsResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleQueryTypesResponseDataV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleTopClientDataV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleTopClientsCombinedResponseDataV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleTopQueriesCombinedResponseV6Data
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleTopQueryDataV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleUpstreamsResponseDataV6
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
    private lateinit var apiServiceV5: StatisticsApiServiceV5

    @MockK
    private lateinit var apiServiceV6: StatisticsApiServiceV6
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var target: StatisticsRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dispatcherProvider = TestDispatcherProvider()
        target = StatisticsRepositoryImpl(apiServiceV5, apiServiceV6, dispatcherProvider)
    }

    @Test
    fun `fetchQueryTypes - apiV5 fetchQueryTypes is invoked AND result is success`() = runTest {
        val activeConnection = mockk<ConnectionEntity.Version5>(relaxed = true)
        val mockResponse = mockk<PiHoleQueryTypesResponseDataV5>(relaxed = true)
        coEvery { apiServiceV5.fetchQueryTypes(activeConnection) } returns Result.success(
            mockResponse
        )

        val result = target.fetchQueryTypes(activeConnection)

        assertThat(result.isSuccess).isTrue()
        val resultList = result.getOrNull()
        assertThat(resultList).isNotNull()
        assertThat(resultList).isInstanceOf(List::class.java)
        coVerify { apiServiceV5.fetchQueryTypes(activeConnection) }
    }

    @Test
    fun `fetchQueryTypes - apiV5 fetchQueryTypes is invoked AND result is failure`() = runTest {
        val activeConnection = mockk<ConnectionEntity.Version5>(relaxed = true)
        val exception = RuntimeException("API error")
        coEvery { apiServiceV5.fetchQueryTypes(activeConnection) } returns Result.failure(exception)

        val result = target.fetchQueryTypes(activeConnection)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { apiServiceV5.fetchQueryTypes(activeConnection) }
    }

    @Test
    fun `fetchForwardDestinations - apiV5 fetchForwardDestinations is invoked AND result is success`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity.Version5>(relaxed = true)
            val mockResponse = mockk<PiHoleUpstreamsResponseDataV5>(relaxed = true)
            coEvery { apiServiceV5.fetchUpstreams(activeConnection) } returns Result.success(
                mockResponse
            )

            val result = target.fetchUpstreamDestinations(activeConnection)

            assertThat(result.isSuccess).isTrue()
            val resultList = result.getOrNull()
            assertThat(resultList).isNotNull()
            assertThat(resultList).isInstanceOf(List::class.java)
            coVerify { apiServiceV5.fetchUpstreams(activeConnection) }
        }

    @Test
    fun `fetchForwardDestinations - apiV5 fetchForwardDestinations is invoked AND result is failure`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity.Version5>(relaxed = true)
            val exception = RuntimeException("API error")
            coEvery { apiServiceV5.fetchUpstreams(activeConnection) } returns Result.failure(
                exception
            )

            val result = target.fetchUpstreamDestinations(activeConnection)

            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isEqualTo(exception)
            coVerify { apiServiceV5.fetchUpstreams(activeConnection) }
        }

    @Test
    fun `fetchTopQueries - apiV5 fetchTopQueries is invoked AND result is success`() = runTest {
        val activeConnection = mockk<ConnectionEntity.Version5>(relaxed = true)
        val mockResponse = mockk<PiHoleTopQueriesResponseDataV5>(relaxed = true)
        coEvery { apiServiceV5.fetchTopQueries(activeConnection) } returns Result.success(
            mockResponse
        )

        val result = target.fetchTopQueries(activeConnection)

        assertThat(result.isSuccess).isTrue()
        val resultEntity = result.getOrNull()
        assertThat(resultEntity).isNotNull()
        assertThat(resultEntity).isInstanceOf(TopQueriesEntity::class.java)
        coVerify { apiServiceV5.fetchTopQueries(activeConnection) }
    }

    @Test
    fun `fetchTopQueries - apiV5 fetchTopQueries is invoked AND result is failure`() = runTest {
        val activeConnection = mockk<ConnectionEntity.Version5>(relaxed = true)
        val exception = RuntimeException("API error")
        coEvery { apiServiceV5.fetchTopQueries(activeConnection) } returns Result.failure(exception)

        val result = target.fetchTopQueries(activeConnection)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { apiServiceV5.fetchTopQueries(activeConnection) }
    }

    @Test
    fun `fetchTopClients - apiV5 fetchTopClients is invoked AND result is success`() = runTest {
        val activeConnection = mockk<ConnectionEntity.Version5>(relaxed = true)
        val mockResponse = mockk<PiHoleTopClientsResponseDataV5>(relaxed = true)
        coEvery { apiServiceV5.fetchTopClients(activeConnection) } returns Result.success(
            mockResponse
        )

        val result = target.fetchTopClients(activeConnection)

        assertThat(result.isSuccess).isTrue()
        val resultList = result.getOrNull()
        assertThat(resultList).isNotNull()
        assertThat(resultList).isInstanceOf(TopClientQueriesEntity::class.java)
        coVerify { apiServiceV5.fetchTopClients(activeConnection) }
    }

    @Test
    fun `fetchTopClients - apiV5 fetchTopClients is invoked AND result is failure`() = runTest {
        val activeConnection = mockk<ConnectionEntity.Version5>(relaxed = true)
        val exception = RuntimeException("API error")
        coEvery { apiServiceV5.fetchTopClients(activeConnection) } returns Result.failure(exception)

        val result = target.fetchTopClients(activeConnection)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { apiServiceV5.fetchTopClients(activeConnection) }
    }

    @Test
    fun `fetchQueryTypes - apiV6 fetchQueryTypes is invoked AND result is success`() = runTest {
        val activeConnection = mockk<ConnectionEntity.Version6>(relaxed = true)
        val mockResponse = mockk<PiHoleQueryTypesResponseDataV6>(relaxed = true)
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
    fun `fetchQueryTypes - apiV6 fetchQueryTypes is invoked AND result is failure`() = runTest {
        val activeConnection = mockk<ConnectionEntity.Version6>(relaxed = true)
        val exception = RuntimeException("API error")
        coEvery { apiServiceV6.fetchQueryTypes(activeConnection) } returns Result.failure(exception)

        val result = target.fetchQueryTypes(activeConnection)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { apiServiceV6.fetchQueryTypes(activeConnection) }
    }

    @Test
    fun `fetchForwardDestinations - apiV6 fetchForwardDestinations is invoked AND result is success`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity.Version6>(relaxed = true)
            val mockResponse = mockk<PiHoleUpstreamsResponseDataV6>(relaxed = true)
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
    fun `fetchForwardDestinations - apiV6 fetchForwardDestinations is invoked AND result is failure`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity.Version6>(relaxed = true)
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
    fun `fetchTopCombinedQueries - apiV6 fetchTopCombinedQueries is invoked AND result is success`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity.Version6>(relaxed = true)
            val mockResponse = PiHoleTopQueriesCombinedResponseV6Data(
                permitted = listOf(
                    PiHoleTopQueryDataV6(domain = "test.com", count = 10),
                    PiHoleTopQueryDataV6(domain = "example.com", count = 5),
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
    fun `fetchTopCombinedQueries - apiV6 fetchTopCombinedQueries is invoked AND result is failure`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity.Version6>(relaxed = true)
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
    fun `fetchTopCombinedClients - apiV6 fetchTopCombinedClients is invoked AND result is success`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity.Version6>(relaxed = true)
            val mockResponse = PiHoleTopClientsCombinedResponseDataV6(
                all = listOf(
                    PiHoleTopClientDataV6(name = "test", ip = "1.2.3.4", count = 5),
                    PiHoleTopClientDataV6(name = "other", ip = "1.2.3.5", count = 3),
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
    fun `fetchTopCombinedClients - apiV6 fetchTopCombinedClients is invoked AND result is failure`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity.Version6>(relaxed = true)
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
