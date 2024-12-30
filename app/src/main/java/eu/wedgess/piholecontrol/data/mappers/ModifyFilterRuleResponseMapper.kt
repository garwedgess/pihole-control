package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleModifyFilterRuleResponse
import eu.wedgess.piholecontrol.domain.model.ModifyFilterRuleResponseEntity

fun PiHoleModifyFilterRuleResponse.toModifyFilterRuleResponseEntity() =
    ModifyFilterRuleResponseEntity(
        success = success,
        message = message
    )
