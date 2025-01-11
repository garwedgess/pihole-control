package eu.wedgess.piholecontrol.data.api.v6

import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleLogsResponseDataV6
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

interface LogsApiServiceV6 {
    suspend fun fetchLogs(
        connection: ConnectionEntity.Version6,
        limit: Int,
        from: Long?,
        until: Long?
    ): PiHoleApiResult<PiHoleLogsResponseDataV6>
}
