package eu.wedgess.piholecontrol.domain.usecase.connections

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.usecases.connections.UpdateConnectionUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateConnectionUseCaseTest {

    @MockK
    private lateinit var repository: ConnectionRepository
    private lateinit var updateConnectionUseCase: UpdateConnectionUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        updateConnectionUseCase = UpdateConnectionUseCase(repository)
    }

    @Test
    fun `invoke - updates connection and returns success`() = runTest {
        val connectionInfo = ConnectionEntity.default
        coEvery { repository.update(connectionInfo) } returns Result.success(Unit)

        val result = updateConnectionUseCase(connectionInfo)

        assertThat(result.isSuccess).isTrue()
        coVerify { repository.update(connectionInfo) }
    }

    @Test
    fun `invoke - returns failure when repository fails`() = runTest {
        val connectionInfo = ConnectionEntity.default
        coEvery { repository.update(connectionInfo) } returns Result.failure(Exception("Update failed"))

        val result = updateConnectionUseCase(connectionInfo)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(Exception::class.java)
    }
}
