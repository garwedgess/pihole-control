package eu.wedgess.piholecontrol.data.network

import eu.wedgess.piholecontrol.data.repository.TokenRefresher
import eu.wedgess.piholecontrol.domain.model.AuthSessionStatusEntity
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import kotlin.Result.Companion.success

class SessionIdAuthenticatorTest {

    @MockK
    private lateinit var tokenRefresher: TokenRefresher

    @RelaxedMockK
    private lateinit var connectionRepository: ConnectionRepository

    private lateinit var target: SessionIdAuthenticator
    private lateinit var response: Response

    @MockK
    private lateinit var route: Route

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        target = SessionIdAuthenticator(
            tokenRefresher = tokenRefresher,
            connectionRepository = connectionRepository,
            retryDelayMs = 10, // Faster tests
            maxRetries = 2
        )

        response = mockk(relaxed = true) {
            every { request } returns Request.Builder().url("https://example.com").build()
        }
    }

    @Test
    fun `authenticate should return null when no active connection`() = runTest {
        // Given
        coEvery { connectionRepository.fetchActive() } returns success(ConnectionEntity.Version5.default)

        // When
        val result = target.authenticate(route, response)

        // Then
        assertNull(result)
    }

    @Test
    fun `authenticate should return null when connection is not Version6`() = runTest {
        // Given
        val connection = mockk<ConnectionEntity.Version5>()
        coEvery { connectionRepository.fetchActive() } returns success(connection)

        // When
        val result = target.authenticate(route, response)

        // Then
        assertNull(result)
    }

    @Test
    fun `authenticate should return request with new session id when successful`() = runTest {
        // Given
        val connection = ConnectionEntity.Version6.default
        val newAuthSession = AuthSessionStatusEntity(
            valid = true,
            totp = false,
            validity = 1800,
            message = "",
            sid = "new-sid"
        )

        coEvery { connectionRepository.fetchActive() } returns success(connection)
        coEvery { tokenRefresher.generateSessionId(connection) } returns success(newAuthSession)

        // When
        val result = target.authenticate(route, response)

        // Then
        assertEquals("new-sid", result?.header("sid"))
        coVerify {
            connectionRepository.update(
                match {
                    it is ConnectionEntity.Version6 && it.sid == "new-sid"
                }
            )
        }
    }

    @Test
    fun `authenticate should return null when token refresh fails`() = runTest {
        // Given
        val connection = ConnectionEntity.Version6.default

        coEvery { connectionRepository.fetchActive() } returns success(connection)
        coEvery { tokenRefresher.generateSessionId(connection) } returns Result.failure(Exception())

        // When
        val result = target.authenticate(route, response)

        // Then
        assertNull(result)
    }

    @Test
    fun `authenticate should retry when token refresh is in progress and return result after waiting`() =
        runTest {
            // Given
            val connection = ConnectionEntity.Version6.default
            val newAuthSession = AuthSessionStatusEntity(
                valid = true,
                totp = false,
                validity = 1800,
                message = "",
                sid = "new-sid"
            )

            // Simulate another thread already refreshing
            target.authenticate(route, response)

            coEvery { connectionRepository.fetchActive() } returns success(connection)
            coEvery { tokenRefresher.generateSessionId(connection) } returns success(newAuthSession)

            // When
            val result = target.authenticate(route, response)

            // Then
            assertEquals("new-sid", result?.header("sid"))
        }
}
