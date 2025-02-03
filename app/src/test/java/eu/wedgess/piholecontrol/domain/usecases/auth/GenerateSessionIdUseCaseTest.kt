package eu.wedgess.piholecontrol.domain.usecases.auth

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.AuthSessionStatusEntity
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.AuthRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GenerateSessionIdUseCaseTest {

    @MockK
    private lateinit var authRepository: AuthRepository
    private lateinit var target: GenerateSessionIdUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        target = GenerateSessionIdUseCase(authRepository)
    }

    @Test
    fun `invoke should return repository result when successful`() = runTest {
        // Given
        val connection = ConnectionEntity.Version6.default
        val expectedResponse = AuthSessionStatusEntity(
            valid = true,
            totp = false,
            sid = "abscdef",
            validity = 1800,
            message = "valid"
        )

        coEvery { authRepository.generateSessionId(connection) } returns Result.success(
            expectedResponse
        )

        // When
        val result = target(connection)

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(expectedResponse)
    }

    @Test
    fun `invoke should return null when session generation fails`() = runTest {
        // Given
        val connection = ConnectionEntity.Version6.default

        coEvery { authRepository.generateSessionId(connection) } returns Result.failure(
            RuntimeException("Error")
        )

        // When
        val result = target(connection)

        // Then
        assertThat(result.isFailure).isTrue()
    }
}
