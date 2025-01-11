package eu.wedgess.piholecontrol.data.api.v5

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.v5.fakes.DashboardMockHttpClientV5
import eu.wedgess.piholecontrol.data.model.responses.ApiErrorResponse
import eu.wedgess.piholecontrol.data.model.responses.exceptions.ApiErrorThrowable
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DashboardApiServiceV5ImplTest {

    private lateinit var defaultHttpClient: HttpClient
    private lateinit var trustAllCertsHttpClient: HttpClient
    private lateinit var target: DashboardApiServiceV5

    @Before
    fun setup() {
        defaultHttpClient = DashboardMockHttpClientV5.mockSuccessHttpClient()
        trustAllCertsHttpClient = DashboardMockHttpClientV5.mockErrorHttpClient()
        target = DashboardApiServiceV5Impl(defaultHttpClient, trustAllCertsHttpClient)
    }

    @Test
    fun `fetchStatusSummary - returns success`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = false)

        val result = target.fetchStatusSummary(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchStatusSummary - returns failure`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = true)

        val result = target.fetchStatusSummary(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse.V5::class.java)
        assertThat((exception.apiErrorResponse as ApiErrorResponse.V5).errorMessage)
            .isEqualTo(
                "Client request(GET http://pi.hole/admin/api.php?summaryRaw=true) " +
                        "invalid: 400 Bad Request. Text: \"[]\""
            )
    }

    @Test
    fun `fetchOverTimeData10Minutes - returns success`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = false)

        val result = target.fetchOverTimeData10Minutes(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchOverTimeData10Minutes - returns failure`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = true)

        val result = target.fetchOverTimeData10Minutes(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse.V5::class.java)
        assertThat((exception.apiErrorResponse as ApiErrorResponse.V5).errorMessage)
            .isEqualTo(
                "Server error(GET http://pi.hole/admin/api.php?overTimeData10mins=true: " +
                        "500 Internal Server Error. Text: \"[]\""
            )
    }

    @Test
    fun `fetchOverTimeDataClients - returns success`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = false)

        val result = target.fetchOverTimeDataClients(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchOverTimeDataClients - returns failure`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = true)

        val result = target.fetchOverTimeDataClients(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse.V5::class.java)
        assertThat((exception.apiErrorResponse as ApiErrorResponse.V5).errorMessage)
            .isEqualTo(
                "Client request(GET http://pi.hole/admin/api.php" +
                        "?overTimeDataClients=true&getClientNames=true) invalid: 400 Bad Request. " +
                        "Text: \"[]\""
            )
    }
}
