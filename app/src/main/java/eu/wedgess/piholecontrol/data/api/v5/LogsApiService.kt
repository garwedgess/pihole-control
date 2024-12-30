package eu.wedgess.piholecontrol.data.api.v5

import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleLogsResponse
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

interface LogsApiService {
    suspend fun fetchLogs(activeMiHole: ConnectionEntity, limit: Int): Result<PiHoleLogsResponse>
}
