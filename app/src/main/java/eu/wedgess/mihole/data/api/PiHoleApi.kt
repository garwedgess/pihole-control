package eu.wedgess.mihole.data.api

import eu.wedgess.mihole.data.model.MiHolesInfo
import eu.wedgess.mihole.data.model.ModifyFilterRuleResponse
import eu.wedgess.mihole.data.model.PiHoleApiResponse
import eu.wedgess.mihole.data.model.PiHoleClientsOverTimeData
import eu.wedgess.mihole.data.model.PiHoleFilterRules
import eu.wedgess.mihole.data.model.PiHoleOverTimeData
import eu.wedgess.mihole.data.model.PiHoleStatistics
import eu.wedgess.mihole.data.model.PiHoleStatusResponse
import eu.wedgess.mihole.data.model.PiHoleSummary
import eu.wedgess.mihole.data.model.enums.FilterRuleType
import kotlin.time.Duration

interface PiHoleApi {

    suspend fun fetchStatusSummary(activeMiHole: MiHolesInfo): PiHoleApiResponse<PiHoleSummary>
    suspend fun fetchOverTimeData10Minutes(activeMiHole: MiHolesInfo): PiHoleApiResponse<PiHoleOverTimeData>
    suspend fun fetchOverTimeDataClients(activeMiHole: MiHolesInfo): PiHoleApiResponse<PiHoleClientsOverTimeData>
    suspend fun fetchStatistics(activeMiHole: MiHolesInfo): PiHoleApiResponse<PiHoleStatistics>
    suspend fun fetchFilterRules(activeMiHole: MiHolesInfo, ruleType: FilterRuleType): PiHoleApiResponse<PiHoleFilterRules>
    suspend fun addFilterRule(activeMiHole: MiHolesInfo, rule: String, ruleType: FilterRuleType): PiHoleApiResponse<ModifyFilterRuleResponse>
    suspend fun removeFilterRule(activeMiHole: MiHolesInfo, rule: String, ruleType: FilterRuleType): PiHoleApiResponse<ModifyFilterRuleResponse>
    suspend fun enableAdBlocking(activeMiHole: MiHolesInfo): PiHoleApiResponse<PiHoleStatusResponse>
    suspend fun disableAdBlocking(activeMiHole: MiHolesInfo, duration: Duration): PiHoleApiResponse<PiHoleStatusResponse>

}