package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.model.enums.PiHoleFilterRuleType
import eu.wedgess.piholecontrol.data.model.responses.PiHoleFilterRules
import eu.wedgess.piholecontrol.data.model.responses.PiHoleModifyFilterRuleResponse
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

interface FilterRulesApiService {

    suspend fun fetchFilterRules(
        connection: ConnectionEntity,
        ruleType: PiHoleFilterRuleType
    ): Result<PiHoleFilterRules>

    suspend fun addFilterRule(
        connection: ConnectionEntity,
        rule: String,
        ruleType: PiHoleFilterRuleType
    ): Result<PiHoleModifyFilterRuleResponse>

    suspend fun removeFilterRule(
        connection: ConnectionEntity,
        rule: String,
        ruleType: PiHoleFilterRuleType
    ): Result<PiHoleModifyFilterRuleResponse>
}
