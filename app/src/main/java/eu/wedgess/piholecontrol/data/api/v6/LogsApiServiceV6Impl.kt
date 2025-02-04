package eu.wedgess.piholecontrol.data.api.v6

import eu.wedgess.piholecontrol.data.extensions.fetchBaseRequestInfoV6
import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleLogSuggestionsResponseDataV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleLogsResponseDataV6
import eu.wedgess.piholecontrol.data.utils.requestResult
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import javax.inject.Inject

class LogsApiServiceV6Impl @Inject constructor(
    @DefaultHttpClient private val defaultHttpClient: HttpClient,
    @TrustAllCertificatesHttpClient private val trustAllCertsHttpClient: HttpClient
) : LogsApiServiceV6 {

    override suspend fun fetchLogs(
        connection: ConnectionEntity.Version6,
        limit: Int,
        domain: String?,
        clientIp: String?,
        clientName: String?,
        queryType: String?,
        advancedStatus: String?,
        from: Long?,
        until: Long?
    ): PiHoleApiResult<PiHoleLogsResponseDataV6> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfoV6(connection, path = QUERIES_ENDPOINT) {
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
        connection: ConnectionEntity.Version6
    ): PiHoleApiResult<PiHoleLogSuggestionsResponseDataV6> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfoV6(connection, path = QUERIE_SUGGESTIONS_ENDPOINT)
        }
    }

    companion object {
        private const val QUERIE_SUGGESTIONS_ENDPOINT = "/api/queries/suggestions"
        private const val QUERIES_ENDPOINT = "/api/queries"
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
