package eu.wedgess.piholecontrol.data.api.v5

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.v5.fakes.FiltersMockHttpClientV5
import eu.wedgess.piholecontrol.data.model.enums.PiHoleFilterRuleType
import eu.wedgess.piholecontrol.data.model.responses.ApiErrorResponse
import eu.wedgess.piholecontrol.data.model.responses.exceptions.ApiErrorThrowable
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FilterRulesApiServiceV5ImplTest {

    private lateinit var defaultHttpClient: HttpClient
    private lateinit var trustAllCertsHttpClient: HttpClient
    private lateinit var target: FilterRulesApiServiceV5

    @Before
    fun setup() {
        defaultHttpClient = FiltersMockHttpClientV5.mockSuccessHttpClient()
        trustAllCertsHttpClient = FiltersMockHttpClientV5.mockErrorHttpClient()
        target = FilterRulesApiServiceV5Impl(defaultHttpClient, trustAllCertsHttpClient)
    }

    @Test
    fun `fetchCombinedFilterRules - returns success`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = false)

        val result = target.fetchCombinedFilterRules(connection, PiHoleFilterRuleType.ALLOW)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchCombinedFilterRules - returns failure`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = true)

        val result = target.fetchCombinedFilterRules(connection, PiHoleFilterRuleType.ALLOW)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse.V5::class.java)
        assertThat((exception.apiErrorResponse as ApiErrorResponse.V5).errorMessage)
            .isEqualTo(
                "Client request(GET http://pi.hole/admin/api.php?list=white) " +
                        "invalid: 400 Bad Request. Text: \"[]\""
            )
    }

    @Test
    fun `addFilterRule - returns success`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = false)

        val result = target.addFilterRule(connection, "test.com", PiHoleFilterRuleType.ALLOW)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `addFilterRule - returns failure`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = true)

        val result = target.addFilterRule(connection, "test.com", PiHoleFilterRuleType.REGEX_DENY)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse.V5::class.java)
        assertThat((exception.apiErrorResponse as ApiErrorResponse.V5).errorMessage)
            .isEqualTo(
                "Server error(GET http://pi.hole/admin/api.php?list=regex_black&add=test.com: " +
                        "500 Internal Server Error. Text: \"[]\""
            )
    }

    @Test
    fun `removeFilterRule - returns success`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = false)

        val result = target.removeFilterRule(connection, "test.com", PiHoleFilterRuleType.DENY)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `removeFilterRule - returns failure`() = runTest {
        val connection = ConnectionEntity.Version5.default.copy(trustAllCerts = true)

        val result =
            target.removeFilterRule(connection, "test.com", PiHoleFilterRuleType.REGEX_ALLOW)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse.V5::class.java)
        assertThat((exception.apiErrorResponse as ApiErrorResponse.V5).errorMessage)
            .isEqualTo(
                "Client request(GET http://pi.hole/admin/api.php" +
                        "?list=regex_white&sub=test.com) invalid: 400 Bad Request. " +
                        "Text: \"[]\""
            )
    }
}
