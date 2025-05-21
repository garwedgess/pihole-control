package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.extensions.fetchBaseRequestInfo
import eu.wedgess.piholecontrol.data.model.enums.TopQueriesType
import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.PiHoleQueryTypesResponseData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopClientsCombinedResponseData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopClientsResponseData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopQueriesCombinedResponseData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopQueriesResponseData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleUpstreamsResponseData
import eu.wedgess.piholecontrol.data.utils.requestResult
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

class StatisticsApiServiceImpl @Inject constructor(
    @DefaultHttpClient private val defaultHttpClient: HttpClient,
    @TrustAllCertificatesHttpClient private val trustAllCertsHttpClient: HttpClient
) : StatisticsApiService {

    override suspend fun fetchQueryTypes(
        connection: ConnectionEntity
    ): PiHoleApiResult<PiHoleQueryTypesResponseData> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfo(connection, path = QUERY_TYPES_ENDPOINT)
        }
    }

    override suspend fun fetchUpstreams(
        connection: ConnectionEntity
    ): PiHoleApiResult<PiHoleUpstreamsResponseData> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfo(connection, path = UPSTREAM_ENDPOINT)
        }
    }

    override suspend fun fetchTopCombinedQueries(
        connection: ConnectionEntity
    ): PiHoleApiResult<PiHoleTopQueriesCombinedResponseData> {
        return supervisorScope {
            val permittedDeferred =
                async { fetchTopQueries(connection, type = TopQueriesType.Permitted) }
            val blockedDeferred =
                async { fetchTopQueries(connection, type = TopQueriesType.Blocked) }
            val permittedResult = permittedDeferred.await()
            val blockedResult = blockedDeferred.await()

            permittedResult.fold(
                onSuccess = { permitted ->
                    blockedResult.fold(
                        onSuccess = { blocked ->
                            PiHoleApiResult.success(
                                PiHoleTopQueriesCombinedResponseData(
                                    permitted = permitted.domains,
                                    blocked = blocked.domains
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

    private suspend fun fetchTopQueries(
        connection: ConnectionEntity,
        type: TopQueriesType
    ): PiHoleApiResult<PiHoleTopQueriesResponseData> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfo(connection, path = TOP_DOMAINS_ENDPOINT)
            if (type == TopQueriesType.Blocked) {
                parameter("blocked", true)
            }
        }
    }

    override suspend fun fetchTopCombinedClients(
        connection: ConnectionEntity
    ): PiHoleApiResult<PiHoleTopClientsCombinedResponseData> {
        return supervisorScope {
            val permittedDeferred =
                async { fetchTopClients(connection, type = TopQueriesType.Permitted) }
            val blockedDeferred =
                async { fetchTopClients(connection, type = TopQueriesType.Blocked) }
            val permittedResult = permittedDeferred.await()
            val blockedResult = blockedDeferred.await()

            permittedResult.fold(
                onSuccess = { permitted ->
                    blockedResult.fold(
                        onSuccess = { blocked ->
                            PiHoleApiResult.success(
                                PiHoleTopClientsCombinedResponseData(
                                    all = permitted.clients,
                                    blocked = blocked.clients
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

    private suspend fun fetchTopClients(
        connection: ConnectionEntity,
        type: TopQueriesType
    ): PiHoleApiResult<PiHoleTopClientsResponseData> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfo(connection, path = TOP_CLIENTS_ENDPOINT)
            if (type == TopQueriesType.Blocked) {
                parameter("blocked", true)
            }
        }
    }

    companion object {
        private const val QUERY_TYPES_ENDPOINT = "/stats/query_types"
        private const val UPSTREAM_ENDPOINT = "/stats/upstreams"
        private const val TOP_DOMAINS_ENDPOINT = "/stats/top_domains"
        private const val TOP_CLIENTS_ENDPOINT = "/stats/top_clients"
    }
}
