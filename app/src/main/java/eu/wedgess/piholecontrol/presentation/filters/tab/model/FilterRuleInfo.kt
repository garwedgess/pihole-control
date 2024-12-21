package eu.wedgess.piholecontrol.presentation.filters.tab.model

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.presentation.theme.domainsOnAdListBackground
import eu.wedgess.piholecontrol.presentation.theme.percentageBlockedBackground
import eu.wedgess.piholecontrol.presentation.theme.queriesBlockedBackground
import eu.wedgess.piholecontrol.presentation.theme.totalQueriesBackground

data class FilterRuleInfo(
    val id: Int,
    val enabled: Boolean,
    val comment: String?,
    val dateAdded: String,
    val dateModified: String,
    val domain: String,
    val groups: List<Int>,
    val type: FilterRuleTypeEntity
) {

    val typeTitle: String
        @Composable get() = when (type) {
            FilterRuleTypeEntity.ALLOW -> stringResource(id = R.string.filters_label_allowlist)
            FilterRuleTypeEntity.BLOCK -> stringResource(id = R.string.filters_label_blocklist)
            FilterRuleTypeEntity.REGEX_ALLOW -> stringResource(
                id = R.string.filters_label_allowlist_regex
            )

            FilterRuleTypeEntity.REGEX_BLOCK -> stringResource(
                id = R.string.filters_label_blocklist_regex
            )
        }

    val typeColor: Color
        @Composable get() = when (type) {
            FilterRuleTypeEntity.ALLOW -> MaterialTheme.colorScheme.totalQueriesBackground
            FilterRuleTypeEntity.BLOCK -> MaterialTheme.colorScheme.domainsOnAdListBackground
            FilterRuleTypeEntity.REGEX_ALLOW -> MaterialTheme.colorScheme.queriesBlockedBackground
            FilterRuleTypeEntity.REGEX_BLOCK -> MaterialTheme.colorScheme.percentageBlockedBackground
        }
}
