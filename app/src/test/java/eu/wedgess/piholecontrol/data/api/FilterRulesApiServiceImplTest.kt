package eu.wedgess.piholecontrol.data.api

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.fakes.FiltersMockHttpClient
import eu.wedgess.piholecontrol.data.model.enums.PiHoleFilterRuleType
import eu.wedgess.piholecontrol.data.model.requests.PiHoleAddFilterRuleRequestData
import eu.wedgess.piholecontrol.data.model.responses.ApiErrorResponse
import eu.wedgess.piholecontrol.data.model.responses.PiHoleErrorResponseData
import eu.wedgess.piholecontrol.data.model.responses.exceptions.ApiErrorThrowable
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FilterRulesApiServiceImplTest {

    private lateinit var defaultHttpClient: HttpClient
    private lateinit var trustAllCertsHttpClient: HttpClient
    private lateinit var target: FilterRulesApiService

    @Before
    fun setup() {
        defaultHttpClient = FiltersMockHttpClient.mockSuccessHttpClient()
        trustAllCertsHttpClient = FiltersMockHttpClient.mockErrorHttpClient()
        target = FilterRulesApiServiceImpl(defaultHttpClient, trustAllCertsHttpClient)
    }

    @Test
    fun `fetchCombinedFilterRules - returns success`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = false)

        val result = target.fetchFilterRules(connection, PiHoleFilterRuleType.ALLOW)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchCombinedFilterRules - returns failure`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = true)

        val result = target.fetchFilterRules(connection, PiHoleFilterRuleType.ALLOW)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse::class.java)
        assertThat(exception.apiErrorResponse.serverError)
            .isEqualTo(PiHoleErrorResponseData.unauthorized)
    }

    @Test
    fun `addFilterRule - returns success`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = false)

        val result = target.addFilterRule(
            connection,
            PiHoleAddFilterRuleRequestData(
                domain = "test.com",
                comment = "",
                groups = emptyList(),
                enabled = true
            ),
            PiHoleFilterRuleType.ALLOW
        )

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `addFilterRule - returns failure`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = true)

        val result = target.addFilterRule(
            connection,
            PiHoleAddFilterRuleRequestData(
                domain = "test.com",
                comment = "",
                groups = emptyList(),
                enabled = true
            ),
            PiHoleFilterRuleType.REGEX_DENY
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
    fun `removeFilterRule - returns success`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = false)

        val result = target.removeFilterRule(
            connection = connection,
            rule = "test.com",
            ruleType = PiHoleFilterRuleType.DENY
        )

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `removeFilterRule - returns failure`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = true)

        val result =
            target.removeFilterRule(connection, "test.com", PiHoleFilterRuleType.REGEX_ALLOW)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse::class.java)
        assertThat(exception.apiErrorResponse.serverError)
            .isEqualTo(PiHoleErrorResponseData.unauthorized)
    }
}
