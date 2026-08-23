package eu.wedgess.piholecontrol.domain.model

data class FilterRuleUpdateEntity(
    val originalDomain: String,
    val domain: String,
    val groups: List<Int>,
    val comment: String?,
    val enabled: Boolean,
    val originalType: FilterRuleTypeEntity,
    val type: FilterRuleTypeEntity
)
