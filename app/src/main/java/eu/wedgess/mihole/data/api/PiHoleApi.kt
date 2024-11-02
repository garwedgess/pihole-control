package eu.wedgess.mihole.data.api

import eu.wedgess.mihole.data.model.PiHoleInfo
import eu.wedgess.mihole.data.model.responses.ModifyFilterRuleResponse
import eu.wedgess.mihole.data.model.PiHoleApiResponse
import eu.wedgess.mihole.data.model.responses.PiHoleClientsOverTimeData
import eu.wedgess.mihole.data.model.responses.PiHoleFilterRules
import eu.wedgess.mihole.data.model.responses.PiHoleForwardDestinations
import eu.wedgess.mihole.data.model.responses.PiHoleLogsResponse
import eu.wedgess.mihole.data.model.responses.PiHoleOverTimeData
import eu.wedgess.mihole.data.model.responses.PiHoleQueryTypes
import eu.wedgess.mihole.data.model.responses.PiHoleStatusResponse
import eu.wedgess.mihole.data.model.responses.PiHoleSummary
import eu.wedgess.mihole.data.model.responses.PiHoleTopClients
import eu.wedgess.mihole.data.model.responses.PiHoleTopQueries
import eu.wedgess.mihole.data.model.enums.FilterRuleType
import kotlin.time.Duration

interface PiHoleApi {

    suspend fun fetchStatus(activeMiHole: PiHoleInfo): Result<PiHoleStatusResponse>
    suspend fun fetchStatusSummary(activeMiHole: PiHoleInfo): Result<PiHoleSummary>
    suspend fun fetchOverTimeData10Minutes(activeMiHole: PiHoleInfo): Result<PiHoleOverTimeData>
    suspend fun fetchOverTimeDataClients(activeMiHole: PiHoleInfo): Result<PiHoleClientsOverTimeData>
    suspend fun fetchQueryTypes(activeMiHole: PiHoleInfo): Result<PiHoleQueryTypes>
    suspend fun fetchForwardDestinations(activeMiHole: PiHoleInfo): Result<PiHoleForwardDestinations>
    suspend fun fetchTopQueries(activeMiHole: PiHoleInfo): Result<PiHoleTopQueries>
    suspend fun fetchTopClients(activeMiHole: PiHoleInfo): Result<PiHoleTopClients>
    suspend fun fetchFilterRules(activeMiHole: PiHoleInfo, ruleType: FilterRuleType): Result<PiHoleFilterRules>
    suspend fun addFilterRule(activeMiHole: PiHoleInfo, rule: String, ruleType: FilterRuleType): Result<ModifyFilterRuleResponse>
    suspend fun removeFilterRule(activeMiHole: PiHoleInfo, rule: String, ruleType: FilterRuleType): Result<ModifyFilterRuleResponse>
    suspend fun fetchLogs(activeMiHole: PiHoleInfo, limit: Int): Result<PiHoleLogsResponse>
    suspend fun enableAdBlocking(activeMiHole: PiHoleInfo): Result<PiHoleStatusResponse>
    suspend fun disableAdBlocking(activeMiHole: PiHoleInfo, duration: Duration): Result<PiHoleStatusResponse>

}