package eu.wedgess.piholecontrol.data.repository

import eu.wedgess.piholecontrol.data.api.v5.LogsApiServiceV5
import eu.wedgess.piholecontrol.data.api.v6.LogsApiServiceV6
import eu.wedgess.piholecontrol.data.mappers.toLogEntryEntity
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleLogsResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleLogsResponseDataV6
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.PiHoleLogsEntity
import eu.wedgess.piholecontrol.domain.repository.LogsRepository
import eu.wedgess.piholecontrol.presentation.logs.model.LogEntryStatus
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import kotlinx.coroutines.withContext

class LogsRepositoryImpl(
    private val apiV5: LogsApiServiceV5,
    private val apiV6: LogsApiServiceV6,
    private val dispatcherProvider: DispatcherProvider
) : LogsRepository {

    override suspend fun fetchLogs(
        connection: ConnectionEntity,
        limit: Int,
        status: LogEntryStatus,
        query: String,
        from: Long?,
        until: Long?
    ): Result<List<PiHoleLogsEntity>> = withContext(dispatcherProvider.io) {
        callVersionedEndpoint(
            activeConnection = connection,
            v5Call = { apiV5.fetchLogs(it, limit) },
            v6Call = { apiV6.fetchLogs(it, limit, from, until) },
            mapper = { response ->
                val result = when (response) {
                    is PiHoleLogsResponseDataV5 -> response.data.map { it.toLogEntryEntity() }
                    is PiHoleLogsResponseDataV6 -> response.queries.map { it.toLogEntryEntity() }
                    else -> throw IllegalArgumentException(
                        "Unknown type: ${response.javaClass.name}"
                    )
                }
                filterByStatus(result, status)
                    .run { filterByQuery(this@run, query) }
                    .run { filterByDateRange(this@run, from, until) }
            }
        )
    }

    private fun filterByStatus(
        logs: List<PiHoleLogsEntity>,
        status: LogEntryStatus
    ): List<PiHoleLogsEntity> = when (status) {
        LogEntryStatus.ALL -> logs
        else -> {
            logs.filter {
                when (it) {
                    is PiHoleLogsEntity.Version5 -> {
                        status.categories.contains(it.answerType.category)
                    }
                    is PiHoleLogsEntity.Version6 -> {
                        status.categories.contains(it.status.category)
                    }
                }
            }
        }
    }

    private fun filterByQuery(logs: List<PiHoleLogsEntity>, query: String): List<PiHoleLogsEntity> =
        if (query.isBlank()) {
            logs
        } else {
            logs.filter {
                it.domain.contains(query, ignoreCase = true)
            }
        }

    private fun filterByDateRange(
        logs: List<PiHoleLogsEntity>,
        from: Long?,
        until: Long?
    ): List<PiHoleLogsEntity> = logs.filter { log ->
        (from == null || log.timestamp >= from) &&
                (until == null || log.timestamp <= until)
    }
}
