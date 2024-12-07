package eu.wedgess.piholecontrol.domain.usecase.app

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.StatusEntity
import eu.wedgess.piholecontrol.domain.repository.StatusRepository
import eu.wedgess.piholecontrol.domain.usecases.app.DisableAdBlockingUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class DisableAdBlockingUseCaseTest {

    @MockK
    private lateinit var repository: StatusRepository
    private lateinit var disableAdBlockingUseCase: DisableAdBlockingUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        disableAdBlockingUseCase = DisableAdBlockingUseCase(repository)
    }

    @Test
    fun `invoke - disables ad blocking and returns success`() = runTest {
        val connection = ConnectionEntity.default
        val status = mockk<StatusEntity>(relaxed = true)
        val duration: Duration = 30.minutes

        coEvery {
            repository.disableAdBlocking(
                connection,
                duration
            )
        } returns Result.success(status)

        val result = disableAdBlockingUseCase(connection, duration)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(status)

        coVerify { repository.disableAdBlocking(connection, duration) }
    }

    @Test
    fun `invoke - returns failure when repository fails to disable ad blocking`() = runTest {
        val connection = ConnectionEntity.default
        val exception = Exception("Failed to disable ad blocking")
        val duration: Duration = 30.minutes

        coEvery { repository.disableAdBlocking(connection, duration) } returns Result.failure(
            exception
        )

        val result = disableAdBlockingUseCase(connection, duration)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)

        coVerify { repository.disableAdBlocking(connection, duration) }
    }
}
