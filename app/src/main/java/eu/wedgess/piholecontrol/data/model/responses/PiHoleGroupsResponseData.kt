package eu.wedgess.piholecontrol.data.model.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleGroupsResponseData(
    val groups: List<GroupData>,
    val took: Double? = null
) {
    @Serializable
    data class GroupData(
        val name: String,
        val comment: String?,
        val enabled: Boolean,
        val id: Int,
        @SerialName("date_added") val dateAdded: Long,
        @SerialName("date_modified") val dateModified: Long
    )
}
