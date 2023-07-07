package eu.wedgess.mihole.data.api

import eu.wedgess.mihole.data.model.MiHolesInfo
import eu.wedgess.mihole.data.model.PiHoleApiResponse
import eu.wedgess.mihole.data.model.PiHoleClientsOverTimeData
import eu.wedgess.mihole.data.model.PiHoleOverTimeData
import eu.wedgess.mihole.data.model.PiHoleStatistics
import eu.wedgess.mihole.data.model.PiHoleStatusResponse
import eu.wedgess.mihole.data.model.PiHoleSummary
import eu.wedgess.mihole.data.utils.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.BasicAuthProvider
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.encodedPath
import javax.inject.Inject
import kotlin.time.Duration

class PiHoleApiImpl @Inject constructor(
    private val httpClient: HttpClient
) : PiHoleApi {

    private suspend fun HttpRequestBuilder.fetchBaseRequestInfo(activeMiHole: MiHolesInfo): HttpRequestBuilder {
        url {
            protocol = activeMiHole.protocol
            host = activeMiHole.host
            encodedPath = activeMiHole.apiPath
            port = activeMiHole.port
            activeMiHole.token?.run {
                parameters["auth"] = this@run
            }
        }
        if (activeMiHole.authUsername.isNotBlank() && activeMiHole.authPassword.isNotBlank()) {
            val basicAuthProvider = BasicAuthProvider(
                credentials = {
                    BasicAuthCredentials(
                        username = activeMiHole.authUsername,
                        password = activeMiHole.authPassword
                    )
                },
                realm = activeMiHole.authRealm.ifBlank { null },
                sendWithoutRequestCallback = { true }
            )
            basicAuthProvider.addRequestHeaders(this)
        }
        return this
    }

    override suspend fun fetchStatusSummary(activeMiHole: MiHolesInfo): PiHoleApiResponse<PiHoleSummary> {
        return httpClient.safeRequest {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["summaryRaw"] = true.toString()
            }
        }
    }

    override suspend fun fetchOverTimeData10Minutes(activeMiHole: MiHolesInfo): PiHoleApiResponse<PiHoleOverTimeData> {
        return httpClient.safeRequest {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["overTimeData10mins"] = true.toString()
            }
        }
    }

    override suspend fun fetchOverTimeDataClients(activeMiHole: MiHolesInfo): PiHoleApiResponse<PiHoleClientsOverTimeData> {
        return httpClient.safeRequest {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["overTimeDataClients"] = true.toString()
                parameters["getClientNames"] = true.toString()
            }
        }
    }

    override suspend fun fetchStatistics(activeMiHole: MiHolesInfo): PiHoleApiResponse<PiHoleStatistics> {
        return httpClient.safeRequest {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["getQueryTypes"] = true.toString()
                parameters["getForwardDestinations"] = true.toString()
                parameters["topItems"] = true.toString()
                parameters["topClients"] = true.toString()
            }
        }
    }

    override suspend fun enableAdBlocking(activeMiHole: MiHolesInfo): PiHoleApiResponse<PiHoleStatusResponse> {
        return httpClient.safeRequest {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["enable"] = true.toString()
            }
        }
    }

    override suspend fun disableAdBlocking(
        activeMiHole: MiHolesInfo,
        duration: Duration
    ): PiHoleApiResponse<PiHoleStatusResponse> {
        return httpClient.safeRequest {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["disable"] =
                    if (duration.isInfinite()) 0.toString() else duration.inWholeSeconds.toString()
            }
        }
    }


}