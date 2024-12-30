package eu.wedgess.piholecontrol.data.api.v5

import eu.wedgess.piholecontrol.data.extensions.fetchBaseRequestInfoV5
import eu.wedgess.piholecontrol.data.model.enums.PiHoleFilterRuleType
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleFilterRules
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleModifyFilterRuleResponse
import eu.wedgess.piholecontrol.data.utils.requestResult
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import javax.inject.Inject

class FilterRulesApiServiceImpl @Inject constructor(
    @DefaultHttpClient private val defaultHttpClient: HttpClient,
    @TrustAllCertificatesHttpClient private val trustAllCertsHttpClient: HttpClient
) : FilterRulesApiService {

    override suspend fun fetchFilterRules(
        connection: ConnectionEntity,
        ruleType: PiHoleFilterRuleType
    ): Result<PiHoleFilterRules> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleFilterRules, String> {
            fetchBaseRequestInfoV5(connection)
            url {
                parameters["list"] = ruleType.value
            }
        }
    }

    override suspend fun addFilterRule(
        connection: ConnectionEntity,
        rule: String,
        ruleType: PiHoleFilterRuleType
    ): Result<PiHoleModifyFilterRuleResponse> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleModifyFilterRuleResponse, String> {
            fetchBaseRequestInfoV5(connection)
            url {
                parameters["list"] = ruleType.value
                parameters["add"] = rule
            }
        }
    }

    override suspend fun removeFilterRule(
        connection: ConnectionEntity,
        rule: String,
        ruleType: PiHoleFilterRuleType
    ): Result<PiHoleModifyFilterRuleResponse> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleModifyFilterRuleResponse, String> {
            fetchBaseRequestInfoV5(connection)
            url {
                parameters["list"] = ruleType.value
                parameters["sub"] = rule
            }
        }
    }
}
