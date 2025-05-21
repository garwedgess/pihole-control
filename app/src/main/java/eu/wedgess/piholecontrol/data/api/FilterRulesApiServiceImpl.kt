package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.extensions.fetchBaseRequestInfo
import eu.wedgess.piholecontrol.data.model.enums.PiHoleFilterRuleType
import eu.wedgess.piholecontrol.data.model.requests.PiHoleAddFilterRuleRequestData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleAddFilterRuleResponseData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.PiHoleFilterRulesResponseData
import eu.wedgess.piholecontrol.data.utils.requestResult
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.http.encodeURLPath

class FilterRulesApiServiceImpl(
    @DefaultHttpClient private val defaultHttpClient: HttpClient,
    @TrustAllCertificatesHttpClient private val trustAllCertsHttpClient: HttpClient
) : FilterRulesApiService {

    override suspend fun fetchFilterRules(
        connection: ConnectionEntity,
        ruleType: PiHoleFilterRuleType
    ): PiHoleApiResult<PiHoleFilterRulesResponseData> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            val path = when (ruleType) {
                PiHoleFilterRuleType.ALLOW, PiHoleFilterRuleType.REGEX_ALLOW ->
                    FILTER_RULES_ALLOW_ENDPOINT

                PiHoleFilterRuleType.DENY, PiHoleFilterRuleType.REGEX_DENY ->
                    FILTER_RULES_DENY_ENDPOINT
            }
            fetchBaseRequestInfo(connection, path = path)
        }
    }

    override suspend fun addFilterRule(
        connection: ConnectionEntity,
        body: PiHoleAddFilterRuleRequestData,
        ruleType: PiHoleFilterRuleType
    ): PiHoleApiResult<PiHoleAddFilterRuleResponseData> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            val path = FILTER_RULES_ADD_ENDPOINT
                .replace(TYPE_KEY, ruleType.type)
                .replace(KIND_KEY, ruleType.kind)
            fetchBaseRequestInfo(connection, path = path)
            method = HttpMethod.Post
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }

    override suspend fun removeFilterRule(
        connection: ConnectionEntity,
        rule: String,
        ruleType: PiHoleFilterRuleType
    ): PiHoleApiResult<Unit> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            val path = FILTER_RULES_DELETE_ENDPOINT
                .replace(TYPE_KEY, ruleType.type)
                .replace(KIND_KEY, ruleType.kind)
                .replace(DOMAIN_KEY, rule.encodeURLPath())
            fetchBaseRequestInfo(connection, path = path)
            method = HttpMethod.Delete
        }
    }

    companion object {
        private const val TYPE_KEY = "{type}"
        private const val KIND_KEY = "{kind}"
        private const val DOMAIN_KEY = "{domain}"
        private const val FILTER_RULES_ALLOW_ENDPOINT = "/domains/allow"
        private const val FILTER_RULES_DENY_ENDPOINT = "/domains/deny"
        private const val FILTER_RULES_ADD_ENDPOINT = "/domains/$TYPE_KEY/$KIND_KEY"
        private const val FILTER_RULES_DELETE_ENDPOINT = "/domains/$TYPE_KEY/$KIND_KEY/$DOMAIN_KEY"
    }
}
