package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.data.model.responses.PiHoleClientsOverTimeData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleOverTimeData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleSummary

interface DashboardApiService {
    suspend fun fetchStatusSummary(activeConnection: ConnectionInfo): Result<PiHoleSummary>
    suspend fun fetchOverTimeData10Minutes(activeConnection: ConnectionInfo): Result<PiHoleOverTimeData>
    suspend fun fetchOverTimeDataClients(activeConnection: ConnectionInfo): Result<PiHoleClientsOverTimeData>
}