package eu.wedgess.piholecontrol.data.model.responses

import kotlinx.serialization.Serializable

@Serializable
data class PiHoleModifyGroupResponseData(
    val groups: List<PiHoleGroupsResponseData.GroupData>,
    val processed: Processed? = null,
    val took: Double
) {
    @Serializable
    data class Processed(
        val success: List<SuccessItem> = emptyList(),
        val errors: List<ErrorItem> = emptyList()
    )

    @Serializable
    data class SuccessItem(
        val item: String
    )

    @Serializable
    data class ErrorItem(
        val item: String,
        val error: String
    )
}
