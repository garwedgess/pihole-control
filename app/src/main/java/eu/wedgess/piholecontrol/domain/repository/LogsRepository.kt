package eu.wedgess.piholecontrol.domain.repository

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.LogEntryEntity

interface LogsRepository {
    suspend fun fetchLogs(
        activeConnection: ConnectionEntity,
        limit: Int
    ): Result<List<LogEntryEntity>>
}