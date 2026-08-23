package eu.wedgess.piholecontrol.presentation.filters.model

import eu.wedgess.piholecontrol.domain.model.GroupEntity

data class FilterRuleDraft(
    val domain: String = "",
    val selectedGroups: Set<GroupEntity> = emptySet(),
    val comment: String = "",
    val isRegex: Boolean = false
) {
    val canConfirm: Boolean
        get() = domain.isNotBlank()
}
