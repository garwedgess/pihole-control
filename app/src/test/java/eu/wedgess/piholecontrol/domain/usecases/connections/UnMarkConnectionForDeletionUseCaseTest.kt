package eu.wedgess.piholecontrol.domain.usecases.connections

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.UUID

class UnMarkConnectionForDeletionUseCaseTest {

    @MockK
    private lateinit var repository: ConnectionRepository
    private lateinit var target: UnMarkConnectionForDeletionUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = UnMarkConnectionForDeletionUseCase(repository)
    }

    @Test
    fun `invoke - unmarks connection for deletion and returns success`() = runTest {
        val connectionId = UUID.randomUUID()
        coEvery { repository.unmarkForDeletion(connectionId) } returns Result.success(Unit)

        val result = target(connectionId)

        assertThat(result.isSuccess).isTrue()
        coVerify { repository.unmarkForDeletion(connectionId) }
    }

    @Test
    fun `invoke - returns failure when repository fails`() = runTest {
        val connectionId = UUID.randomUUID()
        val exception = Exception("Failed to unmark connection for deletion")
        coEvery { repository.unmarkForDeletion(connectionId) } returns Result.failure(exception)

        val result = target(connectionId)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { repository.unmarkForDeletion(connectionId) }
    }
}
