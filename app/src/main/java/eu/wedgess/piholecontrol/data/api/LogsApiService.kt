package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.data.model.responses.PiHoleLogsResponse

interface LogsApiService {
    suspend fun fetchLogs(activeMiHole: ConnectionInfo, limit: Int): Result<PiHoleLogsResponse>
}