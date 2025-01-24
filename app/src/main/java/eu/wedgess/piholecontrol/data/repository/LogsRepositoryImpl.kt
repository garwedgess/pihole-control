package eu.wedgess.piholecontrol.data.repository

import eu.wedgess.piholecontrol.data.api.v5.LogsApiServiceV5
import eu.wedgess.piholecontrol.data.api.v6.LogsApiServiceV6
import eu.wedgess.piholecontrol.data.mappers.DEFAULT_SUGGESTION_ENTRY
import eu.wedgess.piholecontrol.data.mappers.toEntity
import eu.wedgess.piholecontrol.data.mappers.toLogEntryEntity
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleLogSuggestionsResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleLogsResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleLogSuggestionsResponseDataV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleLogsResponseDataV6
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.LogFilterSuggestionsEntity
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
        clientIp: String?,
        clientName: String?,
        queryType: String?,
        advancedStatus: String?,
        from: Long?,
        until: Long?
    ): Result<List<PiHoleLogsEntity>> = withContext(dispatcherProvider.io) {
        callVersionedEndpoint(
            activeConnection = connection,
            v5Call = { apiV5.fetchLogs(it, limit) },
            v6Call = { connection ->
                apiV6.fetchLogs(
                    connection,
                    limit,
                    query.takeIf { it.isNotBlank() },
                    clientIp.takeIf { it != DEFAULT_SUGGESTION_ENTRY },
                    clientName.takeIf { it != DEFAULT_SUGGESTION_ENTRY },
                    queryType.takeIf { it != DEFAULT_SUGGESTION_ENTRY },
                    advancedStatus.takeIf { it != DEFAULT_SUGGESTION_ENTRY },
                    from,
                    until
                )
            },
            mapper = { response ->
                val result = when (response) {
                    is PiHoleLogsResponseDataV5 -> response.data.map { it.toLogEntryEntity() }
                        .run { filterByDomain(this@run, query) }
                        .run { filterByDateRange(this@run, from, until) }
                        .run {
                            filterV5ByClientIp(
                                this@run,
                                clientIp.takeIf { it != DEFAULT_SUGGESTION_ENTRY }
                            )
                        }
                        .run {
                            filterV5ByClientName(
                                this@run,
                                clientName.takeIf { it != DEFAULT_SUGGESTION_ENTRY }
                            )
                        }
                        .run {
                            filterV5ByAdvancedStatus(
                                this@run,
                                advancedStatus.takeIf { it != DEFAULT_SUGGESTION_ENTRY }
                            )
                        }
                        .run {
                            filterV5ByQueryType(
                                this@run,
                                queryType.takeIf { it != DEFAULT_SUGGESTION_ENTRY }
                            )
                        }

                    is PiHoleLogsResponseDataV6 -> response.queries.map { it.toLogEntryEntity() }
                    else -> throw IllegalArgumentException(
                        "Unknown type: ${response.javaClass.name}"
                    )
                }
                filterByStatus(result, status)
            }
        )
    }

    override suspend fun fetchLogFilterSuggestions(
        connection: ConnectionEntity
    ): Result<LogFilterSuggestionsEntity> = withContext(dispatcherProvider.io) {
        callVersionedEndpoint(
            activeConnection = connection,
            v5Call = { apiV5.fetchLogFilterSuggestions(it) },
            v6Call = { apiV6.fetchLogFilterSuggestions(it) },
            mapper = { response ->
                when (response) {
                    is PiHoleLogSuggestionsResponseDataV5 -> response.toEntity()
                    is PiHoleLogSuggestionsResponseDataV6 -> response.toEntity()
                    else -> throw IllegalArgumentException(
                        "Unknown type: ${response.javaClass.name}"
                    )
                }
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

    private fun filterByDomain(
        logs: List<PiHoleLogsEntity>,
        query: String
    ): List<PiHoleLogsEntity> =
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

    private fun filterV5ByClientIp(
        logs: List<PiHoleLogsEntity>,
        clientIp: String?
    ): List<PiHoleLogsEntity> = if (clientIp == null) {
        logs
    } else {
        logs.filter { log -> log.client == clientIp }
    }

    private fun filterV5ByClientName(
        logs: List<PiHoleLogsEntity>,
        clientName: String?
    ): List<PiHoleLogsEntity> = if (clientName == null) {
        logs
    } else {
        logs.filter { log -> log.client == clientName }
    }

    private fun filterV5ByAdvancedStatus(
        logs: List<PiHoleLogsEntity>,
        status: String?
    ): List<PiHoleLogsEntity> = if (status == null) {
        logs
    } else {
        logs
            .filterIsInstance<PiHoleLogsEntity.Version5>()
            .filter { log -> log.answerType.key == status }
    }

    private fun filterV5ByQueryType(
        logs: List<PiHoleLogsEntity>,
        queryType: String?
    ): List<PiHoleLogsEntity> = if (queryType == null) {
        logs
    } else {
        logs.filter { log -> log.queryType.key == queryType }
    }
}
