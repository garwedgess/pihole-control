package eu.wedgess.piholecontrol.data.api

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.fakes.LogsMockHttpClient
import eu.wedgess.piholecontrol.data.model.responses.ApiErrorResponse
import eu.wedgess.piholecontrol.data.model.responses.PiHoleErrorResponseData
import eu.wedgess.piholecontrol.data.model.responses.exceptions.ApiErrorThrowable
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
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

        val result =
            target.fetchLogs(
                connection = connection,
                limit = 500,
                domain = null,
                clientIp = null,
                clientName = null,
                queryType = null,
                advancedStatus = null,
                from = 1234567L,
                until = 12345678L
            )

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchLogs - returns failure`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = true)

        val result =
            target.fetchLogs(
                connection = connection,
                limit = 500,
                domain = null,
                clientIp = null,
                clientName = null,
                queryType = null,
                advancedStatus = null,
                from = 1234567L,
                until = 12345678L
            )

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse::class.java)
        assertThat(exception.apiErrorResponse.serverError)
            .isEqualTo(PiHoleErrorResponseData.notFound)
    }

    @Test
    fun `fetchLogFilterSuggestions - returns success`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = false)

        val result =
            target.fetchLogFilterSuggestions(connection = connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchLogFilterSuggestions - returns failure`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = true)

        val result =
            target.fetchLogFilterSuggestions(connection = connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse::class.java)
        assertThat(exception.apiErrorResponse.serverError)
            .isEqualTo(PiHoleErrorResponseData.notFound)
    }
}
