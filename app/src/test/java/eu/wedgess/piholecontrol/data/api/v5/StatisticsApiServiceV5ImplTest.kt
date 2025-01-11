package eu.wedgess.piholecontrol.data.api.v5

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.v5.fakes.StatisticsMockHttpClientV5
import eu.wedgess.piholecontrol.data.model.responses.ApiErrorResponse
import eu.wedgess.piholecontrol.data.model.responses.exceptions.ApiErrorThrowable
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class StatisticsApiServiceV5ImplTest {

    private lateinit var defaultHttpClient: HttpClient
    private lateinit var trustAllCertsHttpClient: HttpClient
    private lateinit var target: StatisticsApiServiceV5

    @Before
    fun setup() {
        defaultHttpClient = StatisticsMockHttpClientV5.mockSuccessHttpClient()
        trustAllCertsHttpClient = StatisticsMockHttpClientV5.mockErrorHttpClient()
        target = StatisticsApiServiceV5Impl(defaultHttpClient, trustAllCertsHttpClient)
    }

    @Test
    fun `fetchQueryTypes - returns success`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = false)

        val result = target.fetchQueryTypes(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchQueryTypes - returns failure`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = true)

        val result = target.fetchQueryTypes(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse.V5::class.java)
        assertThat((exception.apiErrorResponse as ApiErrorResponse.V5).errorMessage)
            .isEqualTo(
                "Client request(GET http://pi.hole/admin/api.php?getQueryTypes=true) " +
                        "invalid: 400 Bad Request. Text: \"[]\""
            )
    }

    @Test
    fun `fetchForwardDestinations - returns success`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = false)

        val result = target.fetchUpstreams(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchForwardDestinations - returns failure`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = true)

        val result = target.fetchUpstreams(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse.V5::class.java)
        assertThat((exception.apiErrorResponse as ApiErrorResponse.V5).errorMessage)
            .isEqualTo(
                "Server error(GET http://pi.hole/admin/api.php?getForwardDestinations=true: " +
                        "500 Internal Server Error. Text: \"[]\""
            )
    }

    @Test
    fun `fetchTopQueries - returns success`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = false)

        val result = target.fetchTopQueries(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchTopQueries - returns failure`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = true)

        val result = target.fetchTopQueries(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse.V5::class.java)
        assertThat((exception.apiErrorResponse as ApiErrorResponse.V5).errorMessage)
            .isEqualTo(
                "Client request(GET http://pi.hole/admin/api.php" +
                        "?topItems=true) invalid: 400 Bad Request. " +
                        "Text: \"[]\""
            )
    }

    @Test
    fun `fetchTopClients - returns success`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = false)

        val result = target.fetchTopClients(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchTopClients - returns failure`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = true)

        val result = target.fetchTopClients(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse.V5::class.java)
        assertThat((exception.apiErrorResponse as ApiErrorResponse.V5).errorMessage)
            .isEqualTo(
                "Client request(GET http://pi.hole/admin/api.php" +
                        "?topClients=true) invalid: 400 Bad Request. " +
                        "Text: \"[]\""
            )
    }
}
