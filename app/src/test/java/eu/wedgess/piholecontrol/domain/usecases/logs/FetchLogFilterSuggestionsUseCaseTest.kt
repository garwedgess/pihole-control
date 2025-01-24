package eu.wedgess.piholecontrol.domain.usecases.logs

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.LogFilterSuggestionsEntity
import eu.wedgess.piholecontrol.domain.repository.LogsRepository
import eu.wedgess.piholecontrol.domain.usecases.ObserveActiveUserUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FetchLogFilterSuggestionsUseCaseTest {

    @MockK
    private lateinit var logsRepository: LogsRepository

    @MockK
    private lateinit var observeActiveUser: ObserveActiveUserUseCase
    private lateinit var target: FetchLogFilterSuggestionsUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        target = FetchLogFilterSuggestionsUseCase(logsRepository, observeActiveUser)
    }

    @Test
    fun `invoke - emits log filter suggestions successfully`() = runTest {
        val connection = ConnectionEntity.Version5.default
        val expectedSuggestions = LogFilterSuggestionsEntity(
            clientIpAddresses = listOf("192.168.1.22", "192.168.1.70"),
            clientNames = listOf("client1", "client2"),
            queryTypes = listOf("A", "AAAA"),
            domains = listOf("example.com", "test.org"),
            upstreams = emptyList(),
            statuses = emptyList(),
            dnsSecs = emptyList(),
            replyTypes = emptyList()
        )

        coEvery { observeActiveUser() } returns flow {
            emit(Result.success(connection))
        }

        coEvery {
            logsRepository.fetchLogFilterSuggestions(connection)
        } returns Result.success(expectedSuggestions)

        target().test {
            assertThat(awaitItem()).isEqualTo(Result.success(expectedSuggestions))
            awaitComplete()
        }
    }

    @Test
    fun `invoke - emits error when repository returns failure`() = runTest {
        val connection = ConnectionEntity.Version5.default
        val expectedError = Result.failure<LogFilterSuggestionsEntity>(
            Exception("Error fetching log filter suggestions")
        )

        coEvery { observeActiveUser() } returns flow {
            emit(Result.success(connection))
        }

        coEvery {
            logsRepository.fetchLogFilterSuggestions(connection)
        } returns expectedError

        target().test {
            assertThat(awaitItem()).isEqualTo(expectedError)
            awaitComplete()
        }
    }
}
