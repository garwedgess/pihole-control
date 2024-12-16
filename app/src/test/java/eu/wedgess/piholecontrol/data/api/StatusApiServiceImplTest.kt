package eu.wedgess.piholecontrol.data.api

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.fakes.StatusMockHttpClient
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration

class StatusApiServiceImplTest {

    private lateinit var defaultHttpClient: HttpClient
    private lateinit var trustAllCertsHttpClient: HttpClient
    private lateinit var target: StatusApiService

    @Before
    fun setup() {
        defaultHttpClient = StatusMockHttpClient.mockSuccessHttpClient()
        trustAllCertsHttpClient = StatusMockHttpClient.mockErrorHttpClient()
        target = StatusApiServiceImpl(defaultHttpClient, trustAllCertsHttpClient)
    }

    @Test
    fun `fetchStatus - returns success`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = false)

        val result = target.fetchStatus(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchStatus - returns failure`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = true)

        val result = target.fetchStatus(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ClientRequestException::class.java)
        assertThat(exception?.message)
            .isEqualTo(
                "Client request(GET http://pi.hole/admin/api.php?status=true) " +
                        "invalid: 400 Bad Request. Text: \"Client error: 400 Bad Request, Body: []\""
            )
    }

    @Test
    fun `enableAdBlocking - returns success`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = false)

        val result = target.enableAdBlocking(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `enableAdBlocking - returns failure`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = true)

        val result = target.enableAdBlocking(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ServerResponseException::class.java)
        assertThat(exception?.message)
            .isEqualTo(
                "Server error(GET http://pi.hole/admin/api.php?enable=true: " +
                        "500 Internal Server Error. Text: \"Server error: 500 Internal Server Error, Body: []\""
            )
    }

    @Test
    fun `disableAdBlocking - returns success`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = false)

        val result = target.disableAdBlocking(connection, Duration.INFINITE)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `disableAdBlocking - returns failure`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = true)

        val result =
            target.disableAdBlocking(connection, Duration.INFINITE)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ClientRequestException::class.java)
        assertThat(exception?.message)
            .isEqualTo(
                "Client request(GET http://pi.hole/admin/api.php" +
                        "?disable=0) invalid: 400 Bad Request. " +
                        "Text: \"Client error: 400 Bad Request, Body: []\""
            )
    }
}
