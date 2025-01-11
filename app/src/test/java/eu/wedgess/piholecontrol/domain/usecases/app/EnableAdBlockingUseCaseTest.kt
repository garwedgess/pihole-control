package eu.wedgess.piholecontrol.domain.usecases.app

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.StatusEntity
import eu.wedgess.piholecontrol.domain.repository.StatusRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class EnableAdBlockingUseCaseTest {

    @MockK
    private lateinit var repository: StatusRepository
    private lateinit var enableAdBlockingUseCase: EnableAdBlockingUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        enableAdBlockingUseCase = EnableAdBlockingUseCase(repository)
    }

    @Test
    fun `invoke - enables ad blocking and returns success`() = runTest {
        val connection = ConnectionEntity.Version5.default
        val status = StatusEntity.ENABLED

        coEvery { repository.enableAdBlocking(connection) } returns Result.success(status)

        val result = enableAdBlockingUseCase(connection)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(status)
        coVerify { repository.enableAdBlocking(connection) }
    }

    @Test
    fun `invoke - returns failure when repository fails to enable ad blocking`() = runTest {
        val connection = ConnectionEntity.Version5.default
        val exception = Exception("Failed to enable ad blocking")

        coEvery { repository.enableAdBlocking(connection) } returns Result.failure(exception)

        val result = enableAdBlockingUseCase(connection)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { repository.enableAdBlocking(connection) }
    }
}
