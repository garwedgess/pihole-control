package eu.wedgess.piholecontrol.data.api.v5

import eu.wedgess.piholecontrol.data.extensions.fetchBaseRequestInfoV5
import eu.wedgess.piholecontrol.data.model.enums.PiHoleFilterRuleType
import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleCombinedFilterRulesResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleFilterRulesResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleModifyFilterRuleResponseDataV5
import eu.wedgess.piholecontrol.data.utils.requestResult
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

class FilterRulesApiServiceV5Impl @Inject constructor(
    @DefaultHttpClient private val defaultHttpClient: HttpClient,
    @TrustAllCertificatesHttpClient private val trustAllCertsHttpClient: HttpClient
) : FilterRulesApiServiceV5 {

    private suspend fun fetchFilterRules(
        connection: ConnectionEntity.Version5,
        ruleType: PiHoleFilterRuleType
    ): PiHoleApiResult<PiHoleFilterRulesResponseDataV5> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfoV5(connection)
            url {
                parameters["list"] = ruleType.value
            }
        }
    }

    override suspend fun fetchCombinedFilterRules(
        connection: ConnectionEntity.Version5,
        ruleType: PiHoleFilterRuleType
    ): PiHoleApiResult<PiHoleCombinedFilterRulesResponseDataV5> {
        return supervisorScope {
            val (rule, regexRule) = when (ruleType) {
                PiHoleFilterRuleType.ALLOW,
                PiHoleFilterRuleType.REGEX_ALLOW -> Pair(
                    PiHoleFilterRuleType.ALLOW,
                    PiHoleFilterRuleType.REGEX_ALLOW
                )

                PiHoleFilterRuleType.DENY,
                PiHoleFilterRuleType.REGEX_DENY -> Pair(
                    PiHoleFilterRuleType.DENY,
                    PiHoleFilterRuleType.REGEX_DENY
                )
            }

            val deferredRuleType =
                async { fetchFilterRules(connection, rule) }
            val deferredRegexRuleType =
                async { fetchFilterRules(connection, regexRule) }

            val ruleTypeResult = deferredRuleType.await()
            val regexRuleTypeResult = deferredRegexRuleType.await()

            ruleTypeResult.fold(
                onSuccess = { rules ->
                    regexRuleTypeResult.fold(
                        onSuccess = { regexRules ->
                            PiHoleApiResult.success(
                                PiHoleCombinedFilterRulesResponseDataV5(
                                    rules = rules.rulesList,
                                    regexRules = regexRules.rulesList
                                )
                            )
                        },
                        onFailure = { blockedError ->
                            PiHoleApiResult.failure(blockedError)
                        }
                    )
                },
                onFailure = { permittedError ->
                    PiHoleApiResult.failure(permittedError)
                }
            )
        }
    }

    override suspend fun addFilterRule(
        connection: ConnectionEntity.Version5,
        rule: String,
        ruleType: PiHoleFilterRuleType
    ): PiHoleApiResult<PiHoleModifyFilterRuleResponseDataV5> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfoV5(connection)
            url {
                parameters["list"] = ruleType.value
                parameters["add"] = rule
            }
        }
    }

    override suspend fun removeFilterRule(
        connection: ConnectionEntity.Version5,
        rule: String,
        ruleType: PiHoleFilterRuleType
    ): PiHoleApiResult<PiHoleModifyFilterRuleResponseDataV5> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfoV5(connection)
            url {
                parameters["list"] = ruleType.value
                parameters["sub"] = rule
            }
        }
    }
}
