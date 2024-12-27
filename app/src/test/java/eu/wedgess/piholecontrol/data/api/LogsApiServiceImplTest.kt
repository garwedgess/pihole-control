package eu.wedgess.piholecontrol.data.api

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.fakes.LogsMockHttpClient
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LogsApiServiceImplTest {

    private lateinit var defaultHttpClient: HttpClient
    private lateinit var trustAllCertsHttpClient: HttpClient
    private lateinit var target: LogsApiService

    @Before
    fun setup() {
        defaultHttpClient = LogsMockHttpClient.mockSuccessHttpClient()
        trustAllCertsHttpClient = LogsMockHttpClient.mockErrorHttpClient()
        target = LogsApiServiceImpl(defaultHttpClient, trustAllCertsHttpClient)
    }

    @Test
    fun `fetchLogs - returns success`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = false)

        val result = target.fetchLogs(connection, 500)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchLogs - returns failure`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = true)

        val result = target.fetchLogs(connection, 500)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ClientRequestException::class.java)
        assertThat(exception?.message).isEqualTo(
            "Client request(GET http://pi.hole/admin/api.php?getAllQueries=500) " +
                    "invalid: 400 Bad Request. Text: \"Client error: 400 Bad Request, Body: []\""
        )
    }
}
