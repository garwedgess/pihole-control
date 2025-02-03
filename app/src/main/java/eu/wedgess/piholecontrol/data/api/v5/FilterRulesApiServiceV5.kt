package eu.wedgess.piholecontrol.data.api.v5

import eu.wedgess.piholecontrol.data.model.enums.PiHoleFilterRuleType
import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleCombinedFilterRulesResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleModifyFilterRuleResponseDataV5
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

interface FilterRulesApiServiceV5 {

    suspend fun fetchCombinedFilterRules(
        connection: ConnectionEntity.Version5,
        ruleType: PiHoleFilterRuleType
    ): PiHoleApiResult<PiHoleCombinedFilterRulesResponseDataV5>

    suspend fun addFilterRule(
        connection: ConnectionEntity.Version5,
        rule: String,
        ruleType: PiHoleFilterRuleType
    ): PiHoleApiResult<PiHoleModifyFilterRuleResponseDataV5>

    suspend fun removeFilterRule(
        connection: ConnectionEntity.Version5,
        rule: String,
        ruleType: PiHoleFilterRuleType
    ): PiHoleApiResult<PiHoleModifyFilterRuleResponseDataV5>
}
