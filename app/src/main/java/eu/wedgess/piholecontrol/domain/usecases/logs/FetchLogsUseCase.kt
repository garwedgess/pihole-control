package eu.wedgess.piholecontrol.domain.usecases.logs

import androidx.annotation.VisibleForTesting
import eu.wedgess.piholecontrol.domain.model.LogEntryEntity
import eu.wedgess.piholecontrol.domain.repository.LogsRepository
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.presentation.logs.model.LogEntryStatus
import kotlinx.coroutines.flow.Flow

class FetchLogsUseCase(
    private val logsRepository: LogsRepository,
    private val periodicRefreshUseCase: PeriodicRefreshUseCase
) {

    @VisibleForTesting
    var logLimit: Int = 500

    @VisibleForTesting
    var logStatusFilter: LogEntryStatus = LogEntryStatus.ALL

    operator fun invoke(): Flow<Result<List<LogEntryEntity>>> {
        return periodicRefreshUseCase { connection ->
            logsRepository.fetchLogs(connection, logLimit).mapCatching {
                it.applyStatusFilter()
            }
        }
    }

    fun setLogLimit(limit: Int): Boolean {
        this.logLimit = limit
        return periodicRefreshUseCase.triggerRefresh()
    }

    fun setLogStatusFilter(statusFilter: LogEntryStatus): Boolean {
        this.logStatusFilter = statusFilter
        return periodicRefreshUseCase.triggerRefresh()
    }

    private fun List<LogEntryEntity>.applyStatusFilter(): List<LogEntryEntity> {
        return when (logStatusFilter) {
            LogEntryStatus.ALL -> this
            LogEntryStatus.ALLOWED,
            LogEntryStatus.BLOCKED ->
                filter { logStatusFilter.categories.contains(it.answerType.category) }
        }
    }
}
