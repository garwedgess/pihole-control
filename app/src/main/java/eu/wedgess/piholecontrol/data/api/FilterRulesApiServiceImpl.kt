package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.extensions.fetchBaseRequestInfo
import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.data.model.enums.PiHoleFilterRuleType
import eu.wedgess.piholecontrol.data.model.responses.PiHoleModifyFilterRuleResponse
import eu.wedgess.piholecontrol.data.model.responses.PiHoleFilterRules
import eu.wedgess.piholecontrol.data.utils.requestResult
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import io.ktor.client.HttpClient
import javax.inject.Inject

class FilterRulesApiServiceImpl @Inject constructor(
    @DefaultHttpClient private val defaultHttpClient: HttpClient,
    @TrustAllCertificatesHttpClient private val trustAllCertsHttpClient: HttpClient
) : FilterRulesApiService {

    override suspend fun fetchFilterRules(
        activeMiHole: ConnectionInfo,
        ruleType: PiHoleFilterRuleType
    ): Result<PiHoleFilterRules> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleFilterRules, String> {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["list"] = ruleType.value
            }
        }
    }

    override suspend fun addFilterRule(
        activeMiHole: ConnectionInfo,
        rule: String,
        ruleType: PiHoleFilterRuleType
    ): Result<PiHoleModifyFilterRuleResponse> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleModifyFilterRuleResponse, String> {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["list"] = ruleType.value
                parameters["add"] = rule
            }
        }
    }

    override suspend fun removeFilterRule(
        activeMiHole: ConnectionInfo,
        rule: String,
        ruleType: PiHoleFilterRuleType
    ): Result<PiHoleModifyFilterRuleResponse> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleModifyFilterRuleResponse, String> {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["list"] = ruleType.value
                parameters["sub"] = rule
            }
        }
    }
}
