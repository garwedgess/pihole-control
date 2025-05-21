package eu.wedgess.piholecontrol.data.api

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.fakes.DashboardMockHttpClient
import eu.wedgess.piholecontrol.data.model.responses.ApiErrorResponse
import eu.wedgess.piholecontrol.data.model.responses.PiHoleErrorResponseData
import eu.wedgess.piholecontrol.data.model.responses.exceptions.ApiErrorThrowable
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
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
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse).isInstanceOf(ApiErrorResponse::class.java)
        assertThat(exception.apiErrorResponse.serverError).isEqualTo(
            PiHoleErrorResponseData.unauthorized
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
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse).isInstanceOf(ApiErrorResponse::class.java)
        assertThat(exception.apiErrorResponse.serverError).isEqualTo(
            PiHoleErrorResponseData.unauthorized
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
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse).isInstanceOf(ApiErrorResponse::class.java)
        assertThat(exception.apiErrorResponse.serverError).isEqualTo(
            PiHoleErrorResponseData.unauthorized
        )
    }
}
