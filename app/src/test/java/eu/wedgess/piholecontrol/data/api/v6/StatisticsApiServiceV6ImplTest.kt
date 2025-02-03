package eu.wedgess.piholecontrol.data.api.v6

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.v6.fakes.StatisticsMockHttpClientV6
import eu.wedgess.piholecontrol.data.model.responses.ApiErrorResponse
import eu.wedgess.piholecontrol.data.model.responses.exceptions.ApiErrorThrowable
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleErrorResponseDataV6
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class StatisticsApiServiceV6ImplTest {

    private lateinit var defaultHttpClient: HttpClient
    private lateinit var trustAllCertsHttpClient: HttpClient
    private lateinit var target: StatisticsApiServiceV6

    @Before
    fun setup() {
        defaultHttpClient = StatisticsMockHttpClientV6.mockSuccessHttpClient()
        trustAllCertsHttpClient = StatisticsMockHttpClientV6.mockErrorHttpClient()
        target = StatisticsApiServiceV6Impl(defaultHttpClient, trustAllCertsHttpClient)
    }

    @Test
    fun `fetchQueryTypes - returns success`() = runTest {
        val connection = ConnectionEntity.Version6.default.copy(trustAllCerts = false)

        val result = target.fetchQueryTypes(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchQueryTypes - returns failure`() = runTest {
        val connection = ConnectionEntity.Version6.default.copy(trustAllCerts = true)

        val result = target.fetchQueryTypes(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse.V6::class.java)
        assertThat((exception.apiErrorResponse as ApiErrorResponse.V6).serverError)
            .isEqualTo(PiHoleErrorResponseDataV6.notFound)
    }

    @Test
    fun `fetchForwardDestinations - returns success`() = runTest {
        val connection = ConnectionEntity.Version6.default.copy(trustAllCerts = false)

        val result = target.fetchUpstreams(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchForwardDestinations - returns failure`() = runTest {
        val connection = ConnectionEntity.Version6.default.copy(trustAllCerts = true)

        val result = target.fetchUpstreams(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse.V6::class.java)
        assertThat((exception.apiErrorResponse as ApiErrorResponse.V6).serverError)
            .isEqualTo(PiHoleErrorResponseDataV6.unauthorized)
    }

    @Test
    fun `fetchTopQueries - returns success`() = runTest {
        val connection = ConnectionEntity.Version6.default.copy(trustAllCerts = false)

        val result = target.fetchTopCombinedQueries(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchTopQueries - returns failure`() = runTest {
        val connection = ConnectionEntity.Version6.default.copy(trustAllCerts = true)

        val result = target.fetchTopCombinedClients(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse.V6::class.java)
        assertThat((exception.apiErrorResponse as ApiErrorResponse.V6).serverError)
            .isEqualTo(PiHoleErrorResponseDataV6.unauthorized)
    }

    @Test
    fun `fetchTopClients - returns success`() = runTest {
        val connection = ConnectionEntity.Version6.default.copy(trustAllCerts = false)

        val result = target.fetchTopCombinedClients(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchTopClients - returns failure`() = runTest {
        val connection = ConnectionEntity.Version6.default.copy(trustAllCerts = true)

        val result = target.fetchTopCombinedClients(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse.V6::class.java)
        assertThat((exception.apiErrorResponse as ApiErrorResponse.V6).serverError)
            .isEqualTo(PiHoleErrorResponseDataV6.unauthorized)
    }
}
