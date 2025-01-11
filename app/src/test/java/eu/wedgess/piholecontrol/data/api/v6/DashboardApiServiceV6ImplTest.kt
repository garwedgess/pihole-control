package eu.wedgess.piholecontrol.data.api.v6

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.v6.fakes.DashboardMockHttpClientV6
import eu.wedgess.piholecontrol.data.model.responses.ApiErrorResponse
import eu.wedgess.piholecontrol.data.model.responses.exceptions.ApiErrorThrowable
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleErrorResponseDataV6
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DashboardApiServiceV6ImplTest {

    private lateinit var defaultHttpClient: HttpClient
    private lateinit var trustAllCertsHttpClient: HttpClient
    private lateinit var target: DashboardApiServiceV6

    @Before
    fun setup() {
        defaultHttpClient = DashboardMockHttpClientV6.mockSuccessHttpClient()
        trustAllCertsHttpClient = DashboardMockHttpClientV6.mockErrorHttpClient()
        target = DashboardApiServiceV6Impl(defaultHttpClient, trustAllCertsHttpClient)
    }

    @Test
    fun `fetchStatusSummary - returns success`() = runTest {
        val connection = ConnectionEntity.Version6.default.copy(trustAllCerts = false)

        val result = target.fetchStatusSummary(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchStatusSummary - returns failure`() = runTest {
        val connection = ConnectionEntity.Version6.default.copy(trustAllCerts = true)

        val result = target.fetchStatusSummary(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse).isInstanceOf(ApiErrorResponse.V6::class.java)
        assertThat((exception.apiErrorResponse as ApiErrorResponse.V6).serverError).isEqualTo(
            PiHoleErrorResponseDataV6.unauthorized
        )
    }

    @Test
    fun `fetchOverTimeData10Minutes - returns success`() = runTest {
        val connection = ConnectionEntity.Version6.default.copy(trustAllCerts = false)

        val result = target.fetchOverTimeData10Minutes(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchOverTimeData10Minutes - returns failure`() = runTest {
        val connection = ConnectionEntity.Version6.default.copy(trustAllCerts = true)

        val result = target.fetchOverTimeData10Minutes(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse).isInstanceOf(ApiErrorResponse.V6::class.java)
        assertThat((exception.apiErrorResponse as ApiErrorResponse.V6).serverError).isEqualTo(
            PiHoleErrorResponseDataV6.unauthorized
        )
    }

    @Test
    fun `fetchOverTimeDataClients - returns success`() = runTest {
        val connection = ConnectionEntity.Version6.default.copy(trustAllCerts = false)

        val result = target.fetchOverTimeDataClients(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchOverTimeDataClients - returns failure`() = runTest {
        val connection = ConnectionEntity.Version6.default.copy(trustAllCerts = true)

        val result = target.fetchOverTimeDataClients(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse).isInstanceOf(ApiErrorResponse.V6::class.java)
        assertThat((exception.apiErrorResponse as ApiErrorResponse.V6).serverError).isEqualTo(
            PiHoleErrorResponseDataV6.unauthorized
        )
    }
}
