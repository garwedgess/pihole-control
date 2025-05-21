package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.model.enums.PiHoleFilterRuleType
import eu.wedgess.piholecontrol.data.model.requests.PiHoleAddFilterRuleRequestData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleAddFilterRuleResponseData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.PiHoleFilterRulesResponseData
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

interface FilterRulesApiService {

    suspend fun fetchFilterRules(
        connection: ConnectionEntity,
        ruleType: PiHoleFilterRuleType
    ): PiHoleApiResult<PiHoleFilterRulesResponseData>

    suspend fun addFilterRule(
        connection: ConnectionEntity,
        body: PiHoleAddFilterRuleRequestData,
        ruleType: PiHoleFilterRuleType
    ): PiHoleApiResult<PiHoleAddFilterRuleResponseData>

    suspend fun removeFilterRule(
        connection: ConnectionEntity,
        rule: String,
        ruleType: PiHoleFilterRuleType
    ): PiHoleApiResult<Unit>
}
