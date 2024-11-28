package eu.wedgess.piholecontrol.domain.repository

import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.domain.model.LogEntryEntity

interface LogsRepository {
    suspend fun fetchLogs(
        activeConnection: ConnectionInfo,
        limit: Int
    ): Result<List<LogEntryEntity>>
}