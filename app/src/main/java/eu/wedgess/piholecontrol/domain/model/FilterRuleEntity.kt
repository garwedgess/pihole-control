package eu.wedgess.piholecontrol.domain.model

data class FilterRuleEntity(
    val id: Int,
    val enabled: Boolean,
    val comment: String?,
    val dateAdded: String,
    val dateModified: String,
    val domain: String,
    val groups: List<Int>,
    val type: FilterRuleTypeEntity
)
