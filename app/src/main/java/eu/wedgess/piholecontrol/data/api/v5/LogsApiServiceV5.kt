package eu.wedgess.piholecontrol.data.api.v5

import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleLogsResponseDataV5
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

interface LogsApiServiceV5 {
    suspend fun fetchLogs(
        connection: ConnectionEntity.Version5,
        limit: Int
    ): PiHoleApiResult<PiHoleLogsResponseDataV5>
}
