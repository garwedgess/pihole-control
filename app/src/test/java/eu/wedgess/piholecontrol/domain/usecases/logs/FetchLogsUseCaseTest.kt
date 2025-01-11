package eu.wedgess.piholecontrol.domain.usecases.logs

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.LogEntryEntity
import eu.wedgess.piholecontrol.domain.model.PiHoleLogsEntity
import eu.wedgess.piholecontrol.domain.repository.LogsRepository
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.presentation.logs.model.LogEntryStatus
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FetchLogsUseCaseTest {

    @MockK
    private lateinit var logsRepository: LogsRepository

    @MockK
    private lateinit var periodicRefreshUseCase: PeriodicRefreshUseCase
    private lateinit var target: FetchLogsUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        target = FetchLogsUseCase(logsRepository, periodicRefreshUseCase)
    }

    @Test
    fun `invoke - emits logs successfully`() = runTest {
        val connection = ConnectionEntity.Version5.default
        val logEntries = listOf<PiHoleLogsEntity>(
            mockk(relaxed = true),
            mockk(relaxed = true)
        )
        val expectedResult = Result.success(logEntries)

        coEvery { logsRepository.fetchLogs(connection, any(), any(), any(), any(), any()) } returns
                Result.success(logEntries)

        coEvery { periodicRefreshUseCase<List<LogEntryEntity>>(any()) } answers {
            val fetchData = arg<suspend (ConnectionEntity) -> Result<List<LogEntryEntity>>>(0)
            flow {
                emit(fetchData(connection))
            }
        }

        target(
            limit = 50,
            status = LogEntryStatus.ALL,
            query = "",
            from = null,
            until = null
        ).test {
            assertThat(awaitItem()).isEqualTo(expectedResult)
            awaitComplete()
        }
        coVerify { logsRepository.fetchLogs(connection, 50, LogEntryStatus.ALL, "", null, null) }
    }

    @Test
    fun `invoke - emits error when repository returns failure`() = runTest {
        val connection = ConnectionEntity.Version5.default
        val expectedError = Result.failure<List<PiHoleLogsEntity>>(Exception("Error fetching logs"))

        coEvery {
            logsRepository.fetchLogs(
                connection,
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns expectedError

        coEvery { periodicRefreshUseCase<List<LogEntryEntity>>(any()) } answers {
            val fetchData = arg<suspend (ConnectionEntity) -> Result<List<LogEntryEntity>>>(0)
            flow {
                emit(fetchData(connection))
            }
        }

        target(
            limit = 50,
            status = LogEntryStatus.ALL,
            query = "",
            from = null,
            until = null
        ).test {
            assertThat(awaitItem()).isEqualTo(expectedError)
            awaitComplete()
        }
        coVerify { logsRepository.fetchLogs(connection, 50, LogEntryStatus.ALL, "", null, null) }
    }
}
