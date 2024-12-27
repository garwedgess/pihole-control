package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.extensions.fetchBaseRequestInfo
import eu.wedgess.piholecontrol.data.model.responses.PiHoleForwardDestinations
import eu.wedgess.piholecontrol.data.model.responses.PiHoleQueryTypes
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopClients
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopQueries
import eu.wedgess.piholecontrol.data.utils.requestResult
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import javax.inject.Inject

class StatisticsApiServiceImpl @Inject constructor(
    @DefaultHttpClient private val defaultHttpClient: HttpClient,
    @TrustAllCertificatesHttpClient private val trustAllCertsHttpClient: HttpClient
) : StatisticsApiService {

    override suspend fun fetchQueryTypes(activeMiHole: ConnectionEntity): Result<PiHoleQueryTypes> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleQueryTypes, String> {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["getQueryTypes"] = true.toString()
            }
        }
    }

    override suspend fun fetchForwardDestinations(
        activeMiHole: ConnectionEntity
    ): Result<PiHoleForwardDestinations> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleForwardDestinations, String> {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["getForwardDestinations"] = true.toString()
            }
        }
    }

    override suspend fun fetchTopQueries(activeMiHole: ConnectionEntity): Result<PiHoleTopQueries> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleTopQueries, String> {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["topItems"] = true.toString()
            }
        }
    }

    override suspend fun fetchTopClients(activeMiHole: ConnectionEntity): Result<PiHoleTopClients> {
        val client = if (activeMiHole.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult<PiHoleTopClients, String> {
            fetchBaseRequestInfo(activeMiHole)
            url {
                parameters["topClients"] = true.toString()
            }
        }
    }
}
