package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.data.model.responses.PiHoleModifyFilterRuleResponse
import eu.wedgess.piholecontrol.data.model.responses.PiHoleFilterRules
import eu.wedgess.piholecontrol.data.model.enums.PiHoleFilterRuleType

interface FilterRulesApiService {

    suspend fun fetchFilterRules(activeMiHole: ConnectionInfo, ruleType: PiHoleFilterRuleType): Result<PiHoleFilterRules>
    suspend fun addFilterRule(activeMiHole: ConnectionInfo, rule: String, ruleType: PiHoleFilterRuleType): Result<PiHoleModifyFilterRuleResponse>
    suspend fun removeFilterRule(activeMiHole: ConnectionInfo, rule: String, ruleType: PiHoleFilterRuleType): Result<PiHoleModifyFilterRuleResponse>

}