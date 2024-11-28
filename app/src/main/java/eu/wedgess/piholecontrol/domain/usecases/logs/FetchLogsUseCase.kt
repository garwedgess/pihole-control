package eu.wedgess.piholecontrol.domain.usecases.logs

import eu.wedgess.piholecontrol.domain.model.LogEntryEntity
import eu.wedgess.piholecontrol.domain.repository.LogsRepository
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.presentation.logs.model.LogEntryStatus
import kotlinx.coroutines.flow.Flow

class FetchLogsUseCase(
    private val logsRepository: LogsRepository,
    private val periodicRefreshUseCase: PeriodicRefreshUseCase
) {

    private var limit: Int = 500
    private var statusFilter: LogEntryStatus = LogEntryStatus.ALL

    operator fun invoke(): Flow<Result<List<LogEntryEntity>>> {
        return periodicRefreshUseCase { connection ->
            logsRepository.fetchLogs(connection, limit).mapCatching {
                it.applyStatusFilter()
            }

        }
    }

    fun setLimit(limit: Int) {
        this.limit = limit
        periodicRefreshUseCase.triggerRefresh()
    }

    fun setStatusFilter(statusFilter: LogEntryStatus) {
        this.statusFilter = statusFilter
        periodicRefreshUseCase.triggerRefresh()
    }

    private fun List<LogEntryEntity>.applyStatusFilter(): List<LogEntryEntity> {
        return when (statusFilter) {
            LogEntryStatus.ALL -> this
            LogEntryStatus.ALLOWED,
            LogEntryStatus.BLOCKED ->
                filter { statusFilter.categories.contains(it.answerType.category) }
        }
    }
}