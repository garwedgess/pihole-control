package eu.wedgess.piholecontrol.domain.mappers

import eu.wedgess.piholecontrol.data.model.responses.PiHoleModifyFilterRuleResponse
import eu.wedgess.piholecontrol.domain.model.ModifyFilterRuleResponseEntity

fun PiHoleModifyFilterRuleResponse.toModifyFilterRuleResponseEntity() =
    ModifyFilterRuleResponseEntity(
        success = success,
        message = message
    )