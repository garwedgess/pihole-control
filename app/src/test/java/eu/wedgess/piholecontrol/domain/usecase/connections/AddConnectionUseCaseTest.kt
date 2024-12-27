package eu.wedgess.piholecontrol.domain.usecase.connections

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.usecases.connections.AddConnectionUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AddConnectionUseCaseTest {

    @MockK
    private lateinit var repository: ConnectionRepository
    private lateinit var addConnectionUseCase: AddConnectionUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        addConnectionUseCase = AddConnectionUseCase(repository)
    }

    @Test
    fun `invoke - inserts connection and returns success`() = runTest {
        val connectionInfo = ConnectionEntity.default
        coEvery { repository.insert(connectionInfo) } returns Result.success(Unit)

        val result = addConnectionUseCase(connectionInfo)

        assertThat(result.isSuccess).isTrue()
        coVerify { repository.insert(connectionInfo) }
    }

    @Test
    fun `invoke - returns failure when repository fails`() = runTest {
        val connectionInfo = ConnectionEntity.default
        val exception = Exception("Insert failed")
        coEvery { repository.insert(connectionInfo) } returns Result.failure(exception)

        val result = addConnectionUseCase(connectionInfo)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { repository.insert(connectionInfo) }
    }
}
