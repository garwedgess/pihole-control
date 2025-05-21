package eu.wedgess.piholecontrol.data.extensions

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.BasicAuthProvider
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.http.encodedPath

const val SID_HEADER = "sid"

suspend inline fun HttpRequestBuilder.fetchBaseRequestInfo(
    activeConnection: ConnectionEntity,
    path: String,
    noinline configureRequest: (HttpRequestBuilder.() -> Unit)? = null
): HttpRequestBuilder {
    url {
        protocol = activeConnection.protocol
        host = activeConnection.host
        encodedPath = activeConnection.apiPath + path
        port = activeConnection.port
    }
    header(SID_HEADER, activeConnection.sid)
    if (activeConnection.hasAuthCredentials) {
        val basicAuthProvider = BasicAuthProvider(
            credentials = {
                BasicAuthCredentials(
                    username = activeConnection.authUsername,
                    password = activeConnection.authPassword
                )
            },
            realm = activeConnection.authRealm.ifBlank { null },
            sendWithoutRequestCallback = { true }
        )
        basicAuthProvider.addRequestHeaders(this)
    }
    configureRequest?.invoke(this)
    return this
}
