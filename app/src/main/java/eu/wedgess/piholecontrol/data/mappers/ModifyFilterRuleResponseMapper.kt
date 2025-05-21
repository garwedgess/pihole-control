package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.responses.PiHoleAddFilterRuleResponseData
import eu.wedgess.piholecontrol.domain.model.ModifyFilterRuleResponseEntity

fun PiHoleAddFilterRuleResponseData.toEntity(): ModifyFilterRuleResponseEntity {
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
