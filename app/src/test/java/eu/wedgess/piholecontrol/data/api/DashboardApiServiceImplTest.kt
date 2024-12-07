package eu.wedgess.piholecontrol.data.api

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.fakes.DashboardMockHttpClient
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DashboardApiServiceImplTest {

    private lateinit var defaultHttpClient: HttpClient
    private lateinit var trustAllCertsHttpClient: HttpClient
    private lateinit var target: DashboardApiService

    @Before
    fun setup() {
        defaultHttpClient = DashboardMockHttpClient.mockSuccessHttpClient()
        trustAllCertsHttpClient = DashboardMockHttpClient.mockErrorHttpClient()
        target = DashboardApiServiceImpl(defaultHttpClient, trustAllCertsHttpClient)
    }

    @Test
    fun `fetchStatusSummary - returns success`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = false)

        val result = target.fetchStatusSummary(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchStatusSummary - returns failure`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = true)

        val result = target.fetchStatusSummary(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ClientRequestException::class.java)
        assertThat(exception?.message)
            .isEqualTo(
                "Client request(GET http://pi.hole/admin/api.php?summaryRaw=true) " +
                        "invalid: 400 Bad Request. Text: \"Client error: 400 Bad Request, Body: []\""
            )
    }

    @Test
    fun `fetchOverTimeData10Minutes - returns success`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = false)

        val result = target.fetchOverTimeData10Minutes(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchOverTimeData10Minutes - returns failure`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = true)

        val result = target.fetchOverTimeData10Minutes(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ServerResponseException::class.java)
        assertThat(exception?.message)
            .isEqualTo(
                "Server error(GET http://pi.hole/admin/api.php?overTimeData10mins=true: " +
                        "500 Internal Server Error. Text: \"Server error: 500 Internal Server Error, Body: []\""
            )
    }


    @Test
    fun `fetchOverTimeDataClients - returns success`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = false)

        val result = target.fetchOverTimeDataClients(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchOverTimeDataClients - returns failure`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = true)

        val result = target.fetchOverTimeDataClients(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ClientRequestException::class.java)
        assertThat(exception?.message)
            .isEqualTo(
                "Client request(GET http://pi.hole/admin/api.php" +
                        "?overTimeDataClients=true&getClientNames=true) invalid: 400 Bad Request. " +
                        "Text: \"Client error: 400 Bad Request, Body: []\""
            )
    }


}