package eu.wedgess.piholecontrol.domain.usecase.app

import app.cash.turbine.test
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
import kotlinx.coroutines.flow.flow
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
        val status = StatusEntity.ENABLED

        coEvery { periodicRefreshUseCase<StatusEntity>(any()) } answers {
            flow {
                val fetchBlock = arg<suspend (ConnectionEntity) -> Result<StatusEntity>>(0)
                emit(fetchBlock(connection))
            }
        }
        coEvery { repository.fetchStatus(connection) } returns Result.success(status)

        fetchStatusUseCase().test {
            val result = awaitItem()
            assertThat(result.isSuccess).isTrue()
            assertThat(result.getOrNull()).isEqualTo(status)
            awaitComplete()
        }
        coVerify { periodicRefreshUseCase<StatusEntity>(any()) }
        coVerify { repository.fetchStatus(connection) }
    }

    @Test
    fun `invoke - emits error when repository returns failure`() = runTest {
        val connection = ConnectionEntity.default
        val exception = Exception("Failed to fetch status")

        coEvery { periodicRefreshUseCase<StatusEntity>(any()) } answers {
            flow {
                val fetchBlock = arg<suspend (ConnectionEntity) -> Result<StatusEntity>>(0)
                emit(fetchBlock(connection))
            }
        }
        coEvery { repository.fetchStatus(connection) } returns Result.failure(exception)

        fetchStatusUseCase().test {
            val result = awaitItem()
            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isEqualTo(exception)
            awaitComplete()
        }
        coVerify { periodicRefreshUseCase<StatusEntity>(any()) }
        coVerify { repository.fetchStatus(connection) }
    }
}
