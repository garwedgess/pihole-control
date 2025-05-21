package eu.wedgess.piholecontrol.data.repository

import eu.wedgess.piholecontrol.data.api.LogsApiService
import eu.wedgess.piholecontrol.data.mappers.DEFAULT_SUGGESTION_ENTRY
import eu.wedgess.piholecontrol.data.mappers.toEntity
import eu.wedgess.piholecontrol.data.mappers.toLogEntryEntity
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.LogFilterSuggestionsEntity
import eu.wedgess.piholecontrol.domain.model.PiHoleLogsEntity
import eu.wedgess.piholecontrol.domain.repository.LogsRepository
import eu.wedgess.piholecontrol.presentation.logs.model.LogEntryStatus
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import kotlinx.coroutines.withContext

class LogsRepositoryImpl(
    private val logsApiService: LogsApiService,
    private val dispatcherProvider: DispatcherProvider
) : LogsRepository {

    override suspend fun fetchLogs(
        connection: ConnectionEntity,
        limit: Int,
        status: LogEntryStatus,
        query: String,
        clientIp: String?,
        clientName: String?,
        queryType: String?,
        advancedStatus: String?,
        from: Long?,
        until: Long?
    ): Result<List<PiHoleLogsEntity>> = withContext(dispatcherProvider.io) {
        logsApiService.fetchLogs(
            connection,
            limit,
            domain = query.takeIf { it.isNotBlank() },
            clientIp = clientIp.takeIf { it != DEFAULT_SUGGESTION_ENTRY },
            clientName = clientName.takeIf { it != DEFAULT_SUGGESTION_ENTRY },
            queryType = queryType.takeIf { it != DEFAULT_SUGGESTION_ENTRY },
            advancedStatus = advancedStatus.takeIf { it != DEFAULT_SUGGESTION_ENTRY },
            from,
            until
        ).mapCatching { response ->
            val result = response.queries.map { it.toLogEntryEntity() }
            filterByStatus(result, status)
        }
    }

    override suspend fun fetchLogFilterSuggestions(
        connection: ConnectionEntity
    ): Result<LogFilterSuggestionsEntity> = withContext(dispatcherProvider.io) {
        logsApiService.fetchLogFilterSuggestions(connection).mapCatching { it.toEntity() }
    }

    private fun filterByStatus(
        logs: List<PiHoleLogsEntity>,
        status: LogEntryStatus
    ): List<PiHoleLogsEntity> = when (status) {
        LogEntryStatus.ALL -> logs
        else -> logs.filter { status.categories.contains(it.status.category) }
    }
}
