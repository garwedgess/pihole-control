package eu.wedgess.piholecontrol.domain.model

data class GroupEntity(
    val name: String,
    val comment: String?,
    val enabled: Boolean,
    val id: Int,
    val dateAdded: String,
    val dateModified: String,
)
