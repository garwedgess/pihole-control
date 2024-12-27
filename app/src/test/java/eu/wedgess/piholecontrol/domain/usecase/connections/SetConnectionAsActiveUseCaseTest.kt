package eu.wedgess.piholecontrol.domain.usecase.connections

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.usecases.connections.SetConnectionAsActiveUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SetConnectionAsActiveUseCaseTest {

    @MockK
    private lateinit var repository: ConnectionRepository
    private lateinit var setConnectionAsActiveUseCase: SetConnectionAsActiveUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        setConnectionAsActiveUseCase = SetConnectionAsActiveUseCase(repository)
    }

    @Test
    fun `invoke - sets connection as active and returns success`() = runTest {
        val connectionId = 123L

        coEvery { repository.setActiveById(connectionId) } returns Result.success(Unit)

        val result = setConnectionAsActiveUseCase(connectionId)

        assertThat(result.isSuccess).isTrue()
        coVerify { repository.setActiveById(connectionId) }
    }

    @Test
    fun `invoke - returns failure when repository fails`() = runTest {
        val connectionId = 123L
        val exception = Exception("Failed to set connection as active")
        coEvery { repository.setActiveById(connectionId) } returns
                Result.failure(exception)

        val result = setConnectionAsActiveUseCase(connectionId)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { repository.setActiveById(connectionId) }
    }
}
