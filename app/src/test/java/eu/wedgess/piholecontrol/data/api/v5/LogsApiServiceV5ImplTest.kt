package eu.wedgess.piholecontrol.data.api.v5

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.v5.fakes.LogsMockHttpClientV5
import eu.wedgess.piholecontrol.data.model.responses.ApiErrorResponse
import eu.wedgess.piholecontrol.data.model.responses.exceptions.ApiErrorThrowable
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LogsApiServiceV5ImplTest {

    private lateinit var defaultHttpClient: HttpClient
    private lateinit var trustAllCertsHttpClient: HttpClient
    private lateinit var target: LogsApiServiceV5

    @Before
    fun setup() {
        defaultHttpClient = LogsMockHttpClientV5.mockSuccessHttpClient()
        trustAllCertsHttpClient = LogsMockHttpClientV5.mockErrorHttpClient()
        target = LogsApiServiceV5Impl(defaultHttpClient, trustAllCertsHttpClient)
    }

    @Test
    fun `fetchLogs - returns success`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = false)

        val result = target.fetchLogs(connection, 500)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchLogs - returns failure`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = true)

        val result = target.fetchLogs(connection, 500)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse.V5::class.java)
        assertThat((exception.apiErrorResponse as ApiErrorResponse.V5).errorMessage)
            .isEqualTo(
                "Client request(GET http://pi.hole/admin/api.php?getAllQueries=500) " +
                        "invalid: 400 Bad Request. Text: \"[]\""
            )
    }
}
