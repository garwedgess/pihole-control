package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.extensions.fetchBaseRequestInfo
import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.PiHoleLocalDnsResponseData
import eu.wedgess.piholecontrol.data.utils.requestResult
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import io.ktor.http.encodeURLPath

class LocalDnsApiServiceImpl(
    @DefaultHttpClient private val defaultHttpClient: HttpClient,
    @TrustAllCertificatesHttpClient private val trustAllCertsHttpClient: HttpClient
) : LocalDnsApiService {

    override suspend fun fetchLocalDnsRecords(
        connection: ConnectionEntity
    ): PiHoleApiResult<PiHoleLocalDnsResponseData> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfo(connection, path = LOCAL_DNS_HOSTS_ENDPOINT)
        }
    }

    override suspend fun addLocalDnsRecord(
        connection: ConnectionEntity,
        value: String
    ): PiHoleApiResult<Unit> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfo(
                connection,
                path = "$LOCAL_DNS_HOSTS_MODIFY_ENDPOINT/${value.encodeURLPath()}"
            )
            method = HttpMethod.Put
        }
    }

    override suspend fun deleteLocalDnsRecord(
        connection: ConnectionEntity,
        value: String
    ): PiHoleApiResult<Unit> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfo(
                connection,
                path = "$LOCAL_DNS_HOSTS_MODIFY_ENDPOINT/${value.encodeURLPath()}"
            )
            method = HttpMethod.Delete
        }
    }

    companion object {
        private const val LOCAL_DNS_HOSTS_ENDPOINT = "/config/dns/hosts"
        private const val LOCAL_DNS_HOSTS_MODIFY_ENDPOINT = "/config/dns%2Fhosts"
    }
}
