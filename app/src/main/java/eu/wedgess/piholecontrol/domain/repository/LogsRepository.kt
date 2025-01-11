package eu.wedgess.piholecontrol.domain.repository

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.PiHoleLogsEntity
import eu.wedgess.piholecontrol.presentation.logs.model.LogEntryStatus

interface LogsRepository {
    suspend fun fetchLogs(
        connection: ConnectionEntity,
        limit: Int,
        status: LogEntryStatus,
        query: String,
        from: Long?,
        until: Long?
    ): Result<List<PiHoleLogsEntity>>
}
