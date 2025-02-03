package eu.wedgess.piholecontrol.presentation.filters.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Pattern
import androidx.compose.ui.graphics.vector.ImageVector
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.presentation.navigation.tabs.FilterTab
import eu.wedgess.piholecontrol.utils.UiText

enum class FilterByOption(val label: UiText, val icon: ImageVector) {
    ALLOW_EXACT(
        label = UiText.StringResource(R.string.filters_label_allowlist),
        icon = Icons.Default.CheckCircleOutline
    ),
    ALLOW_REGEX(
        label = UiText.StringResource(R.string.filters_label_allowlist_regex),
        icon = Icons.Default.Pattern
    ),
    DENY_EXACT(
        label = UiText.StringResource(R.string.filters_label_denylist),
        icon = Icons.Default.Block
    ),
    DENY_REGEX(
        label = UiText.StringResource(R.string.filters_label_denylist_regex),
        icon = Icons.Default.Pattern
    );

    companion object {
        fun getByTab(type: FilterTab): List<FilterByOption> {
            return when (type) {
                is FilterTab.AllowList -> listOf(ALLOW_EXACT, ALLOW_REGEX)
                is FilterTab.DenyList -> listOf(DENY_EXACT, DENY_REGEX)
            }
        }

        fun List<FilterByOption>.containsEntityEquivalent(entity: FilterRuleTypeEntity): Boolean {
            val option = when (entity) {
                FilterRuleTypeEntity.ALLOW -> ALLOW_EXACT
                FilterRuleTypeEntity.DENY -> DENY_EXACT
                FilterRuleTypeEntity.REGEX_ALLOW -> ALLOW_REGEX
                FilterRuleTypeEntity.REGEX_DENY -> DENY_REGEX
            }
            return this.contains(option)
        }

        fun FilterByOption.isAllow(): Boolean = this == ALLOW_EXACT || this == ALLOW_REGEX
    }
}
