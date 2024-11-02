package eu.wedgess.piholecontrol.data.api

import androidx.annotation.VisibleForTesting
import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.data.model.enums.FilterRuleType
import eu.wedgess.piholecontrol.data.model.responses.ModifyFilterRuleResponse
import eu.wedgess.piholecontrol.data.model.responses.PiHoleClientsOverTimeData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleFilterRules
import eu.wedgess.piholecontrol.data.model.responses.PiHoleForwardDestinations
import eu.wedgess.piholecontrol.data.model.responses.PiHoleLogsResponse
import eu.wedgess.piholecontrol.data.model.responses.PiHoleOverTimeData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleQueryTypes
import eu.wedgess.piholecontrol.data.model.responses.PiHoleStatusResponse
import eu.wedgess.piholecontrol.data.model.responses.PiHoleSummary
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopClients
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopQueries
import eu.wedgess.piholecontrol.data.utils.requestResult
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.BasicAuthProvider
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.encodedPath
import javax.inject.Inject
import kotlin.time.Duration

class PiHoleApiImpl @Inject constructor(
    @DefaultHttpClient private val defaultHttpClient: HttpClient,
    @TrustAllCertificatesHttpClient private val trustAllCertsHttpClient: HttpClient
) : PiHoleApi {

    @VisibleForTesting
    suspend fun HttpRequestBuilder.fetchBaseRequestInfo(
        activePiHole: ConnectionInfo
    ): HttpRequestBuilder {
        url {
            protocol = activePiHole.protocol
            host = activePiHole.host
            encodedPath = activePiHole.apiPath
            port = activePiHole.port
            activePiHole.token.takeIf { it.isNotBlank() }?.run {
                parameters["auth"] = this@run
            }
        }
        if (activePiHole.hasAuthCredentials) {
            val basicAuthProvider = BasicAuthProvider(
                credentials = {
                    BasicAuthCredentials(
                        username = activePiHole.authUsername,
                        password = activePiHole.authPassword
                    )
                },
                realm = activePiHole.authRealm.ifBlank { null },
                sendWithoutRequestCallback = { true }
            )
            basicAuthProvider.addRequestHeaders(this)
        }
        return this
    }

    override suspend fun fetchStatus(activeMiHole: ConnectionInfo): Result<PiHoleStatusResponse> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleStatusResponse, String> {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["status"] = true.toString()
            }
        }
    }

    override suspend fun fetchStatusSummary(activeMiHole: ConnectionInfo): Result<PiHoleSummary> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleSummary, String> {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["summaryRaw"] = true.toString()
            }
        }
    }

    override suspend fun fetchOverTimeData10Minutes(activeMiHole: ConnectionInfo): Result<PiHoleOverTimeData> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleOverTimeData, String> {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["overTimeData10mins"] = true.toString()
            }
        }
    }

    override suspend fun fetchOverTimeDataClients(activeMiHole: ConnectionInfo): Result<PiHoleClientsOverTimeData> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleClientsOverTimeData, String> {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["overTimeDataClients"] = true.toString()
                parameters["getClientNames"] = true.toString()
            }
        }
    }

    override suspend fun fetchQueryTypes(activeMiHole: ConnectionInfo): Result<PiHoleQueryTypes> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleQueryTypes, String> {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["getQueryTypes"] = true.toString()
            }
        }
    }

    override suspend fun fetchForwardDestinations(activeMiHole: ConnectionInfo): Result<PiHoleForwardDestinations> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleForwardDestinations, String> {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["getForwardDestinations"] = true.toString()
            }
        }
    }

    override suspend fun fetchTopQueries(activeMiHole: ConnectionInfo): Result<PiHoleTopQueries> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleTopQueries, String> {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["topItems"] = true.toString()
            }
        }
    }

    override suspend fun fetchTopClients(activeMiHole: ConnectionInfo): Result<PiHoleTopClients> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleTopClients, String> {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["topClients"] = true.toString()
            }
        }
    }

    override suspend fun fetchFilterRules(
        activeMiHole: ConnectionInfo,
        ruleType: FilterRuleType
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
        ruleType: FilterRuleType
    ): Result<ModifyFilterRuleResponse> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<ModifyFilterRuleResponse, String> {
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
        ruleType: FilterRuleType
    ): Result<ModifyFilterRuleResponse> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<ModifyFilterRuleResponse, String> {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["list"] = ruleType.value
                parameters["sub"] = rule
            }
        }
    }

    override suspend fun fetchLogs(
        activeMiHole: ConnectionInfo,
        limit: Int
    ): Result<PiHoleLogsResponse> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleLogsResponse, String> {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["getAllQueries"] = limit.toString()
            }
        }
    }

    override suspend fun enableAdBlocking(activeMiHole: ConnectionInfo): Result<PiHoleStatusResponse> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleStatusResponse, String> {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["enable"] = true.toString()
            }
        }
    }

    override suspend fun disableAdBlocking(
        activeMiHole: ConnectionInfo,
        duration: Duration
    ): Result<PiHoleStatusResponse> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleStatusResponse, String> {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["disable"] =
                    (if (duration.isInfinite()) 0 else duration.inWholeSeconds).run { toString() }
            }
        }
    }
}
