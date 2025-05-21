package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.extensions.fetchBaseRequestInfo
import eu.wedgess.piholecontrol.data.model.requests.PiHoleGroupRequestData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.PiHoleGroupsResponseData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleModifyGroupResponseData
import eu.wedgess.piholecontrol.data.utils.requestResult
import eu.wedgess.piholecontrol.di.annotations.AuthHttpClient
import eu.wedgess.piholecontrol.di.annotations.AuthTrustAllCertificatesHttpClient
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import javax.inject.Inject

class GroupApiServiceImpl @Inject constructor(
    @AuthHttpClient private val defaultHttpClient: HttpClient,
    @AuthTrustAllCertificatesHttpClient private val trustAllCertsHttpClient: HttpClient
) : GroupApiService {

    override suspend fun fetchAllGroups(
        connection: ConnectionEntity
    ): PiHoleApiResult<PiHoleGroupsResponseData> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfo(connection, path = GROUPS_ENDPOINT)
        }
    }

    override suspend fun updateGroup(
        connection: ConnectionEntity,
        name: String,
        groupRequestData: PiHoleGroupRequestData
    ): PiHoleApiResult<PiHoleModifyGroupResponseData> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfo(connection, path = "${GROUPS_ENDPOINT}/$name")
            url {
                method = HttpMethod.Post
                contentType(ContentType.Application.Json)
                setBody(groupRequestData)
            }
        }
    }

    override suspend fun deleteGroup(
        connection: ConnectionEntity,
        name: String
    ): PiHoleApiResult<Unit> {
        val client = if (connection.trustAllCerts) trustAllCertsHttpClient else defaultHttpClient
        return client.requestResult {
            fetchBaseRequestInfo(connection, path = "${GROUPS_ENDPOINT}/$name")
            url {
                method = HttpMethod.Delete
            }
        }
    }

    companion object {
        private const val GROUPS_ENDPOINT = "/groups"
    }
}
