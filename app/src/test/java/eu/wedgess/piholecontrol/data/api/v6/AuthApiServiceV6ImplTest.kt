package eu.wedgess.piholecontrol.data.api.v6

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.v6.fakes.AuthMockHttpClientV6
import eu.wedgess.piholecontrol.data.model.responses.ApiErrorResponse
import eu.wedgess.piholecontrol.data.model.responses.exceptions.ApiErrorThrowable
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleErrorResponseDataV6
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AuthApiServiceV6ImplTest {

    private lateinit var defaultHttpClient: HttpClient
    private lateinit var trustAllCertsHttpClient: HttpClient
    private lateinit var target: AuthApiServiceV6

    @Before
    fun setup() {
        defaultHttpClient = AuthMockHttpClientV6.mockSuccessHttpClient()
        trustAllCertsHttpClient = AuthMockHttpClientV6.mockErrorHttpClient()
        target = AuthApiServiceV6Impl(defaultHttpClient, trustAllCertsHttpClient)
    }

    @Test
    fun `validateSessionId - returns success`() = runTest {
        val connection = ConnectionEntity.Version6.default.copy(trustAllCerts = false)

        val result = target.validateSessionId(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `validateSessionId - returns failure`() = runTest {
        val connection = ConnectionEntity.Version6.default.copy(trustAllCerts = true)

        val result = target.validateSessionId(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse.V6::class.java)
        assertThat((exception.apiErrorResponse as ApiErrorResponse.V6).serverError)
            .isEqualTo(PiHoleErrorResponseDataV6.unauthorized)
    }

    @Test
    fun `generateSessionId - returns success`() = runTest {
        val connection = ConnectionEntity.Version6.default.copy(trustAllCerts = false)

        val result = target.generateSessionId(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `generateSessionId - returns failure`() = runTest {
        val connection = ConnectionEntity.Version6.default.copy(trustAllCerts = true)

        val result = target.generateSessionId(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse.V6::class.java)
        assertThat((exception.apiErrorResponse as ApiErrorResponse.V6).serverError)
            .isEqualTo(PiHoleErrorResponseDataV6.unauthorized)
    }
}
