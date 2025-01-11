package eu.wedgess.piholecontrol.data.api.v6

import eu.wedgess.piholecontrol.data.extensions.fetchBaseRequestInfoV6
import eu.wedgess.piholecontrol.data.model.enums.TopQueriesTypeV6
import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleQueryTypesResponseDataV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleTopClientsCombinedResponseDataV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleTopClientsResponseDataV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleTopQueriesCombinedResponseV6Data
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleTopQueriesResponseDataV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleUpstreamsResponseDataV6
import eu.wedgess.piholecontrol.data.utils.requestResult
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

class StatisticsApiServiceV6Impl @Inject constructor(
    @DefaultHttpClient private val defaultHttpClient: HttpClient,
    @TrustAllCertificatesHttpClient private val trustAllCertsHttpClient: HttpClient
) : StatisticsApiServiceV6 {

    override suspend fun fetchQueryTypes(
        connection: ConnectionEntity.Version6
    ): PiHoleApiResult<PiHoleQueryTypesResponseDataV6> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfoV6(connection, path = QUERY_TYPES_ENDPOINT)
        }
    }

    override suspend fun fetchUpstreams(
        connection: ConnectionEntity.Version6
    ): PiHoleApiResult<PiHoleUpstreamsResponseDataV6> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfoV6(connection, path = UPSTREAM_ENDPOINT)
        }
    }

    override suspend fun fetchTopCombinedQueries(
        connection: ConnectionEntity.Version6
    ): PiHoleApiResult<PiHoleTopQueriesCombinedResponseV6Data> {
        return supervisorScope {
            val permittedDeferred =
                async { fetchTopQueries(connection, type = TopQueriesTypeV6.Permitted) }
            val blockedDeferred =
                async { fetchTopQueries(connection, type = TopQueriesTypeV6.Blocked) }
            val permittedResult = permittedDeferred.await()
            val blockedResult = blockedDeferred.await()

            permittedResult.fold(
                onSuccess = { permitted ->
                    blockedResult.fold(
                        onSuccess = { blocked ->
                            PiHoleApiResult.success(
                                PiHoleTopQueriesCombinedResponseV6Data(
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
        connection: ConnectionEntity.Version6,
        type: TopQueriesTypeV6
    ): PiHoleApiResult<PiHoleTopQueriesResponseDataV6> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfoV6(connection, path = TOP_DOMAINS_ENDPOINT)
            if (type == TopQueriesTypeV6.Blocked) {
                parameter("blocked", true)
            }
        }
    }

    override suspend fun fetchTopCombinedClients(
        connection: ConnectionEntity.Version6
    ): PiHoleApiResult<PiHoleTopClientsCombinedResponseDataV6> {
        return supervisorScope {
            val permittedDeferred =
                async { fetchTopClients(connection, type = TopQueriesTypeV6.Permitted) }
            val blockedDeferred =
                async { fetchTopClients(connection, type = TopQueriesTypeV6.Blocked) }
            val permittedResult = permittedDeferred.await()
            val blockedResult = blockedDeferred.await()

            permittedResult.fold(
                onSuccess = { permitted ->
                    blockedResult.fold(
                        onSuccess = { blocked ->
                            PiHoleApiResult.success(
                                PiHoleTopClientsCombinedResponseDataV6(
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
        connection: ConnectionEntity.Version6,
        type: TopQueriesTypeV6
    ): PiHoleApiResult<PiHoleTopClientsResponseDataV6> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfoV6(connection, path = TOP_CLIENTS_ENDPOINT)
            if (type == TopQueriesTypeV6.Blocked) {
                parameter("blocked", true)
            }
        }
    }

    companion object {
        private const val QUERY_TYPES_ENDPOINT = "/api/stats/query_types"
        private const val UPSTREAM_ENDPOINT = "/api/stats/upstreams"
        private const val TOP_DOMAINS_ENDPOINT = "/api/stats/top_domains"
        private const val TOP_CLIENTS_ENDPOINT = "/api/stats/top_clients"
    }
}
