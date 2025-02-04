package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleModifyFilterRuleResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleAddFilterRuleResponseDataV6
import eu.wedgess.piholecontrol.domain.model.ModifyFilterRuleResponseEntity

fun PiHoleModifyFilterRuleResponseDataV5.toEntity() = ModifyFilterRuleResponseEntity(
    success = success,
    message = message
)

fun PiHoleAddFilterRuleResponseDataV6.toEntity(): ModifyFilterRuleResponseEntity {
    val isSuccessful = this.processed.success.isNotEmpty()
    val message = if (isSuccessful) {
        this.processed.success.joinToString(separator = "\n") { it.item }
    } else {
        this.processed.errors.joinToString(separator = "\n") { "${it.item}: ${it.error}" }
    }
    return ModifyFilterRuleResponseEntity(
        success = isSuccessful,
        message = message
    )
}
