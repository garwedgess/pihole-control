package eu.wedgess.piholecontrol.domain.usecase.logs

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.LogEntryEntity
import eu.wedgess.piholecontrol.domain.repository.LogsRepository
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.domain.usecases.logs.FetchLogsUseCase
import eu.wedgess.piholecontrol.presentation.logs.model.LogEntryStatus
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
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
        val connection = ConnectionEntity.default
        val logEntries = listOf<LogEntryEntity>(
            mockk(relaxed = true),
            mockk(relaxed = true)
        )
        val expectedResult = Result.success(logEntries)

        coEvery { logsRepository.fetchLogs(connection, any()) } returns Result.success(logEntries)

        coEvery { periodicRefreshUseCase<Result<List<LogEntryEntity>>>(any()) } answers {
            val fetchData = arg<suspend (ConnectionEntity) -> Result<List<LogEntryEntity>>>(0)
            flow {
                emit(fetchData(connection))
            }
        }

        val results = target().take(1).toList()

        assertThat(results).containsExactly(expectedResult)
        coVerify { logsRepository.fetchLogs(connection, 500) }
    }

    @Test
    fun `invoke - emits error when repository returns failure`() = runTest {
        val connection = ConnectionEntity.default
        val expectedError = Result.failure<List<LogEntryEntity>>(Exception("Error fetching logs"))

        coEvery { logsRepository.fetchLogs(connection, any()) } returns expectedError

        coEvery { periodicRefreshUseCase<Result<List<LogEntryEntity>>>(any()) } answers {
            val fetchData = arg<suspend (ConnectionEntity) -> Result<List<LogEntryEntity>>>(0)
            flow {
                emit(fetchData(connection))
            }
        }

        val results = target().take(1).toList()

        assertThat(results).containsExactly(expectedError)
        coVerify { logsRepository.fetchLogs(connection, 500) }
    }

    @Test
    fun `setLimit - updates limit and triggers refresh`() {
        every { periodicRefreshUseCase.triggerRefresh() } returns true

        target.setLogLimit(1000)

        assertThat(target.logLimit).isEqualTo(1000)

        verify { periodicRefreshUseCase.triggerRefresh() }
    }

    @Test
    fun `setStatusFilter - updates filter and triggers refresh`() {
        every { periodicRefreshUseCase.triggerRefresh() } returns true

        target.setLogStatusFilter(LogEntryStatus.BLOCKED)

        assertThat(target.logStatusFilter).isEqualTo(LogEntryStatus.BLOCKED)

        verify { periodicRefreshUseCase.triggerRefresh() }
    }

    @Test
    fun `applyStatusFilter - filters logs based on status`() {
        val logEntries = listOf<LogEntryEntity>(
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true)
        )
        val filteredLogs = FetchLogsUseCase::class.java.getDeclaredMethod(
            "applyStatusFilter",
            List::class.java
        ).apply { isAccessible = true }
            .invoke(target, logEntries) as List<LogEntryEntity>

        assertThat(filteredLogs).hasSize(3)
    }
}
