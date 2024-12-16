package eu.wedgess.piholecontrol.presentation.filters.tab.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.presentation.common.previews.ThemePreview
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import eu.wedgess.piholecontrol.presentation.theme.domainsOnAdListBackground
import eu.wedgess.piholecontrol.presentation.theme.percentageBlockedBackground
import eu.wedgess.piholecontrol.presentation.theme.queriesBlockedBackground
import eu.wedgess.piholecontrol.presentation.theme.totalQueriesBackground

@Composable
fun FilterRuleItem(
    rule: FilterRuleEntity,
    onItemClicked: () -> Unit
) {
    val typePair = when (rule.type) {
        FilterRuleTypeEntity.ALLOW -> Pair(
            stringResource(id = R.string.filters_label_allowlist),
            MaterialTheme.colorScheme.totalQueriesBackground
        )

        FilterRuleTypeEntity.BLOCK -> Pair(
            stringResource(id = R.string.filters_label_blocklist),
            MaterialTheme.colorScheme.domainsOnAdListBackground
        )

        FilterRuleTypeEntity.REGEX_ALLOW -> Pair(
            stringResource(id = R.string.filters_label_allowlist_regex),
            MaterialTheme.colorScheme.queriesBlockedBackground
        )

        FilterRuleTypeEntity.REGEX_BLOCK -> Pair(
            stringResource(id = R.string.filters_label_blocklist_regex),
            MaterialTheme.colorScheme.percentageBlockedBackground
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClicked() }
            .padding(
                horizontal = PiHoleControlTheme.dimens.padding.screenContent,
                vertical = PiHoleControlTheme.dimens.padding.screenContent
            ),
        verticalArrangement = Arrangement.spacedBy(PiHoleControlTheme.dimens.padding.itemContentSmall)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(end = PiHoleControlTheme.dimens.padding.itemContent)
            ) {
                Text(
                    text = typePair.first,
                    color = typePair.second,
                    style = MaterialTheme.typography.labelSmall
                )
                Text(text = rule.domain, maxLines = 1, overflow = TextOverflow.Ellipsis)
                rule.comment?.takeIf { it.isNotBlank() }?.run {
                    Text(
                        text = this,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Normal,
                            color = LocalContentColor.current.copy(
                                alpha = PiHoleControlTheme.dimens.weight.secondaryTextAlpha
                            )
                        )
                    )
                }
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                text = rule.dateAdded,
                maxLines = 1,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@ThemePreview
@Composable
private fun FilterRuleItemPreview(
    @PreviewParameter(FilterListItemPreviewParameterProvider::class) filter: FilterRuleEntity
) {
    PiHoleControlTheme {
        Surface {
            FilterRuleItem(rule = filter, onItemClicked = {})
        }
    }
}

private class FilterListItemPreviewParameterProvider :
    PreviewParameterProvider<FilterRuleEntity> {
    override val values = sequenceOf(
        FilterRuleEntity(
            id = 0,
            dateAdded = "14-01-2022",
            dateModified = "14-01-2023",
            type = FilterRuleTypeEntity.BLOCK,
            domain = "www.google.com",
            enabled = true,
            comment = null,
            groups = emptyList()
        ),
        FilterRuleEntity(
            id = 1,
            dateAdded = "14-01-2022",
            dateModified = "14-01-2023",
            type = FilterRuleTypeEntity.REGEX_BLOCK,
            domain = "r[0-9]*----sn-.*.*.*.googlevideo.com",
            comment = "YouTube Ads",
            enabled = false,
            groups = emptyList()
        )
    )
}
