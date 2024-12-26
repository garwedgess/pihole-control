package eu.wedgess.piholecontrol.domain.usecase.connections

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.usecases.connections.MarkConnectionForDeletionUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class MarkConnectionForDeletionUseCaseTest {

    @MockK
    private lateinit var repository: ConnectionRepository
    private lateinit var target: MarkConnectionForDeletionUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = MarkConnectionForDeletionUseCase(repository)
    }

    @Test
    fun `invoke - marks connection for deletion and returns success`() = runTest {
        val connectionId = 123L
        coEvery { repository.markForDeletion(connectionId) } returns Result.success(Unit)

        val result = target(connectionId)

        assertThat(result.isSuccess).isTrue()
        coVerify { repository.markForDeletion(connectionId) }
    }

    @Test
    fun `invoke - returns failure when repository fails`() = runTest {
        val connectionId = 123L
        val exception = Exception("Failed to mark connection for deletion")
        coEvery { repository.markForDeletion(connectionId) } returns Result.failure(exception)

        val result = target(connectionId)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { repository.markForDeletion(connectionId) }
    }
}
