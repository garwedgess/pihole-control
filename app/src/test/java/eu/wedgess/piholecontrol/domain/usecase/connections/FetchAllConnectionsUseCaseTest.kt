package eu.wedgess.piholecontrol.domain.usecase.connections

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.usecases.connections.FetchAllConnectionsUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
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

        coEvery { repository.fetchAll() } returns flowOf(
            Result.success(
                connections
            )
        )

        fetchAllConnectionsUseCase().test {
            val result = awaitItem()
            assertThat(result.isSuccess).isTrue()
            assertThat(result.getOrNull()).isEqualTo(connections)
            awaitComplete()
        }
        coVerify { repository.fetchAll() }
    }

    @Test
    fun `invoke - returns failure when repository fetch fails`() = runTest {
        val exception = Exception("Failed to fetch connections")
        coEvery { repository.fetchAll() } returns flowOf(
            Result.failure(
                exception
            )
        )

        fetchAllConnectionsUseCase().test {
            val result = awaitItem()
            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isEqualTo(exception)
            awaitComplete()
        }
        coVerify { repository.fetchAll() }
    }
}
