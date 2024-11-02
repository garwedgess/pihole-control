package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.data.model.responses.ModifyFilterRuleResponse
import eu.wedgess.piholecontrol.data.model.responses.PiHoleClientsOverTimeData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleFilterRules
import eu.wedgess.piholecontrol.data.model.responses.PiHoleForwardDestinations
import eu.wedgess.piholecontrol.data.model.responses.PiHoleLogsResponse
import eu.wedgess.piholecontrol.data.model.responses.PiHoleOverTimeData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleQueryTypes
import eu.wedgess.piholecontrol.data.model.responses.PiHoleStatusResponse
import eu.wedgess.piholecontrol.data.model.responses.PiHoleSummary
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopClients
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopQueries
import eu.wedgess.piholecontrol.data.model.enums.FilterRuleType
import kotlin.time.Duration

interface PiHoleApi {

    suspend fun fetchStatus(activeMiHole: ConnectionInfo): Result<PiHoleStatusResponse>
    suspend fun fetchStatusSummary(activeMiHole: ConnectionInfo): Result<PiHoleSummary>
    suspend fun fetchOverTimeData10Minutes(activeMiHole: ConnectionInfo): Result<PiHoleOverTimeData>
    suspend fun fetchOverTimeDataClients(activeMiHole: ConnectionInfo): Result<PiHoleClientsOverTimeData>
    suspend fun fetchQueryTypes(activeMiHole: ConnectionInfo): Result<PiHoleQueryTypes>
    suspend fun fetchForwardDestinations(activeMiHole: ConnectionInfo): Result<PiHoleForwardDestinations>
    suspend fun fetchTopQueries(activeMiHole: ConnectionInfo): Result<PiHoleTopQueries>
    suspend fun fetchTopClients(activeMiHole: ConnectionInfo): Result<PiHoleTopClients>
    suspend fun fetchFilterRules(activeMiHole: ConnectionInfo, ruleType: FilterRuleType): Result<PiHoleFilterRules>
    suspend fun addFilterRule(activeMiHole: ConnectionInfo, rule: String, ruleType: FilterRuleType): Result<ModifyFilterRuleResponse>
    suspend fun removeFilterRule(activeMiHole: ConnectionInfo, rule: String, ruleType: FilterRuleType): Result<ModifyFilterRuleResponse>
    suspend fun fetchLogs(activeMiHole: ConnectionInfo, limit: Int): Result<PiHoleLogsResponse>
    suspend fun enableAdBlocking(activeMiHole: ConnectionInfo): Result<PiHoleStatusResponse>
    suspend fun disableAdBlocking(activeMiHole: ConnectionInfo, duration: Duration): Result<PiHoleStatusResponse>

}