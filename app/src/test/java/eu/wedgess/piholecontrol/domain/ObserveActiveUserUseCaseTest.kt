package eu.wedgess.piholecontrol.domain

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.usecases.ObserveActiveUserUseCase
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ObserveActiveUserUseCaseTest {

    @MockK
    private lateinit var repository: ConnectionRepository
    private lateinit var target: ObserveActiveUserUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        target = ObserveActiveUserUseCase(repository)
    }

    @Test
    fun `invoke - emits active connection successfully`() = runTest {
        val activeConnection = ConnectionEntity.default
        val expectedResult = Result.success(activeConnection)

        every { repository.fetchActiveFlow() } returns flowOf(expectedResult)

        val results = target().toList()

        assertThat(results).containsExactly(expectedResult)
        coVerify { repository.fetchActiveFlow() }
    }

    @Test
    fun `invoke - emits error when repository returns failure`() = runTest {
        val expectedError = Result.failure<ConnectionEntity>(Exception("No active connection"))

        every { repository.fetchActiveFlow() } returns flowOf(expectedError)

        val results = target().toList()

        assertThat(results).containsExactly(expectedError)
        coVerify { repository.fetchActiveFlow() }
    }
}