package eu.wedgess.piholecontrol.domain

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.usecases.ObserveActiveUserUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ObserveActiveUserUseCaseTest {

    @MockK
    private lateinit var repository: ConnectionRepository
    private lateinit var target: ObserveActiveUserUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = ObserveActiveUserUseCase(repository)
    }

    @Test
    fun `invoke - emits active connection successfully`() = runTest {
        val activeConnection = ConnectionEntity.default
        val expectedResult = Result.success(activeConnection)

        every { repository.fetchActiveFlow() } returns flowOf(expectedResult)

        target().test {
            assertThat(awaitItem()).isEqualTo(expectedResult)
            awaitComplete()
        }
    }

    @Test
    fun `invoke - emits error when repository returns failure`() = runTest {
        val expectedError = Result.failure<ConnectionEntity>(Exception("No active connection"))

        every { repository.fetchActiveFlow() } returns flowOf(expectedError)

        target().test {
            assertThat(awaitItem()).isEqualTo(expectedError)
            awaitComplete()
        }
    }
}
