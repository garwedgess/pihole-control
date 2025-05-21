package eu.wedgess.piholecontrol.data.api

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.fakes.StatusMockHttpClient
import eu.wedgess.piholecontrol.data.model.responses.ApiErrorResponse
import eu.wedgess.piholecontrol.data.model.responses.PiHoleErrorResponseData
import eu.wedgess.piholecontrol.data.model.responses.exceptions.ApiErrorThrowable
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
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
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse::class.java)
        assertThat(exception.apiErrorResponse.serverError)
            .isEqualTo(PiHoleErrorResponseData.unauthorized)
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
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse::class.java)
        assertThat(exception.apiErrorResponse.serverError)
            .isEqualTo(PiHoleErrorResponseData.unauthorized)
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
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse::class.java)
        assertThat(exception.apiErrorResponse.serverError)
            .isEqualTo(PiHoleErrorResponseData.unauthorized)
    }
}
