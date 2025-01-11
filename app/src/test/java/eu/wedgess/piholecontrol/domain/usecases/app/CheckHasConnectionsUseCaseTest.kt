package eu.wedgess.piholecontrol.domain.usecases.app

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CheckHasConnectionsUseCaseTest {

    @MockK
    private lateinit var connectionRepository: ConnectionRepository

    private lateinit var checkHasConnectionsUseCase: CheckHasConnectionsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        checkHasConnectionsUseCase = CheckHasConnectionsUseCase(connectionRepository)
    }

    @Test
    fun `invoke returns true when repository indicates connections exist`() = runTest {
        coEvery { connectionRepository.checkHasConnections() } returns Result.success(true)

        val result = checkHasConnectionsUseCase()

        assertThat(result).isEqualTo(Result.success(true))
        coVerify { connectionRepository.checkHasConnections() }
    }

    @Test
    fun `invoke returns false when repository indicates no connections exist`() = runTest {
        coEvery { connectionRepository.checkHasConnections() } returns Result.success(false)

        val result = checkHasConnectionsUseCase()

        assertThat(result).isEqualTo(Result.success(false))
        coVerify { connectionRepository.checkHasConnections() }
    }

    @Test
    fun `invoke returns failure when repository throws exception`() = runTest {
        val exception = Exception("Repository error")
        coEvery { connectionRepository.checkHasConnections() } returns Result.failure(exception)

        val result = checkHasConnectionsUseCase()

        assertThat(result).isEqualTo(Result.failure<Boolean>(exception))
        coVerify { connectionRepository.checkHasConnections() }
    }
}
