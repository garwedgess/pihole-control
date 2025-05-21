package eu.wedgess.piholecontrol.data.network

import eu.wedgess.piholecontrol.data.extensions.SID_HEADER
import eu.wedgess.piholecontrol.data.repository.TokenRefresher
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class SessionIdAuthenticator @Inject constructor(
    private val tokenRefresher: TokenRefresher,
    private val connectionRepository: ConnectionRepository,
    private val retryDelayMs: Long = 100,
    private val maxRetries: Int = 3
) : Authenticator {

    private val tokenRefreshInProgress = AtomicBoolean(false)

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            var retryCount = 0
            while (tokenRefreshInProgress.get() && retryCount < maxRetries) {
                delay(retryDelayMs)
                retryCount++
            }

            if (tokenRefreshInProgress.get()) {
                return@runBlocking null // Give up after max retries
            }

            try {
                tokenRefreshInProgress.set(true)
                handleTokenRefresh(response.request.newBuilder())
            } finally {
                tokenRefreshInProgress.set(false)
            }
        }
    }

    private suspend fun handleTokenRefresh(requestBuilder: Request.Builder): Request? {
        val connection = connectionRepository.fetchActive().getOrNull()
            ?: return null

        val newAuthSession = tokenRefresher.generateSessionId(connection).getOrNull() ?: return null
        val newConnection = connection.copy(sid = newAuthSession.sid)
        connectionRepository.update(newConnection)

        return requestBuilder
            .header(SID_HEADER, newConnection.sid)
            .build()
    }
}
