package eu.wedgess.piholecontrol.data.api.v5

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.v5.fakes.StatusMockHttpClientV5
import eu.wedgess.piholecontrol.data.model.responses.ApiErrorResponse
import eu.wedgess.piholecontrol.data.model.responses.exceptions.ApiErrorThrowable
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration

class StatusServiceV5ImplTest {

    private lateinit var defaultHttpClient: HttpClient
    private lateinit var trustAllCertsHttpClient: HttpClient
    private lateinit var target: StatusApiServiceV5

    @Before
    fun setup() {
        defaultHttpClient = StatusMockHttpClientV5.mockSuccessHttpClient()
        trustAllCertsHttpClient = StatusMockHttpClientV5.mockErrorHttpClient()
        target = StatusApiServiceV5Impl(defaultHttpClient, trustAllCertsHttpClient)
    }

    @Test
    fun `fetchStatus - returns success`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = false)

        val result = target.fetchStatus(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchStatus - returns failure`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = true)

        val result = target.fetchStatus(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse.V5::class.java)
        assertThat((exception.apiErrorResponse as ApiErrorResponse.V5).errorMessage)
            .isEqualTo(
                "Client request(GET http://pi.hole/admin/api.php?status=true) " +
                        "invalid: 400 Bad Request. Text: \"[]\""
            )
    }

    @Test
    fun `enableAdBlocking - returns success`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = false)

        val result = target.enableAdBlocking(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `enableAdBlocking - returns failure`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = true)

        val result = target.enableAdBlocking(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse.V5::class.java)
        assertThat((exception.apiErrorResponse as ApiErrorResponse.V5).errorMessage)
            .isEqualTo(
                "Server error(GET http://pi.hole/admin/api.php?enable=true: " +
                        "500 Internal Server Error. Text: \"[]\""
            )
    }

    @Test
    fun `disableAdBlocking - returns success`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = false)

        val result = target.disableAdBlocking(connection, Duration.INFINITE)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `disableAdBlocking - returns failure`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = true)

        val result =
            target.disableAdBlocking(connection, Duration.INFINITE)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse.V5::class.java)
        assertThat((exception.apiErrorResponse as ApiErrorResponse.V5).errorMessage)
            .isEqualTo(
            "Client request(GET http://pi.hole/admin/api.php" +
                    "?disable=0) invalid: 400 Bad Request. " +
                    "Text: \"[]\""
        )
    }
}
