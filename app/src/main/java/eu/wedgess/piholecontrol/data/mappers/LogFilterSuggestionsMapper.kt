package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleLogSuggestionsResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleLogSuggestionsResponseDataV6
import eu.wedgess.piholecontrol.domain.model.LogFilterSuggestionsEntity
import eu.wedgess.piholecontrol.domain.model.PiHoleLogsEntity

internal const val DEFAULT_SUGGESTION_ENTRY = "ALL"

fun PiHoleLogSuggestionsResponseDataV5.toEntity() = LogFilterSuggestionsEntity(
    clientNames = buildList {
        val clientNameList = clients.map { it.name }.filterNot { it.isBlank() }
        if (clientNameList.isNotEmpty()) {
            add(DEFAULT_SUGGESTION_ENTRY)
        }
        addAll(clientNameList)
    },
    clientIpAddresses = buildList {
        val clientIpList = clients.map { it.ip }.filterNot { it.isBlank() }
        if (clientIpList.isNotEmpty()) {
            add(DEFAULT_SUGGESTION_ENTRY)
        }
        addAll(clientIpList)
    },
    statuses = buildList {
        add(DEFAULT_SUGGESTION_ENTRY)
        addAll(PiHoleLogsEntity.LogsAnswerTypeEntity.entries.map { it.key })
    },
    queryTypes = buildList {
        add(DEFAULT_SUGGESTION_ENTRY)
        addAll(PiHoleLogsEntity.LogEntryQueryTypeEntity.entries.map { it.key })
    },
    domains = emptyList(),
    upstreams = emptyList(),
    replyTypes = emptyList(),
    dnsSecs = emptyList()
)

fun PiHoleLogSuggestionsResponseDataV6.toEntity() = LogFilterSuggestionsEntity(
    clientNames = buildList {
        if (this@toEntity.suggestions.clientNames.isNotEmpty()) {
            add(DEFAULT_SUGGESTION_ENTRY)
        }
        addAll(this@toEntity.suggestions.clientNames)
    },
    clientIpAddresses = buildList {
        if (this@toEntity.suggestions.clientIpAddresses.isNotEmpty()) {
            add(DEFAULT_SUGGESTION_ENTRY)
        }
        addAll(this@toEntity.suggestions.clientIpAddresses)
    },
    statuses = buildList {
        if (this@toEntity.suggestions.statuses.isNotEmpty()) {
            add(DEFAULT_SUGGESTION_ENTRY)
        }
        addAll(this@toEntity.suggestions.statuses)
    },
    queryTypes = buildList {
        if (this@toEntity.suggestions.queryTypes.isNotEmpty()) {
            add(DEFAULT_SUGGESTION_ENTRY)
        }
        addAll(this@toEntity.suggestions.queryTypes)
    },
    domains = this.suggestions.domains,
    upstreams = this.suggestions.upstreams,
    replyTypes = this.suggestions.replyTypes,
    dnsSecs = this.suggestions.dnsSecs
)
