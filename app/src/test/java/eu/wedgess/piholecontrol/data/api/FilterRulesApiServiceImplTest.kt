package eu.wedgess.piholecontrol.data.api

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.fakes.FiltersMockHttpClient
import eu.wedgess.piholecontrol.data.model.enums.PiHoleFilterRuleType
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
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
    fun `fetchFilterRules - returns success`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = false)

        val result = target.fetchFilterRules(connection, PiHoleFilterRuleType.ALLOW)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchFilterRules - returns failure`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = true)

        val result = target.fetchFilterRules(connection, PiHoleFilterRuleType.ALLOW)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ClientRequestException::class.java)
        assertThat(exception?.message)
            .isEqualTo(
                "Client request(GET http://pi.hole/admin/api.php?list=white) " +
                        "invalid: 400 Bad Request. Text: \"Client error: 400 Bad Request, Body: []\""
            )
    }

    @Test
    fun `addFilterRule - returns success`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = false)

        val result = target.addFilterRule(connection, "test.com", PiHoleFilterRuleType.ALLOW)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `addFilterRule - returns failure`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = true)

        val result = target.addFilterRule(connection, "test.com", PiHoleFilterRuleType.REGEX_BLOCK)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ServerResponseException::class.java)
        assertThat(exception?.message)
            .isEqualTo(
                "Server error(GET http://pi.hole/admin/api.php?list=regex_black&add=test.com: " +
                        "500 Internal Server Error. Text: \"Server error: 500 Internal Server Error, Body: []\""
            )
    }

    @Test
    fun `removeFilterRule - returns success`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = false)

        val result = target.removeFilterRule(connection, "test.com", PiHoleFilterRuleType.BLOCK)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `removeFilterRule - returns failure`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = true)

        val result =
            target.removeFilterRule(connection, "test.com", PiHoleFilterRuleType.REGEX_ALLOW)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ClientRequestException::class.java)
        assertThat(exception?.message).isEqualTo(
            "Client request(GET http://pi.hole/admin/api.php" +
                    "?list=regex_white&sub=test.com) invalid: 400 Bad Request. " +
                    "Text: \"Client error: 400 Bad Request, Body: []\""
        )
    }
}
