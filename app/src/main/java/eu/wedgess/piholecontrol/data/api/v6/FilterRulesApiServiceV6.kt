package eu.wedgess.piholecontrol.data.api.v6

import eu.wedgess.piholecontrol.data.model.enums.PiHoleFilterRuleTypeV6
import eu.wedgess.piholecontrol.data.model.requests.PiHoleAddFilterRuleRequestDataV6
import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleAddFilterRuleResponseDataV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleFilterRulesResponseDataV6
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

interface FilterRulesApiServiceV6 {

    suspend fun fetchFilterRules(
        connection: ConnectionEntity.Version6,
        ruleType: PiHoleFilterRuleTypeV6
    ): PiHoleApiResult<PiHoleFilterRulesResponseDataV6>

    suspend fun addFilterRule(
        connection: ConnectionEntity.Version6,
        body: PiHoleAddFilterRuleRequestDataV6,
        ruleType: PiHoleFilterRuleTypeV6
    ): PiHoleApiResult<PiHoleAddFilterRuleResponseDataV6>

    suspend fun removeFilterRule(
        connection: ConnectionEntity.Version6,
        rule: String,
        ruleType: PiHoleFilterRuleTypeV6
    ): PiHoleApiResult<Unit>
}
