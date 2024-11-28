package eu.wedgess.piholecontrol.data

import eu.wedgess.piholecontrol.data.api.LogsApiService
import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.domain.mappers.toLogEntryEntity
import eu.wedgess.piholecontrol.domain.model.LogEntryEntity
import eu.wedgess.piholecontrol.domain.repository.LogsRepository
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import kotlinx.coroutines.withContext

class LogsRepositoryImpl(
    private val apiService: LogsApiService,
    private val dispatcherProvider: DispatcherProvider
) : LogsRepository {

    override suspend fun fetchLogs(
        activeConnection: ConnectionInfo,
        limit: Int
    ): Result<List<LogEntryEntity>> = withContext(dispatcherProvider.io) {
        apiService.fetchLogs(activeConnection, limit).mapCatching { response ->
            response.data.map { it.toLogEntryEntity() }

        }
    }
}


