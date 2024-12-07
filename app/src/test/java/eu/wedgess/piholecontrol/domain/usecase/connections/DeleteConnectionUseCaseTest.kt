package eu.wedgess.piholecontrol.domain.usecase.connections

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.usecases.connections.DeleteConnectionUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteConnectionUseCaseTest {

    @MockK
    private lateinit var repository: ConnectionRepository
    private lateinit var deleteConnectionUseCase: DeleteConnectionUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        deleteConnectionUseCase = DeleteConnectionUseCase(repository)
    }

    @Test
    fun `invoke - deletes connection and returns success`() = runTest {
        val connectionId = 123L
        coEvery { repository.deleteById(connectionId) } returns Result.success(Unit)

        val result = deleteConnectionUseCase(connectionId)

        assertThat(result.isSuccess).isTrue()
        coVerify { repository.deleteById(connectionId) }
    }

    @Test
    fun `invoke - returns failure when repository fails`() = runTest {
        val connectionId = 123L

        coEvery { repository.deleteById(connectionId) } returns Result.failure(Exception("Delete failed"))

        val result = deleteConnectionUseCase(connectionId)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(Exception::class.java)
    }
}
