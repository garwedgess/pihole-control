package eu.wedgess.piholecontrol.data.api.v5

import eu.wedgess.piholecontrol.data.model.enums.PiHoleFilterRuleType
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleFilterRules
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleModifyFilterRuleResponse
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
