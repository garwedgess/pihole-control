package eu.wedgess.piholecontrol.data.api.v6

import eu.wedgess.piholecontrol.data.extensions.fetchBaseRequestInfoV6
import eu.wedgess.piholecontrol.data.model.enums.PiHoleFilterRuleTypeV6
import eu.wedgess.piholecontrol.data.model.requests.PiHoleAddFilterRuleRequestDataV6
import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleAddFilterRuleResponseDataV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleFilterRulesResponseDataV6
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

class FilterRulesApiServiceV6Impl(
    @DefaultHttpClient private val defaultHttpClient: HttpClient,
    @TrustAllCertificatesHttpClient private val trustAllCertsHttpClient: HttpClient
) : FilterRulesApiServiceV6 {

    override suspend fun fetchFilterRules(
        connection: ConnectionEntity.Version6,
        ruleType: PiHoleFilterRuleTypeV6
    ): PiHoleApiResult<PiHoleFilterRulesResponseDataV6> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            val path = when (ruleType) {
                PiHoleFilterRuleTypeV6.ALLOW, PiHoleFilterRuleTypeV6.REGEX_ALLOW ->
                    FILTER_RULES_ALLOW_ENDPOINT

                PiHoleFilterRuleTypeV6.DENY, PiHoleFilterRuleTypeV6.REGEX_DENY ->
                    FILTER_RULES_DENY_ENDPOINT
            }
            fetchBaseRequestInfoV6(connection, path = path)
        }
    }

    override suspend fun addFilterRule(
        connection: ConnectionEntity.Version6,
        body: PiHoleAddFilterRuleRequestDataV6,
        ruleType: PiHoleFilterRuleTypeV6
    ): PiHoleApiResult<PiHoleAddFilterRuleResponseDataV6> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            val path = FILTER_RULES_ADD_ENDPOINT
                .replace(TYPE_KEY, ruleType.type)
                .replace(KIND_KEY, ruleType.kind)
            fetchBaseRequestInfoV6(connection, path = path)
            method = HttpMethod.Post
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }

    override suspend fun removeFilterRule(
        connection: ConnectionEntity.Version6,
        rule: String,
        ruleType: PiHoleFilterRuleTypeV6
    ): PiHoleApiResult<Unit> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            val path = FILTER_RULES_DELETE_ENDPOINT
                .replace(TYPE_KEY, ruleType.type)
                .replace(KIND_KEY, ruleType.kind)
                .replace(DOMAIN_KEY, rule.encodeURLPath())
            fetchBaseRequestInfoV6(connection, path = path)
            method = HttpMethod.Delete
        }
    }

    companion object {
        private const val TYPE_KEY = "{type}"
        private const val KIND_KEY = "{kind}"
        private const val DOMAIN_KEY = "{domain}"
        private const val FILTER_RULES_ALLOW_ENDPOINT = "/api/domains/allow"
        private const val FILTER_RULES_DENY_ENDPOINT = "/api/domains/deny"
        private const val FILTER_RULES_ADD_ENDPOINT = "/api/domains/$TYPE_KEY/$KIND_KEY"
        private const val FILTER_RULES_DELETE_ENDPOINT =
            "/api/domains/$TYPE_KEY/$KIND_KEY/$DOMAIN_KEY"
    }
}
