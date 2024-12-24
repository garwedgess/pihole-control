package eu.wedgess.piholecontrol.domain.usecase.connections

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.usecases.connections.DeleteConnectionMarkedForDeletionUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteConnectionMarkedForDeletionUseCaseTest {

    @MockK
    private lateinit var repository: ConnectionRepository
    private lateinit var target: DeleteConnectionMarkedForDeletionUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = DeleteConnectionMarkedForDeletionUseCase(repository)
    }

    @Test
    fun `invoke - deletes connection and returns success`() = runTest {
        coEvery { repository.deleteAllMarkedForDeletion() } returns Result.success(Unit)

        val result = target()

        assertThat(result.isSuccess).isTrue()
        coVerify { repository.deleteAllMarkedForDeletion() }
    }

    @Test
    fun `invoke - returns failure when repository fails`() = runTest {
        coEvery {
            repository.deleteAllMarkedForDeletion()
        } returns Result.failure(Exception("Delete failed"))

        val result = target()

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(Exception::class.java)
    }
}
