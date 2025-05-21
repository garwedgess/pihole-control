package eu.wedgess.piholecontrol.domain.usecases.auth

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.AuthSessionStatusEntity
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.AuthRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ValidateSessionIdUseCaseTest {

    @MockK
    private lateinit var authRepository: AuthRepository
    private lateinit var useCase: ValidateSessionIdUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        authRepository = mockk()
        useCase = ValidateSessionIdUseCase(authRepository)
    }

    @Test
    fun `invoke should return repository validation result`() = runTest {
        // Given
        val connection = ConnectionEntity.default
        val expectedResponse = AuthSessionStatusEntity(
            valid = true,
            totp = false,
            sid = "abscdef",
            validity = 1800,
            message = "valid"
        )

        coEvery { authRepository.validateSessionId(connection) } returns Result.success(
            expectedResponse
        )

        // When
        val result = useCase(connection)

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(expectedResponse)
    }

    @Test
    fun `invoke should return false when validation fails`() = runTest {
        // Given
        val connection = ConnectionEntity.default

        coEvery { authRepository.validateSessionId(connection) } returns Result.failure(
            RuntimeException("Error")
        )

        // When
        val result = useCase(connection)

        // Then
        assertThat(result.isFailure).isTrue()
    }
}
