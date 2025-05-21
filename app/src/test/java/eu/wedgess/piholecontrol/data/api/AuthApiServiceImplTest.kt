package eu.wedgess.piholecontrol.data.api

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.fakes.AuthMockHttpClient
import eu.wedgess.piholecontrol.data.model.responses.ApiErrorResponse
import eu.wedgess.piholecontrol.data.model.responses.PiHoleErrorResponseData
import eu.wedgess.piholecontrol.data.model.responses.exceptions.ApiErrorThrowable
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AuthApiServiceImplTest {

    private lateinit var defaultHttpClient: HttpClient
    private lateinit var trustAllCertsHttpClient: HttpClient
    private lateinit var target: AuthApiService

    @Before
    fun setup() {
        defaultHttpClient = AuthMockHttpClient.mockSuccessHttpClient()
        trustAllCertsHttpClient = AuthMockHttpClient.mockErrorHttpClient()
        target = AuthApiServiceImpl(defaultHttpClient, trustAllCertsHttpClient)
    }

    @Test
    fun `validateSessionId - returns success`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = false)

        val result = target.validateSessionId(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `validateSessionId - returns failure`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = true)

        val result = target.validateSessionId(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse::class.java)
        assertThat(exception.apiErrorResponse.serverError)
            .isEqualTo(PiHoleErrorResponseData.unauthorized)
    }

    @Test
    fun `generateSessionId - returns success`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = false)

        val result = target.generateSessionId(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `generateSessionId - returns failure`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = true)

        val result = target.generateSessionId(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse::class.java)
        assertThat(exception.apiErrorResponse.serverError)
            .isEqualTo(PiHoleErrorResponseData.unauthorized)
    }
}
