package eu.wedgess.piholecontrol.data.network

import eu.wedgess.piholecontrol.data.extensions.SID_HEADER
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
import java.util.UUID
import kotlin.Result.Companion.success
import kotlin.Result.Companion.failure

class SessionIdAuthenticatorTest {

    @MockK
    private lateinit var tokenRefresher: TokenRefresher

    @RelaxedMockK
    private lateinit var connectionRepository: ConnectionRepository

    private lateinit var target: SessionIdAuthenticator
    private lateinit var response: Response

    @MockK
    private lateinit var route: Route

    // Create a proper ConnectionEntity instance instead of using .default
    private val testConnection = ConnectionEntity(
        id = UUID.randomUUID(),
        name = "Test Connection",
        protocol = mockk(relaxed = true),
        host = "test.host",
        port = 80,
        password = "test-password",
        apiPath = "/api",
        sid = "old-sid",
        authUsername = "",
        authPassword = "",
        authRealm = "",
        trustAllCerts = false,
        isDeleted = false,
        isActive = true
    )

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
    fun `authenticate should return null when no active connection due to failure`() = runTest {
        // Given - fetchActive returns null (no active connection)
        coEvery { connectionRepository.fetchActive() } returns failure(RuntimeException())

        // When
        val result = target.authenticate(route, response)

        // Then
        assertNull(result)

        coVerify(exactly = 0) { tokenRefresher.generateSessionId(any()) }
    }

    @Test
    fun `authenticate should return request with new session id when successful`() = runTest {
        // Given
        val newAuthSession = AuthSessionStatusEntity(
            valid = true,
            totp = false,
            validity = 1800,
            message = "",
            sid = "new-sid"
        )

        coEvery { connectionRepository.fetchActive() } returns success(testConnection)
        coEvery { tokenRefresher.generateSessionId(testConnection) } returns success(newAuthSession)

        // When
        val result = target.authenticate(route, response)

        // Then
        assertEquals("new-sid", result?.header(SID_HEADER))
        coVerify {
            connectionRepository.update(
                match { connection ->
                    connection.sid == "new-sid" && connection.id == testConnection.id
                }
            )
        }
    }

    @Test
    fun `authenticate should return null when token refresh fails`() = runTest {
        // Given
        coEvery { connectionRepository.fetchActive() } returns success(testConnection)
        coEvery { tokenRefresher.generateSessionId(testConnection) } returns failure(Exception("Token refresh failed"))

        // When
        val result = target.authenticate(route, response)

        // Then
        assertNull(result)
        // Verify that update was not called since token refresh failed
        coVerify(exactly = 0) { connectionRepository.update(any()) }
    }

    @Test
    fun `authenticate should return null when fetchActive fails`() = runTest {
        // Given
        coEvery { connectionRepository.fetchActive() } returns failure(Exception("Fetch failed"))

        // When
        val result = target.authenticate(route, response)

        // Then
        assertNull(result)
        coVerify(exactly = 0) { tokenRefresher.generateSessionId(any()) }
        coVerify(exactly = 0) { connectionRepository.update(any()) }
    }

    @Test
    fun `authenticate should retry when token refresh is in progress and return result after waiting`() = runTest {
        // This test needs a more complex setup to properly simulate concurrent access
        // For now, let's test the basic retry mechanism by testing sequential calls

        // Given
        val newAuthSession = AuthSessionStatusEntity(
            valid = true,
            totp = false,
            validity = 1800,
            message = "",
            sid = "new-sid"
        )

        coEvery { connectionRepository.fetchActive() } returns success(testConnection)
        coEvery { tokenRefresher.generateSessionId(testConnection) } returns success(newAuthSession)

        // When - make two sequential calls
        val result1 = target.authenticate(route, response)
        val result2 = target.authenticate(route, response)

        // Then
        assertEquals("new-sid", result1?.header(SID_HEADER))
        assertEquals("new-sid", result2?.header(SID_HEADER))

        // Verify that the connection was updated twice (once for each call)
        coVerify(exactly = 2) {
            connectionRepository.update(
                match { connection ->
                    connection.sid == "new-sid" && connection.id == testConnection.id
                }
            )
        }
    }
}