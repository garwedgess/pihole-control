package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.PiHoleLogSuggestionsResponseData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleLogsResponseData
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

interface LogsApiService {
    suspend fun fetchLogs(
        connection: ConnectionEntity,
        limit: Int,
        domain: String?,
        clientIp: String?,
        clientName: String?,
        queryType: String?,
        advancedStatus: String?,
        from: Long?,
        until: Long?
    ): PiHoleApiResult<PiHoleLogsResponseData>

    suspend fun fetchLogFilterSuggestions(
        connection: ConnectionEntity
    ): PiHoleApiResult<PiHoleLogSuggestionsResponseData>
}
