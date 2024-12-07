package eu.wedgess.piholecontrol.data.repository

import TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.StatisticsApiService
import eu.wedgess.piholecontrol.data.model.responses.PiHoleForwardDestinations
import eu.wedgess.piholecontrol.data.model.responses.PiHoleQueryTypes
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopClients
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopQueries
import eu.wedgess.piholecontrol.domain.model.*
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
    private lateinit var api: StatisticsApiService
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var target: StatisticsRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dispatcherProvider = TestDispatcherProvider()
        target = StatisticsRepositoryImpl(api, dispatcherProvider)
    }

    @Test
    fun `fetchQueryTypes - api fetchQueryTypes is invoked AND result is success`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        val mockResponse = mockk<PiHoleQueryTypes>(relaxed = true)
        coEvery { api.fetchQueryTypes(activeConnection) } returns Result.success(mockResponse)

        val result = target.fetchQueryTypes(activeConnection)

        assertThat(result.isSuccess).isTrue()
        val resultList = result.getOrNull()
        assertThat(resultList).isNotNull()
        assertThat(resultList).isInstanceOf(List::class.java)
        coVerify { api.fetchQueryTypes(activeConnection) }
    }

    @Test
    fun `fetchQueryTypes - api fetchQueryTypes is invoked AND result is failure`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        coEvery { api.fetchQueryTypes(activeConnection) } returns Result.failure(RuntimeException())

        val result = target.fetchQueryTypes(activeConnection)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isNotNull()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        coVerify { api.fetchQueryTypes(activeConnection) }
    }

    @Test
    fun `fetchForwardDestinations - api fetchForwardDestinations is invoked AND result is success`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        val mockResponse = mockk<PiHoleForwardDestinations>(relaxed = true)
        coEvery { api.fetchForwardDestinations(activeConnection) } returns Result.success(mockResponse)

        val result = target.fetchForwardDestinations(activeConnection)

        assertThat(result.isSuccess).isTrue()
        val resultList = result.getOrNull()
        assertThat(resultList).isNotNull()
        assertThat(resultList).isInstanceOf(List::class.java)
        coVerify { api.fetchForwardDestinations(activeConnection) }
    }

    @Test
    fun `fetchForwardDestinations - api fetchForwardDestinations is invoked AND result is failure`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        coEvery { api.fetchForwardDestinations(activeConnection) } returns Result.failure(RuntimeException())

        val result = target.fetchForwardDestinations(activeConnection)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isNotNull()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        coVerify { api.fetchForwardDestinations(activeConnection) }
    }

    @Test
    fun `fetchTopQueries - api fetchTopQueries is invoked AND result is success`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        val mockResponse = mockk<PiHoleTopQueries>(relaxed = true)
        coEvery { api.fetchTopQueries(activeConnection) } returns Result.success(mockResponse)

        val result = target.fetchTopQueries(activeConnection)

        assertThat(result.isSuccess).isTrue()
        val resultEntity = result.getOrNull()
        assertThat(resultEntity).isNotNull()
        assertThat(resultEntity).isInstanceOf(TopQueriesEntity::class.java)
        coVerify { api.fetchTopQueries(activeConnection) }
    }

    @Test
    fun `fetchTopQueries - api fetchTopQueries is invoked AND result is failure`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        coEvery { api.fetchTopQueries(activeConnection) } returns Result.failure(RuntimeException())

        val result = target.fetchTopQueries(activeConnection)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isNotNull()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        coVerify { api.fetchTopQueries(activeConnection) }
    }

    @Test
    fun `fetchTopClients - api fetchTopClients is invoked AND result is success`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        val mockResponse = mockk<PiHoleTopClients>(relaxed = true)
        coEvery { api.fetchTopClients(activeConnection) } returns Result.success(mockResponse)

        val result = target.fetchTopClients(activeConnection)

        assertThat(result.isSuccess).isTrue()
        val resultList = result.getOrNull()
        assertThat(resultList).isNotNull()
        assertThat(resultList).isInstanceOf(List::class.java)
        coVerify { api.fetchTopClients(activeConnection) }
    }

    @Test
    fun `fetchTopClients - api fetchTopClients is invoked AND result is failure`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        coEvery { api.fetchTopClients(activeConnection) } returns Result.failure(RuntimeException())

        val result = target.fetchTopClients(activeConnection)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isNotNull()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        coVerify { api.fetchTopClients(activeConnection) }
    }
}
