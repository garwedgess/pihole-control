package eu.wedgess.piholecontrol.domain.usecase.app

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.StatusEntity
import eu.wedgess.piholecontrol.domain.repository.StatusRepository
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.FetchStatusUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FetchStatusUseCaseTest {

    @MockK
    private lateinit var repository: StatusRepository

    @MockK
    private lateinit var periodicRefreshUseCase: PeriodicRefreshUseCase
    private lateinit var fetchStatusUseCase: FetchStatusUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        fetchStatusUseCase = FetchStatusUseCase(repository, periodicRefreshUseCase)
    }

    @Test
    fun `invoke - emits success when repository returns status`() = runTest {
        val connection = ConnectionEntity.default
        val status = mockk<StatusEntity>(relaxed = true)

        coEvery { periodicRefreshUseCase<Result<StatusEntity>>(any()) } answers {
            flow {
                val fetchBlock = arg<suspend (ConnectionEntity) -> Result<StatusEntity>>(0)
                emit(fetchBlock(connection))
            }
        }
        coEvery { repository.fetchStatus(connection) } returns Result.success(status)

        val result = fetchStatusUseCase().toList()

        assertThat(result).hasSize(1)
        assertThat(result.first().isSuccess).isTrue()
        assertThat(result.first().getOrNull()).isEqualTo(status)
        coVerify { periodicRefreshUseCase<Result<StatusEntity>>(any()) }
        coVerify { repository.fetchStatus(any()) }
    }

    @Test
    fun `invoke - emits error when repository returns failure`() = runTest {
        val connection = ConnectionEntity.default
        val exception = Exception("Failed to fetch status")

        coEvery { periodicRefreshUseCase<Result<StatusEntity>>(any()) } answers {
            flow {
                val fetchBlock = arg<suspend (ConnectionEntity) -> Result<StatusEntity>>(0)
                emit(fetchBlock(connection))
            }
        }
        coEvery { repository.fetchStatus(any()) } returns Result.failure(exception)

        val result = fetchStatusUseCase().toList()

        assertThat(result).hasSize(1)
        assertThat(result.first().isFailure).isTrue()
        assertThat(result.first().exceptionOrNull()).isEqualTo(exception)
        coVerify { periodicRefreshUseCase<Result<StatusEntity>>(any()) }
        coVerify { repository.fetchStatus(any()) }
    }
}
