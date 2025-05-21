package eu.wedgess.piholecontrol.data.model.responses

import kotlinx.serialization.Serializable

@Serializable
data class PiHoleAddFilterRuleResponseData(
    val domains: List<PiHoleFilterRulesResponseData.PiHoleFilterRuleData>,
    val processed: PiHoleAddFilterRuleProcessedResponseData,
    val took: Double
) {

    @Serializable
    data class PiHoleAddFilterRuleProcessedResponseData(
        val success: List<PiHoleAddFilterRuleProcessedItemResponseData>,
        val errors: List<PiHoleAddFilterRuleProcessedErrorResponseData>
    ) {

        @Serializable
        data class PiHoleAddFilterRuleProcessedItemResponseData(
            val item: String
        )

        @Serializable
        data class PiHoleAddFilterRuleProcessedErrorResponseData(
            val item: String,
            val error: String
        )
    }
}
