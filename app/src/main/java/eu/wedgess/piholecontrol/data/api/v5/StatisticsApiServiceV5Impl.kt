package eu.wedgess.piholecontrol.data.api.v5

import eu.wedgess.piholecontrol.data.extensions.fetchBaseRequestInfoV5
import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleQueryTypesResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleTopClientsResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleTopQueriesResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleUpstreamsResponseDataV5
import eu.wedgess.piholecontrol.data.utils.requestResult
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import javax.inject.Inject

class StatisticsApiServiceV5Impl @Inject constructor(
    @DefaultHttpClient private val defaultHttpClient: HttpClient,
    @TrustAllCertificatesHttpClient private val trustAllCertsHttpClient: HttpClient
) : StatisticsApiServiceV5 {

    override suspend fun fetchQueryTypes(
        connection: ConnectionEntity.Version5
    ): PiHoleApiResult<PiHoleQueryTypesResponseDataV5> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfoV5(connection)
            url {
                parameters["getQueryTypes"] = true.toString()
            }
        }
    }

    override suspend fun fetchUpstreams(
        connection: ConnectionEntity.Version5
    ): PiHoleApiResult<PiHoleUpstreamsResponseDataV5> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfoV5(connection)
            url {
                parameters["getForwardDestinations"] = true.toString()
            }
        }
    }

    override suspend fun fetchTopQueries(
        connection: ConnectionEntity.Version5
    ): PiHoleApiResult<PiHoleTopQueriesResponseDataV5> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfoV5(connection)
            url {
                parameters["topItems"] = true.toString()
            }
        }
    }

    override suspend fun fetchTopClients(
        connection: ConnectionEntity.Version5
    ): PiHoleApiResult<PiHoleTopClientsResponseDataV5> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfoV5(connection)
            url {
                parameters["topClients"] = true.toString()
                parameters["topClientsBlocked"] = true.toString()
            }
        }
    }
}
