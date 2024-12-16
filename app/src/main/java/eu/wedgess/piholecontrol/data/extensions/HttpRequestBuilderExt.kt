package eu.wedgess.piholecontrol.data.extensions

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.BasicAuthProvider
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.encodedPath

suspend fun HttpRequestBuilder.fetchBaseRequestInfo(
    activePiHole: ConnectionEntity
): HttpRequestBuilder {
    url {
        protocol = activePiHole.protocol
        host = activePiHole.host
        encodedPath = activePiHole.apiPath
        port = activePiHole.port
        activePiHole.token.takeIf { it.isNotBlank() }?.run {
            parameters["auth"] = this@run
        }
    }
    if (activePiHole.hasAuthCredentials) {
        val basicAuthProvider = BasicAuthProvider(
            credentials = {
                BasicAuthCredentials(
                    username = activePiHole.authUsername,
                    password = activePiHole.authPassword
                )
            },
            realm = activePiHole.authRealm.ifBlank { null },
            sendWithoutRequestCallback = { true }
        )
        basicAuthProvider.addRequestHeaders(this)
    }
    return this
}
