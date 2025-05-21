package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.responses.PiHoleLogSuggestionsResponseData
import eu.wedgess.piholecontrol.domain.model.LogFilterSuggestionsEntity

internal const val DEFAULT_SUGGESTION_ENTRY = "ALL"

fun PiHoleLogSuggestionsResponseData.toEntity() = LogFilterSuggestionsEntity(
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
