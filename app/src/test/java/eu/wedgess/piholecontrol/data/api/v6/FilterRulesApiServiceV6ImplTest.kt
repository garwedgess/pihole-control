package eu.wedgess.piholecontrol.data.api.v6

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.v6.fakes.FiltersMockHttpClientV6
import eu.wedgess.piholecontrol.data.model.enums.PiHoleFilterRuleTypeV6
import eu.wedgess.piholecontrol.data.model.requests.PiHoleAddFilterRuleRequestDataV6
import eu.wedgess.piholecontrol.data.model.responses.ApiErrorResponse
import eu.wedgess.piholecontrol.data.model.responses.exceptions.ApiErrorThrowable
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleErrorResponseDataV6
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FilterRulesApiServiceV6ImplTest {

    private lateinit var defaultHttpClient: HttpClient
    private lateinit var trustAllCertsHttpClient: HttpClient
    private lateinit var target: FilterRulesApiServiceV6

    @Before
    fun setup() {
        defaultHttpClient = FiltersMockHttpClientV6.mockSuccessHttpClient()
        trustAllCertsHttpClient = FiltersMockHttpClientV6.mockErrorHttpClient()
        target = FilterRulesApiServiceV6Impl(defaultHttpClient, trustAllCertsHttpClient)
    }

    @Test
    fun `fetchCombinedFilterRules - returns success`() = runTest {
        val connection = ConnectionEntity.Version6.default.copy(trustAllCerts = false)

        val result = target.fetchFilterRules(connection, PiHoleFilterRuleTypeV6.ALLOW)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchCombinedFilterRules - returns failure`() = runTest {
        val connection = ConnectionEntity.Version6.default.copy(trustAllCerts = true)

        val result = target.fetchFilterRules(connection, PiHoleFilterRuleTypeV6.ALLOW)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse.V6::class.java)
        assertThat((exception.apiErrorResponse as ApiErrorResponse.V6).serverError)
            .isEqualTo(PiHoleErrorResponseDataV6.unauthorized)
    }

    @Test
    fun `addFilterRule - returns success`() = runTest {
        val connection = ConnectionEntity.Version6.default.copy(trustAllCerts = false)

        val result = target.addFilterRule(
            connection,
            PiHoleAddFilterRuleRequestDataV6(
                domain = "test.com",
                comment = "",
                groups = emptyList(),
                enabled = true
            ),
            PiHoleFilterRuleTypeV6.ALLOW
        )

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `addFilterRule - returns failure`() = runTest {
        val connection = ConnectionEntity.Version6.default.copy(trustAllCerts = true)

        val result = target.addFilterRule(
            connection,
            PiHoleAddFilterRuleRequestDataV6(
                domain = "test.com",
                comment = "",
                groups = emptyList(),
                enabled = true
            ),
            PiHoleFilterRuleTypeV6.REGEX_DENY
        )

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse.V6::class.java)
        assertThat((exception.apiErrorResponse as ApiErrorResponse.V6).serverError)
            .isEqualTo(PiHoleErrorResponseDataV6.notFound)
    }

    @Test
    fun `removeFilterRule - returns success`() = runTest {
        val connection = ConnectionEntity.Version6.default.copy(trustAllCerts = false)

        val result = target.removeFilterRule(
            connection = connection,
            rule = "test.com",
            ruleType = PiHoleFilterRuleTypeV6.DENY
        )

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `removeFilterRule - returns failure`() = runTest {
        val connection = ConnectionEntity.Version6.default.copy(trustAllCerts = true)

        val result =
            target.removeFilterRule(connection, "test.com", PiHoleFilterRuleTypeV6.REGEX_ALLOW)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse.V6::class.java)
        assertThat((exception.apiErrorResponse as ApiErrorResponse.V6).serverError)
            .isEqualTo(PiHoleErrorResponseDataV6.unauthorized)
    }
}
