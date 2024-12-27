package eu.wedgess.piholecontrol.data.api

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.fakes.StatisticsMockHttpClient
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
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
        assertThat(exception).isInstanceOf(ClientRequestException::class.java)
        assertThat(exception?.message)
            .isEqualTo(
                "Client request(GET http://pi.hole/admin/api.php?getQueryTypes=true) " +
                        "invalid: 400 Bad Request. Text: \"Client error: 400 Bad Request, Body: []\""
            )
    }

    @Test
    fun `fetchForwardDestinations - returns success`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = false)

        val result = target.fetchForwardDestinations(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchForwardDestinations - returns failure`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = true)

        val result = target.fetchForwardDestinations(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ServerResponseException::class.java)
        assertThat(exception?.message)
            .isEqualTo(
                "Server error(GET http://pi.hole/admin/api.php?getForwardDestinations=true: " +
                        "500 Internal Server Error. Text: \"Server error: 500 Internal Server Error, Body: []\""
            )
    }

    @Test
    fun `fetchTopQueries - returns success`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = false)

        val result = target.fetchTopQueries(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchTopQueries - returns failure`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = true)

        val result = target.fetchTopQueries(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ClientRequestException::class.java)
        assertThat(exception?.message)
            .isEqualTo(
                "Client request(GET http://pi.hole/admin/api.php" +
                        "?topItems=true) invalid: 400 Bad Request. " +
                        "Text: \"Client error: 400 Bad Request, Body: []\""
            )
    }

    @Test
    fun `fetchTopClients - returns success`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = false)

        val result = target.fetchTopClients(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchTopClients - returns failure`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = true)

        val result = target.fetchTopClients(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ClientRequestException::class.java)
        assertThat(exception?.message).isEqualTo(
            "Client request(GET http://pi.hole/admin/api.php" +
                    "?topClients=true) invalid: 400 Bad Request. " +
                    "Text: \"Client error: 400 Bad Request, Body: []\""
        )
    }
}
