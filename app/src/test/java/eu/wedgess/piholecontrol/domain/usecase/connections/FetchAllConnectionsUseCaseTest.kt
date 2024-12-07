package eu.wedgess.piholecontrol.domain.usecase.connections

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.usecases.connections.FetchAllConnectionsUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FetchAllConnectionsUseCaseTest {

    @MockK
    private lateinit var repository: ConnectionRepository
    private lateinit var fetchAllConnectionsUseCase: FetchAllConnectionsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        fetchAllConnectionsUseCase = FetchAllConnectionsUseCase(repository)
    }

    @Test
    fun `invoke - fetches all connections and returns success`() = runTest {
        val connections = listOf(ConnectionEntity.default, ConnectionEntity.default.copy(id = 2L))

        coEvery { repository.fetchAll() } returns kotlinx.coroutines.flow.flowOf(
            Result.success(
                connections
            )
        )

        val result = fetchAllConnectionsUseCase().toList()

        assertThat(result).hasSize(1)
        assertThat(result.first().isSuccess).isTrue()
        assertThat(result.first().getOrNull()).isEqualTo(connections)
        coVerify { repository.fetchAll() }
    }

    @Test
    fun `invoke - returns failure when repository fetch fails`() = runTest {
        val exception = Exception("Failed to fetch connections")
        coEvery { repository.fetchAll() } returns kotlinx.coroutines.flow.flowOf(
            Result.failure(
                exception
            )
        )

        val result = fetchAllConnectionsUseCase().toList()

        assertThat(result).hasSize(1)
        assertThat(result.first().isFailure).isTrue()
        assertThat(result.first().exceptionOrNull()).isEqualTo(exception)
        coVerify { repository.fetchAll() }
    }
}
