package eu.wedgess.piholecontrol.data.api

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.fakes.StatisticsMockHttpClient
import eu.wedgess.piholecontrol.data.model.responses.ApiErrorResponse
import eu.wedgess.piholecontrol.data.model.responses.PiHoleErrorResponseData
import eu.wedgess.piholecontrol.data.model.responses.exceptions.ApiErrorThrowable
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class StatisticsApiServiceImplTest {

    private lateinit var defaultHttpClient: HttpClient
    private lateinit var trustAllCertsHttpClient: HttpClient
    private lateinit var target: StatisticsApiService

    @Before
    fun setup() {
        defaultHttpClient = StatisticsMockHttpClient.mockSuccessHttpClient()
        trustAllCertsHttpClient = StatisticsMockHttpClient.mockErrorHttpClient()
        target = StatisticsApiServiceImpl(defaultHttpClient, trustAllCertsHttpClient)
    }

    @Test
    fun `fetchQueryTypes - returns success`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = false)

        val result = target.fetchQueryTypes(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchQueryTypes - returns failure`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = true)

        val result = target.fetchQueryTypes(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse::class.java)
        assertThat(exception.apiErrorResponse.serverError)
            .isEqualTo(PiHoleErrorResponseData.notFound)
    }

    @Test
    fun `fetchForwardDestinations - returns success`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = false)

        val result = target.fetchUpstreams(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchForwardDestinations - returns failure`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = true)

        val result = target.fetchUpstreams(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse::class.java)
        assertThat(exception.apiErrorResponse.serverError)
            .isEqualTo(PiHoleErrorResponseData.unauthorized)
    }

    @Test
    fun `fetchTopQueries - returns success`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = false)

        val result = target.fetchTopCombinedQueries(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchTopQueries - returns failure`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = true)

        val result = target.fetchTopCombinedClients(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse::class.java)
        assertThat(exception.apiErrorResponse.serverError)
            .isEqualTo(PiHoleErrorResponseData.unauthorized)
    }

    @Test
    fun `fetchTopClients - returns success`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = false)

        val result = target.fetchTopCombinedClients(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchTopClients - returns failure`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = true)

        val result = target.fetchTopCombinedClients(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse::class.java)
        assertThat(exception.apiErrorResponse.serverError)
            .isEqualTo(PiHoleErrorResponseData.unauthorized)
    }
}
