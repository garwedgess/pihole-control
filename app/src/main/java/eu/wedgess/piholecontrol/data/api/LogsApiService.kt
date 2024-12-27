package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.model.responses.PiHoleLogsResponse
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

interface LogsApiService {
    suspend fun fetchLogs(activeMiHole: ConnectionEntity, limit: Int): Result<PiHoleLogsResponse>
}
