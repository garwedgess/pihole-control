package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.extensions.fetchBaseRequestInfo
import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.PiHoleDiagnosisMessagesResponseData
import eu.wedgess.piholecontrol.data.utils.requestResult
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod

class DiagnosisApiServiceImpl(
    @DefaultHttpClient private val defaultHttpClient: HttpClient,
    @TrustAllCertificatesHttpClient private val trustAllCertsHttpClient: HttpClient
) : DiagnosisApiService {

    override suspend fun fetchDiagnosisMessages(
        connection: ConnectionEntity
    ): PiHoleApiResult<PiHoleDiagnosisMessagesResponseData> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfo(connection, path = DIAGNOSIS_MESSAGES_ENDPOINT)
        }
    }

    override suspend fun dismissDiagnosisMessages(
        connection: ConnectionEntity,
        ids: List<Int>
    ): PiHoleApiResult<Unit> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfo(
                connection,
                path = "$DIAGNOSIS_MESSAGES_ENDPOINT/${ids.joinToString(",")}"
            )
            method = HttpMethod.Delete
        }
    }

    companion object {
        private const val DIAGNOSIS_MESSAGES_ENDPOINT = "/info/messages"
    }
}
