package eu.wedgess.piholecontrol.domain.usecases.connections

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.UUID

class FetchConnectionByIdUseCaseTest {

    @MockK
    private lateinit var repository: ConnectionRepository
    private lateinit var fetchConnectionByIdUseCase: FetchConnectionByIdUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        fetchConnectionByIdUseCase = FetchConnectionByIdUseCase(repository)
    }

    @Test
    fun `invoke - fetches connection by id and returns success`() = runTest {
        val connection = ConnectionEntity.Version5.default
        val connectionId = connection.id

        coEvery { repository.fetchById(connectionId) } returns Result.success(connection)

        val result = fetchConnectionByIdUseCase(connectionId)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(connection)
        coVerify { repository.fetchById(connectionId) }
    }

    @Test
    fun `invoke - returns failure when repository fetch fails`() = runTest {
        val connectionId = UUID.randomUUID()
        val exception = Exception("Failed to fetch connection")

        coEvery { repository.fetchById(connectionId) } returns Result.failure(exception)

        val result = fetchConnectionByIdUseCase(connectionId)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { repository.fetchById(connectionId) }
    }
}
