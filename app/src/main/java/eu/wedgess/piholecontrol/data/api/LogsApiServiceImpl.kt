package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.extensions.fetchBaseRequestInfo
import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.PiHoleLogSuggestionsResponseData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleLogsResponseData
import eu.wedgess.piholecontrol.data.utils.requestResult
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import javax.inject.Inject

class LogsApiServiceImpl @Inject constructor(
    @DefaultHttpClient private val defaultHttpClient: HttpClient,
    @TrustAllCertificatesHttpClient private val trustAllCertsHttpClient: HttpClient
) : LogsApiService {

    override suspend fun fetchLogs(
        connection: ConnectionEntity,
        limit: Int,
        domain: String?,
        clientIp: String?,
        clientName: String?,
        queryType: String?,
        advancedStatus: String?,
        from: Long?,
        until: Long?
    ): PiHoleApiResult<PiHoleLogsResponseData> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfo(connection, path = QUERIES_ENDPOINT) {
                url {
                    parameters.append(PARAM_LENGTH, limit.toString())
                    domain?.let { parameters.append(PARAM_DOMAIN, "*$it*") }
                    clientIp?.let { parameters.append(PARAM_CLIENT_IP, it) }
                    clientName?.let { parameters.append(PARAM_CLIENT_NAME, it) }
                    advancedStatus?.let { parameters.append(PARAM_STATUS, it) }
                    queryType?.let { parameters.append(PARAM_TYPE, it) }
                    from?.let { parameters.append(PARAM_FROM, it.toString()) }
                    until?.let { parameters.append(PARAM_UNTIL, it.toString()) }
                }
            }
        }
    }

    override suspend fun fetchLogFilterSuggestions(
        connection: ConnectionEntity
    ): PiHoleApiResult<PiHoleLogSuggestionsResponseData> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfo(connection, path = QUERIE_SUGGESTIONS_ENDPOINT)
        }
    }

    companion object {
        private const val QUERIE_SUGGESTIONS_ENDPOINT = "/queries/suggestions"
        private const val QUERIES_ENDPOINT = "/queries"
        private const val PARAM_LENGTH = "length"
        private const val PARAM_STATUS = "status"
        private const val PARAM_DOMAIN = "domain"
        private const val PARAM_CLIENT_IP = "client_ip"
        private const val PARAM_CLIENT_NAME = "client_name"
        private const val PARAM_TYPE = "type"
        private const val PARAM_FROM = "from"
        private const val PARAM_UNTIL = "until"
    }
}
