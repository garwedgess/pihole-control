package eu.wedgess.piholecontrol.domain.usecases.logs

import eu.wedgess.piholecontrol.domain.model.PiHoleLogsEntity
import eu.wedgess.piholecontrol.domain.model.RefreshMode
import eu.wedgess.piholecontrol.domain.repository.LogsRepository
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.presentation.logs.model.LogEntryStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchLogsUseCase @Inject constructor(
    private val logsRepository: LogsRepository,
    private val periodicRefreshUseCase: PeriodicRefreshUseCase
) {

    operator fun invoke(
        limit: Int,
        status: LogEntryStatus,
        query: String,
        from: Long?,
        until: Long?
    ): Flow<Result<List<PiHoleLogsEntity>>> {
        return periodicRefreshUseCase { connection ->
            logsRepository.fetchLogs(connection, limit, status, query, from, until)
        }
    }

    fun refresh() = periodicRefreshUseCase.triggerRefresh()

    fun setRefreshMode(refreshMode: RefreshMode) =
        periodicRefreshUseCase.setRefreshMode(refreshMode)
}
